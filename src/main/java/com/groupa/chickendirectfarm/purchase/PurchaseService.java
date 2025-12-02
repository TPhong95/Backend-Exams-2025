package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customerAddress.CustomerAddressService;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    private final PurchaseBatchService purchaseBatchService;

    public PurchaseService(PurchaseRepo purchaseRepo, CustomerService customerService, CustomerAddressService customerAddressService, PurchaseBatchService purchaseBatchService) {
        this.purchaseRepo = purchaseRepo;
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
        this.purchaseBatchService = purchaseBatchService;
    }

    public Purchase save(PurchaseDto purchaseDto) {
        var customerId = customerService.getCustomerById(purchaseDto.customerId());
        var customerAddressId = customerAddressService.getCustomerAddressById(purchaseDto.customerAddressId());
        List<PurchaseBatch> purchaseBatchIdList = new ArrayList<>();
        purchaseDto.purchaseBatchIds().forEach(p -> purchaseBatchIdList.add(purchaseBatchService.getPurchaseBatchById(p)));
        var newPurchase = new Purchase(purchaseDto.shippingCharge(), purchaseDto.totalPrice(), customerId, customerAddressId, purchaseBatchIdList);
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
