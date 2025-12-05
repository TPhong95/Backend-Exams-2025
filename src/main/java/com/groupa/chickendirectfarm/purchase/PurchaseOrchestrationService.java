package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatchDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrchestrationService {
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final ProductOrchestrationService productOrchestrationService;
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;


    public PurchaseOrchestrationService(PurchaseService purchaseService, ProductService productService, ProductOrchestrationService productOrchestrationService, CustomerService customerService, CustomerAddressService customerAddressService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.productOrchestrationService = productOrchestrationService;
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
    }



    @Transactional
    public Purchase create(PurchaseDto purchaseDto) {
        Customer customer = customerService.getCustomerById(purchaseDto.customerId());
        CustomerAddress customerAddress = customerAddressService.getCustomerAddressById(
                purchaseDto.customerAddressId()
        );

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setCustomerAddress(customerAddress);
        purchase.setShippingCharge(purchaseDto.shippingPrice());

        List<PurchaseBatch> batches = new ArrayList<>();
        long totalPrice = 0;
        int totalQuantity = 0;


        for (PurchaseBatchDto batchDto : purchaseDto.purchaseBatchesDto()) {
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
        purchase.setPurchaseBatches(batches);
        purchase.setTotalPrice(totalPrice + purchase.getShippingCharge());
        purchase.setTotalQuantity(totalQuantity);

        return purchaseService.save(purchase);

}
}
