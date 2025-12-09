package com.groupa.chickendirectfarm.purchase.batch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseBatchService {
    private final PurchaseBatchRepo purchaseBatchRepo;
    public PurchaseBatchService(PurchaseBatchRepo purchaseBatchRepo) {
        this.purchaseBatchRepo = purchaseBatchRepo;
    }

    //Tror ikke vi trenger denne
public PurchaseBatch getPurchaseBatchById(int id){
        return purchaseBatchRepo.findById(id).orElseThrow();
}
    //Tror ikke vi trenger denne
public List<PurchaseBatch> getAllPurchaseBatches(){
    return purchaseBatchRepo.findAll();
}

    //Tror ikke vi trenger denne
public void deletePurchaseBatchById(int id){
        purchaseBatchRepo.deleteById(id);
 }

}
