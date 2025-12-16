package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;

    public PurchaseService(PurchaseRepo purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    public Purchase save(Purchase purchase) {
        log.info("Saving purchase id {} with Customer Id: {}, total Price: {}, total quantity: {}", purchase.getId(), purchase.getCustomer().getId(), purchase.getTotalPrice(), purchase.getTotalQuantity());

        Purchase savedPurchase = purchaseRepo.save(purchase);
        log.info("Purchase saved successfully with id: {}", savedPurchase.getId());

        return savedPurchase;
    }

    public Purchase getPurchaseById(int id) {
        log.debug("Getting purchase with id: {}", id);
        return purchaseRepo.findById(id).orElseThrow(() -> {
            log.warn("Purchase not found with id: {}", id);
            return new PurchaseNotFoundException("Purchase with id " + id + " not found");
        });
    }

    public List<Purchase> getAllPurchases() {
        log.debug("Retrieving all purchases");
        List<Purchase> purchases = purchaseRepo.findAll();
        log.debug("Retrieved {} purchases", purchases.size());
        return purchases;
    }


    public void deletePurchaseById(int id) {
        log.info("ENTRY: Deleting purchase with id: {}", id);

        if (!purchaseRepo.existsById(id)) {
            log.warn("Delete failed, purchase with id: {} not found", id);
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }

        Purchase purchase = getPurchaseById(id);

        Customer customer = purchase.getCustomer();
        customer.getPurchases().remove(purchase);

        purchaseRepo.deleteById(id);
        log.info("EXIT: Purchase with id: {} deleted successfully", id);
    }


}