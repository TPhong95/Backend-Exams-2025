package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;

    public PurchaseService(PurchaseRepo purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepo.save(purchase);
    }

    public Purchase getPurchaseById(int id){
        return purchaseRepo.findById(id).orElseThrow();
    }

    public List<Purchase> getAllPurchases(){
        return purchaseRepo.findAll();
    }

    public void deletePurchaseById(int id){
        purchaseRepo.deleteById(id);
    }
}
