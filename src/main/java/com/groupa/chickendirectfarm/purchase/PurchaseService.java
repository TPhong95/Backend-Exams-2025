package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
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
        return purchaseRepo.findById(id).orElseThrow(() -> new PurchaseNotFoundException("Purchase with id " + id + " not found"));
    }

    public List<Purchase> getAllPurchases(){
        return purchaseRepo.findAll();
    }

    public void deletePurchaseById(int id)
    {
        if (!purchaseRepo.existsById(id)){
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }
        purchaseRepo.deleteById(id);
    }
}
