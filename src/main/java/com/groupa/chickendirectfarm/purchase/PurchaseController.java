package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.dto.PurchaseCreateDto;
import com.groupa.chickendirectfarm.dto.PurchaseDetailsResponseDto;
import com.groupa.chickendirectfarm.dtomappers.PurchaseDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseOrchestrationService purchaseOrchestrationService;
    private final PurchaseDtoMapper purchaseDtoMapper;
    public PurchaseController(PurchaseService purchaseService, PurchaseOrchestrationService purchaseOrchestrationService, PurchaseDtoMapper purchaseDtoMapper) {
        this.purchaseService = purchaseService;
        this.purchaseOrchestrationService = purchaseOrchestrationService;
        this.purchaseDtoMapper = purchaseDtoMapper;
    }

    @PostMapping()
    public ResponseEntity<PurchaseDetailsResponseDto> savePurchase(@RequestBody PurchaseCreateDto purchaseCreateDto){
        Purchase result = purchaseOrchestrationService.create(purchaseCreateDto);
        return ResponseEntity.ok(purchaseDtoMapper.toPurchaseDetailsDto(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetailsResponseDto> getPurchaseById(@PathVariable int id){
        Purchase result = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(purchaseDtoMapper.toPurchaseDetailsDto(result));
    }

    @GetMapping()
    public ResponseEntity<List<PurchaseDetailsResponseDto>> getAllPurchases(){
        List<Purchase> purchases = purchaseService.getAllPurchases();
        if (purchases.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PurchaseDetailsResponseDto> dtos = purchases.stream()
                .map(purchaseDtoMapper::toPurchaseDetailsDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseById(@PathVariable int id){
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase with id " + id + " was deleted");
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<PurchaseDetailsResponseDto> cancelPurchaseById(@PathVariable int id){
        Purchase result = purchaseOrchestrationService.cancelPurchaseById(id);
        return ResponseEntity. ok(purchaseDtoMapper.toPurchaseDetailsDto(result));
    }
}
