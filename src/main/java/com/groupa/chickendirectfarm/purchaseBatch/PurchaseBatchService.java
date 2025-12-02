package com.groupa.chickendirectfarm.purchaseBatch;

import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseBatchService {
    private final PurchaseBatchRepo purchaseBatchRepo;
    private final PurchaseService purchaseService;
    private final ProductService productService;
    public PurchaseBatchService(PurchaseBatchRepo purchaseBatchRepo, PurchaseService purchaseService, ProductService productService) {
        this.purchaseBatchRepo = purchaseBatchRepo;
        this.purchaseService = purchaseService;
        this.productService = productService;
    }

public PurchaseBatch save(PurchaseBatchDto purchaseBatchDto){
        var purchaseId = purchaseService.getPurchaseById(purchaseBatchDto.purchaseId());
        var productId = productService.getProductById(purchaseBatchDto.productId());
        var newPurchaseBatch = new PurchaseBatch(purchaseBatchDto.quantity(), purchaseBatchDto.totalPrice(), purchaseId, productId);
        return purchaseBatchRepo.save(newPurchaseBatch);
}

public PurchaseBatch getPurchaseBatchById(int id){
    return purchaseBatchRepo.findById(id).orElse(null);
}

public List<PurchaseBatch> getAllPurchaseBatches(){
    return purchaseBatchRepo.findAll();
}

public void deletePurchaseBatchById(int id){
        purchaseBatchRepo.deleteById(id);
 }

}
