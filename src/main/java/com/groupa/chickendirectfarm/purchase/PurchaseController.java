package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.dto.PurchaseCreateDto;
import com.groupa.chickendirectfarm.dto.PurchaseDetailsResponseDto;
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
    public ResponseEntity<PurchaseDetailsResponseDto> savePurchase(@RequestBody PurchaseCreateDto purchaseCreateDto){
        Purchase purchase = purchaseOrchestrationService.create(purchaseCreateDto);
        return ResponseEntity.ok(purchaseService.convertToDetailsDto(purchase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetailsResponseDto> getPurchaseById(@PathVariable int id){
        var result = purchaseService.getPurchaseDtoById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<PurchaseDetailsResponseDto>> getAllPurchases(){
        var  result = purchaseService.getAllPurchaseDtos();
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
