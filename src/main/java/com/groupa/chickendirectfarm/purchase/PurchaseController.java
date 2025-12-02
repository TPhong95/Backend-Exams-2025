package com.groupa.chickendirectfarm.purchase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public Purchase savePurchase(@RequestBody PurchaseDto purchaseDto){
        return purchaseService.save(purchaseDto);
    }

    @GetMapping("/{id}")
    public Purchase getPurchaseById(@PathVariable int id){
        return purchaseService.getPurchaseById(id);
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

}
