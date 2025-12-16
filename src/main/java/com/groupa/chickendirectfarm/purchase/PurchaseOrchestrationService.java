package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.conflict.BatchRequiredInPurchaseException;
import com.groupa.chickendirectfarm.exception.conflict.DuplicateProductInPurchaseException;
import com.groupa.chickendirectfarm.exception.conflict.PurchaseAlreadyHandledException;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PurchaseOrchestrationService {
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final ProductOrchestrationService productOrchestrationService;
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    private final PurchaseEventService purchaseEventService;


    public PurchaseOrchestrationService(PurchaseService purchaseService, ProductService productService, ProductOrchestrationService productOrchestrationService, CustomerService customerService, CustomerAddressService customerAddressService, PurchaseEventService purchaseEventService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.productOrchestrationService = productOrchestrationService;
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
        this.purchaseEventService = purchaseEventService;
    }


    //Vurdere å splitte opp denne store metoden (EKSTRAOPPGAVE)
    @Transactional
    public Purchase create(PurchaseCreateDto purchaseCreateDto) {
        log.info("ENTRY: Creating new purchase for customer with address Id: {}", purchaseCreateDto.customerAddressId());

        CustomerAddress customerAddress = customerAddressService.getCustomerAddressById(
                purchaseCreateDto.customerAddressId()
        );
        log.debug("Customer address retrieved with streetname: {}", customerAddress.getStreetName());

        Customer customer = customerService.getCustomerById(customerAddress.getCustomer().getId());
        log.debug("Customer retrieved with Id: {}, name: {}", customer.getId(), customer.getName());


        Set<Integer> productIds = new HashSet<>();
        for (PurchaseBatchCreateDto batchDto : purchaseCreateDto.purchaseBatchesDto()) {
            if (!productIds.add(batchDto.productId())) {
                log.warn("Duplicate product detected in purchase with product Id: {}", batchDto.productId());
                throw new DuplicateProductInPurchaseException("Duplicate product in purchase batch with id: " + batchDto.productId());
            }
        }

        if (productIds.isEmpty()) {
            log.error("Purchase creation failed: No batches provided");
            throw new BatchRequiredInPurchaseException("Purchase creation failed: No batches provided");
        }

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setCustomerAddress(customerAddress);
        purchase.setShippingCharge(purchaseCreateDto.shippingPrice());
        log.debug("Purchase object initialized with shipping charge");

        List<PurchaseBatch> batches = new ArrayList<>();
        long totalPrice = 0;
        int totalQuantity = 0;
        int batchNumber = 0;


        for (PurchaseBatchCreateDto batchDto : purchaseCreateDto.purchaseBatchesDto()) {
            batchNumber++;
            log.debug("Processing batch number: {}, product Id: {}", batchNumber, batchDto.productId());

            Product product = productService.getProductById(batchDto.productId());
            log.debug("Product retrieved breed: {}, price: {}, quantity: {}",
                    product.getBreed(), product.getPrice(), product.getQuantity());

            productOrchestrationService.decreaseStock(
                    batchDto.productId(),
                    batchDto.quantity()
            );
            log.debug("Product Id: {} decreased quantity by {}", batchDto.productId(), batchDto.quantity());

            int batchTotal = product.getPrice() * batchDto.quantity();

            PurchaseBatch batch = new PurchaseBatch();
            batch.setPurchase(purchase);
            batch.setProduct(product);
            batch.setQuantity(batchDto.quantity());
            batch.setBatchPrice(batchTotal);

            batches.add(batch);
            totalPrice += batchTotal;
            totalQuantity += batchDto.quantity();

            log.debug("Batch number {} done processing. total cost for batch {}, total quantity for batch {}",
                    batchNumber, batchTotal, batchDto.quantity());
        }

        log.debug("All batches done with processing, {} batches with a total cost of purchase {}, and total quantity of products {}",
                batches.size(), totalPrice, totalQuantity);

        purchase.setPurchaseBatches(batches);
        purchase.setTotalPrice(totalPrice + purchase.getShippingCharge());
        purchase.setTotalQuantity(totalQuantity);

        Purchase savedPurchase = purchaseService.save(purchase);
        customer.getPurchases().add(savedPurchase);
        purchaseEventService.save(ShippedStatus.NOT_SHIPPED, savedPurchase);

        log.info("EXIT: Purchase ID: {} created with {} batches, total price of {}, on the address {} with customer{};",
                savedPurchase.getId(),
                savedPurchase.getPurchaseBatches().size(),
                savedPurchase.getTotalPrice(),
                savedPurchase.getCustomerAddress().getStreetName(),
                savedPurchase.getCustomer().getName());

        return savedPurchase;
    }

    @Transactional
    public Purchase cancelPurchaseById(int id) {
        log.info("ENTRY: Canceling purchase with id: {}", id);

        Purchase purchase = purchaseService.getPurchaseById(id);

        ShippedStatus shippedStatus = purchase.getPurchaseEvents().reversed().stream()
                .map(PurchaseEvent::getShippedStatus)
                .filter(status -> status == ShippedStatus.CANCELLED || status == ShippedStatus.DELIVERED || status == ShippedStatus.SHIPPED)
                .findFirst()
                .orElse(null);

        if (shippedStatus == ShippedStatus.CANCELLED || shippedStatus == ShippedStatus.DELIVERED || shippedStatus == ShippedStatus.SHIPPED) {
            log.warn("Cancel failed, purchase already {} on purchase id: {}", shippedStatus.toString().toLowerCase(), id);
            throw new PurchaseAlreadyHandledException("Purchase with id " + id + " cannot be canceled since it's already " + shippedStatus.toString().toLowerCase() + ".");
        }

        log.debug("Processing cancellation of purchase with id: {}, Restocking canceled products", id);

        List<PurchaseBatch> batches = purchase.getPurchaseBatches();
        for (PurchaseBatch purchaseBatch : batches) {
            log.debug("Restocking product Id: {}, breed: {}, with quantity of {} products",
                    purchaseBatch.getProduct().getId(),
                    purchaseBatch.getProduct().getBreed(),
                    purchaseBatch.getQuantity());

            productOrchestrationService.increaseStock(purchaseBatch.getProduct().getId(), purchaseBatch.getQuantity(), ProductEventAction.CANCELED);
        }
        purchaseEventService.save(ShippedStatus.CANCELLED, purchase);
        log.info("EXIT: Purchase with id: {} cancelled successfully and product stock restocked", id);

        return purchase;
    }

    @Transactional
    public Purchase updatePurchaseById(int id, ShippedStatus newShippedStatus) {
        log.info("ENTRY: Updating purchase with id: {}", id);

        Purchase purchase = purchaseService.getPurchaseById(id);

        ShippedStatus shippedStatus = purchase.getPurchaseEvents().reversed().stream()
                .map(PurchaseEvent::getShippedStatus)
                .filter(status -> status == ShippedStatus.CANCELLED || status == ShippedStatus.DELIVERED || status == ShippedStatus.SHIPPED)
                .findFirst()
                .orElse(null);

        if (shippedStatus == ShippedStatus.CANCELLED || shippedStatus == ShippedStatus.DELIVERED || (shippedStatus == ShippedStatus.SHIPPED && newShippedStatus == ShippedStatus.SHIPPED)) {
            log.warn("Update failed, purchase already {} on purchase id: {}", shippedStatus.toString().toLowerCase(), id);
            throw new PurchaseAlreadyHandledException("Purchase with id " + id + " cannot be updated since it's already " + shippedStatus.toString().toLowerCase() + ".");
        }



        log.debug("Processing update of purchase with id: {}", id);


        purchaseEventService.save(newShippedStatus, purchase);
        log.info("EXIT: Purchase with id: {} updated successfully", id);

        return purchase;
    }
}
