package com.groupa.chickendirectfarm.purchase.event;

import com.groupa.chickendirectfarm.purchase.Purchase;
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



    public PurchaseEvent updatePurchaseEvent(PurchaseEventDto purchaseEventDto){

        PurchaseEvent purchaseEvent = new PurchaseEvent(purchaseEventDto.shippedStatus(), purchaseService.getPurchaseById(purchaseEventDto.purchaseId()));
        return purchaseEventRepo.save(purchaseEvent);
    }


    public PurchaseEvent save(ShippedStatus shippedStatus, Purchase purchase){
        PurchaseEvent purchaseEvent = new PurchaseEvent(shippedStatus, purchase);
        return purchaseEventRepo.save(purchaseEvent);
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
