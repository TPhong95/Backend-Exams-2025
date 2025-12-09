package com.groupa.chickendirectfarm.purchase.event;

import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseEventService {
    private final PurchaseEventRepo purchaseEventRepo;

    public PurchaseEventService(PurchaseEventRepo purchaseEventRepo) {
        this.purchaseEventRepo = purchaseEventRepo;
    }

    public PurchaseEvent save(ShippedStatus shippedStatus, Purchase purchase){
        PurchaseEvent purchaseEvent = new PurchaseEvent(shippedStatus, purchase);
        PurchaseEvent saved = purchaseEventRepo.save(purchaseEvent);

        purchase.getPurchaseEvents().add(saved);

        return saved;
    }

    public PurchaseEvent getPurchaseEventById(int id){
        return purchaseEventRepo.findById(id).orElseThrow();
    }

    public List<PurchaseEvent> getAllPurchaseEvents(){
        return purchaseEventRepo.findAll();
    }

    public void deletePurchaseEventById(int id){
        purchaseEventRepo.deleteById(id);
    }

    public List<PurchaseEvent> getEventsByPurchaseId(int purchaseId) {
        return purchaseEventRepo.findByPurchaseId(purchaseId);
    }
}
