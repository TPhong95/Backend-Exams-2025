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

        purchase.getPurchaseEvents().add(savedEvent);
        return savedEvent;
    }
}
