package com.groupa.chickendirectfarm.purchaseevent;

import com.groupa.chickendirectfarm.purchase.PurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseEventService {
    private final PurchaseEventRepo purchaseEventRepo;
    private final PurchaseService purchaseService;

    public PurchaseEventService(PurchaseEventRepo purchaseEventRepo, PurchaseService purchaseService) {
        this.purchaseEventRepo = purchaseEventRepo;
        this.purchaseService = purchaseService;
    }

    public PurchaseEvent save(PurchaseEventDto purchaseEventDto){
        var purchaseId = purchaseService.getPurchaseById(purchaseEventDto.purchaseId());
        var newPurchaseEvent = new PurchaseEvent(purchaseEventDto.timestamp(), purchaseEventDto.shippedStatus(), purchaseId);
        return purchaseEventRepo.save(newPurchaseEvent);
    }
    public PurchaseEvent getPurchaseEventById(int id){
        return purchaseEventRepo.findById(id).orElse(null);
    }

    public List<PurchaseEvent> getAllPurchaseEvents(){
        return purchaseEventRepo.findAll();
    }

    public void deletePurchaseEventById(int id){
        purchaseEventRepo.deleteById(id);
    }
}
