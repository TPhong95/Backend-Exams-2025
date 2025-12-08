package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatchService;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseOrchestrationService purchaseOrchestrationService;
    public PurchaseController(PurchaseService purchaseService, PurchaseOrchestrationService purchaseOrchestrationService) {
        this.purchaseService = purchaseService;
        this.purchaseOrchestrationService = purchaseOrchestrationService;
    }

    @PostMapping()
    public ResponseEntity<Purchase> savePurchase(@RequestBody PurchaseDto purchaseDto){
        Purchase purchase = purchaseOrchestrationService.create(purchaseDto);
        return ResponseEntity.ok(purchaseService.save(purchase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable int id){
        var result = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Purchase>> getAllPurchases(){
        var  result = purchaseService.getAllPurchases();
        if (result.isEmpty()) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseById(@PathVariable int id){
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase with id " + id + " was deleted");
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Purchase> cancelPurchaseById(@PathVariable int id){
        purchaseService.cancelPurchaseById(id);
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }
}
