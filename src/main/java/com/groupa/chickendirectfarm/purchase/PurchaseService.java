package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    private final ProductOrchestrationService productOrchestrationService;
    private final PurchaseEventService purchaseEventService;

    public PurchaseService(PurchaseRepo purchaseRepo, ProductOrchestrationService productOrchestrationService, PurchaseEventService purchaseEventService) {
        this.purchaseRepo = purchaseRepo;
        this.productOrchestrationService = productOrchestrationService;
        this.purchaseEventService = purchaseEventService;
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

    public void cancelPurchaseById(int id)
    {
        Purchase purchase = getPurchaseById(id);

        if (purchase == null){
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }




        List<PurchaseBatch> batches = purchase.getPurchaseBatches();
        for (PurchaseBatch purchaseBatch : batches) {
            productOrchestrationService.increaseStock(purchaseBatch.getProduct().getId(), purchaseBatch.getQuantity(), ProductEventAction.CANCELED);
        }
        purchaseEventService.save(ShippedStatus.CANCELLED, purchase);
    }
}
