package com.groupa.chickendirectfarm.purchase.batch;

import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseBatchService {
    private final PurchaseBatchRepo purchaseBatchRepo;
    public PurchaseBatchService(PurchaseBatchRepo purchaseBatchRepo) {
        this.purchaseBatchRepo = purchaseBatchRepo;
    }

public PurchaseBatch getPurchaseBatchById(int id){
    return purchaseBatchRepo.findById(id).orElseThrow();
}

public List<PurchaseBatch> getAllPurchaseBatches(){
    return purchaseBatchRepo.findAll();
}

public void deletePurchaseBatchById(int id){
        purchaseBatchRepo.deleteById(id);
 }

}
