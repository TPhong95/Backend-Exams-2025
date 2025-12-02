package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customerAddress.CustomerAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    public PurchaseService(PurchaseRepo purchaseRepo, CustomerService customerService, CustomerAddressService customerAddressService) {
        this.purchaseRepo = purchaseRepo;
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
    }

    public Purchase save(PurchaseDto purchaseDto) {
        var customerId = customerService.getCustomerById(purchaseDto.customerId());
        var customerAddressId = customerAddressService.getCustomerAddressById(purchaseDto.customerAddressId());
        var newPurchase = new Purchase(purchaseDto.shippingCharge(), purchaseDto.totalPrice(), customerId, customerAddressId);
        return purchaseRepo.save(newPurchase);
    }

    public Purchase getPurchaseById(int id){
        return purchaseRepo.findById(id).orElse(null);
    }

    public List<Purchase> getAllPurchases(){
        return purchaseRepo.findAll();
    }

    public void deletePurchaseById(int id){
        purchaseRepo.deleteById(id);
    }
}
