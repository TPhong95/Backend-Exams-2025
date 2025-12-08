package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.PurchaseCreateDto;
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
        Customer customer = customerService.getCustomerById(purchaseCreateDto.customerId());
        CustomerAddress customerAddress = customerAddressService.getCustomerAddressById(
                purchaseCreateDto.customerAddressId()
        );

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setCustomerAddress(customerAddress);
        purchase.setShippingCharge(purchaseCreateDto.shippingPrice());

        List<PurchaseBatch> batches = new ArrayList<>();
        long totalPrice = 0;
        int totalQuantity = 0;

        Set<Integer> productIds = new HashSet<>();
        for (PurchaseBatchCreateDto batchDto : purchaseCreateDto.purchaseBatchesDto()) {

            if (!productIds.add(batchDto.productId())) {
                throw new DuplicateProductInPurchaseException("Duplicate product in purchase batch with id: " + batchDto.productId());
            }


            Product product = productService. getProductById(batchDto. productId());

            productOrchestrationService.decreaseStock(
                    batchDto.productId(),
                    batchDto.quantity()
            );

            int batchTotal = product.getPrice() * batchDto.quantity();

            PurchaseBatch batch = new PurchaseBatch();
            batch.setPurchase(purchase);
            batch.setProduct(product);
            batch.setQuantity(batchDto.quantity());
            batch. setTotalPrice(batchTotal);

            batches.add(batch);
            totalPrice += batchTotal;
            totalQuantity += batchDto.quantity();
    }
        purchaseEventService.save(ShippedStatus.NOT_SHIPPED, purchase);
        purchase.setPurchaseBatches(batches);
        purchase.setTotalPrice(totalPrice + purchase.getShippingCharge());
        purchase.setTotalQuantity(totalQuantity);



        return purchaseService.save(purchase);

}
}
