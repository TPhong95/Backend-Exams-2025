package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseEventCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseMoreDetailsResponseDto;
import com.groupa.chickendirectfarm.mapper.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseOrchestrationService purchaseOrchestrationService;
    private final DtoMapper dtoMapper;
    public PurchaseController(PurchaseService purchaseService, PurchaseOrchestrationService purchaseOrchestrationService, DtoMapper dtoMapper) {
        this.purchaseService = purchaseService;
        this.purchaseOrchestrationService = purchaseOrchestrationService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping()
    public ResponseEntity<PurchaseMoreDetailsResponseDto> savePurchase(@RequestBody PurchaseCreateDto purchaseCreateDto){
        Purchase result = purchaseOrchestrationService.create(purchaseCreateDto);
        return ResponseEntity.ok(dtoMapper.toPurchaseMoreDetailsDto(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseMoreDetailsResponseDto> getPurchaseById(@PathVariable int id){
        Purchase result = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(dtoMapper.toPurchaseMoreDetailsDto(result));
    }

    @GetMapping()
    public ResponseEntity<List<PurchaseMoreDetailsResponseDto>> getAllPurchases(){
        List<Purchase> purchases = purchaseService.getAllPurchases();
        if (purchases.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PurchaseMoreDetailsResponseDto> dtos = purchases.stream()
                .map(dtoMapper::toPurchaseMoreDetailsDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseById(@PathVariable int id){
        purchaseService.deletePurchaseById(id);
        return ResponseEntity.ok("Purchase with id " + id + " was deleted");
    }


    @PostMapping("/cancel/{id}")
    public ResponseEntity<PurchaseMoreDetailsResponseDto> cancelPurchaseById(@PathVariable int id){
        Purchase result = purchaseOrchestrationService.cancelPurchaseById(id);
        return ResponseEntity.ok(dtoMapper.toPurchaseMoreDetailsDto(result));
    }

    @PostMapping("/update")
    public ResponseEntity<PurchaseMoreDetailsResponseDto> updatePurchaseById(@RequestBody PurchaseEventCreateDto event){
        Purchase result = purchaseOrchestrationService.updatePurchaseById(event.purchaseId(), event.shippedStatus());
        return ResponseEntity.ok(dtoMapper.toPurchaseMoreDetailsDto(result));
    }
}
