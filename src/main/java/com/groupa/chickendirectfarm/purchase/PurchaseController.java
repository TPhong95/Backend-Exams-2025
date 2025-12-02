package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatchDto;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseBatchService purchaseBatchService;
    public PurchaseController(PurchaseService purchaseService, PurchaseBatchService purchaseBatchService) {
        this.purchaseService = purchaseService;
        this.purchaseBatchService = purchaseBatchService;
    }

    @PostMapping
    public ResponseEntity<Purchase> savePurchase(@RequestBody PurchaseDto purchaseDto){
        return ResponseEntity.ok(purchaseService.save(purchaseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable int id){
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Purchase>> getAllPurchases(){
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseById(@PathVariable int id){
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase with id " + id + " was deleted");
    }

    @PostMapping("/batch")
    public ResponseEntity<PurchaseBatch> savePurchaseBatch(@RequestBody PurchaseBatchDto purchaseBatchDto){
        return ResponseEntity.ok(purchaseBatchService.save(purchaseBatchDto));
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<PurchaseBatch> getPurchaseBatchById(@PathVariable int id){
        return ResponseEntity.ok(purchaseBatchService.getPurchaseBatchById(id));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<PurchaseBatch>> getAllPurchaseBatches(){
        return ResponseEntity.ok(purchaseBatchService.getAllPurchaseBatches());
    }

    @DeleteMapping("/batch/{id}")
    public ResponseEntity<String> deletePurchaseBatchById(@PathVariable int id){
        purchaseBatchService.deletePurchaseBatchById(id);
        return ResponseEntity.ok("Purchase Batch with id " + id + " was deleted");
    }

}
