package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.badrequest.CustomerAddressDoesNotExistInCustomerException;
import com.groupa.chickendirectfarm.exception.conflict.DuplicateProductInPurchaseException;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.dto.PurchaseBatchCreateDto;
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



    @Transactional
    public Purchase create(PurchaseCreateDto purchaseCreateDto) {
        log.info("ENTRY: Creating new purchase for customer ID: {} with address: {}", purchaseCreateDto.customerId(), purchaseCreateDto.customerAddressId());

        Customer customer = customerService.getCustomerById(purchaseCreateDto.customerId());
        log.debug("Customer retrieved with name: {}",customer.getName());

        CustomerAddress customerAddress = customerAddressService.getCustomerAddressById(
                purchaseCreateDto.customerAddressId()
        );
        log.debug("Customer address retrieved with streetname: {}",customerAddress.getStreetName());

        if (customer.getId() != customerAddress.getCustomer().getId()) {
            log.warn("The customer id in customer address payload does not match the database, received Id: {}", customerAddress.getCustomer().getId()
            );
            throw new CustomerAddressDoesNotExistInCustomerException("Customer address was not found in the list of addresses for Customer Id: " + customer.getId());
        }

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setCustomerAddress(customerAddress);
        purchase.setShippingCharge(purchaseCreateDto.shippingPrice());
        log.debug("Purchase object initialized with shipping charge");

        List<PurchaseBatch> batches = new ArrayList<>();
        long totalPrice = 0;
        int totalQuantity = 0;

        Set<Integer> productIds = new HashSet<>();

        int batchNumber = 0;

        for (PurchaseBatchCreateDto batchDto : purchaseCreateDto.purchaseBatchesDto()) {
            batchNumber++;
            log.debug("Processing batch number: {}, product Id: {}", batchNumber, batchDto.productId());

            if (!productIds.add(batchDto.productId())) {
                log.warn("Duplicate product detected in purchase with product Id: {}", batchDto.productId());
                throw new DuplicateProductInPurchaseException("Duplicate product in purchase batch with id: " + batchDto.productId());
            }


            Product product = productService. getProductById(batchDto. productId());
            log.debug("Product retrieved breed: {}, price: {}, quantity: {}", product.getBreed(), product.getPrice(), product.getQuantity());

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
            batch.setTotalPrice(batchTotal);

            batches.add(batch);
            totalPrice += batchTotal;
            totalQuantity += batchDto.quantity();

            log.debug("Batch number {} done processing. total cost for batch {}, total quantity for batch {}", batchNumber, batchTotal, batchDto.quantity());
    }

        log.debug("All batches done with processing, {} batches with a total cost of purchase {}, and total quantity of products {}",batches.size(), totalPrice, totalQuantity);

        purchase.setPurchaseBatches(batches);
        purchase.setTotalPrice(totalPrice + purchase.getShippingCharge());
        purchase.setTotalQuantity(totalQuantity);

        Purchase savedPurchase = purchaseService.save(purchase);
        purchaseEventService.save(ShippedStatus.NOT_SHIPPED, savedPurchase);


        log.info("EXIT: Purchase ID: {} created with {} batches, total price of {}, on the address {} with customer{};",
                savedPurchase.getId(),
                savedPurchase.getPurchaseBatches().size(),
                savedPurchase.getTotalPrice(),
                savedPurchase.getCustomerAddress().getStreetName(),
                savedPurchase.getCustomer().getName());

        return savedPurchase;

}
}
