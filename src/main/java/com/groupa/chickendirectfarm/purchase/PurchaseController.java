package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatchDto;
import com.groupa.chickendirectfarm.purchaseBatch.PurchaseBatchService;
import com.groupa.chickendirectfarm.purchaseevent.PurchaseEvent;
import com.groupa.chickendirectfarm.purchaseevent.PurchaseEventService;
import com.groupa.chickendirectfarm.purchaseevent.PurchaseEventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseBatchService purchaseBatchService;
    private final PurchaseEventService purchaseEventService;
    public PurchaseController(PurchaseService purchaseService, PurchaseBatchService purchaseBatchService, PurchaseEventService purchaseEventService) {
        this.purchaseService = purchaseService;
        this.purchaseBatchService = purchaseBatchService;
        this.purchaseEventService = purchaseEventService;
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

    @PostMapping("/event")
    public ResponseEntity<PurchaseEvent> savePurchaseEvent(@RequestBody PurchaseEventDto purchaseEventDto ){
        return ResponseEntity.ok(purchaseEventService.save(purchaseEventDto));
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<PurchaseEvent> getPurchaseEventById(@PathVariable int id){
        return ResponseEntity.ok(purchaseEventService.getPurchaseEventById(id));
    }

    @GetMapping("/event")
    public ResponseEntity<List<PurchaseEvent>> getAllPurchaseEvents(){
        return ResponseEntity.ok(purchaseEventService.getAllPurchaseEvents());
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deletePurchaseEventById(@PathVariable int id){
        purchaseEventService.deletePurchaseEventById(id);
        return ResponseEntity.ok("Purchase Event with id " + id + " was deleted");
    }

}
