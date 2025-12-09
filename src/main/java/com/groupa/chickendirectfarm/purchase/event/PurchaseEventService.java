package com.groupa.chickendirectfarm.purchase.event;

import com.groupa.chickendirectfarm.purchase.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PurchaseEventService {
    private final PurchaseEventRepo purchaseEventRepo;

    public PurchaseEventService(PurchaseEventRepo purchaseEventRepo) {
        this.purchaseEventRepo = purchaseEventRepo;
    }

    public PurchaseEvent save(ShippedStatus shippedStatus, Purchase purchase){
        log.info("Saving purchase event on purchase Id: {}, with shipped status {}", purchase.getId(), shippedStatus);
        PurchaseEvent purchaseEvent = new PurchaseEvent(shippedStatus, purchase);
        PurchaseEvent savedEvent = purchaseEventRepo.save(purchaseEvent);
        log.info("Purchase event saved on purchase Id: {}, with shipped status {}", savedEvent.getId(), shippedStatus);

        //?
        purchase.getPurchaseEvents().add(savedEvent);

        return savedEvent;
    }

    //Tror ikke vi trenger denne
    public PurchaseEvent getPurchaseEventById(int id){
        return purchaseEventRepo.findById(id).orElseThrow();
    }

    //Tror ikke vi trenger denne
    public List<PurchaseEvent> getAllPurchaseEvents(){
        return purchaseEventRepo.findAll();
    }

    //Tror ikke vi trenger denne
    public void deletePurchaseEventById(int id){
        purchaseEventRepo.deleteById(id);
    }


    public List<PurchaseEvent> getEventsByPurchaseId(int purchaseId) {
        log.debug("Retrieving purchase event from purchase Id: {}", purchaseId);
        List<PurchaseEvent> purchaseEvents = purchaseEventRepo.findByPurchaseId(purchaseId);
        log.debug("Retrived {} events from purchase Id: {}", purchaseEvents.size(), purchaseId);
        return purchaseEvents;
    }
}
