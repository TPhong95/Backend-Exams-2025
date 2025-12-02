package com.groupa.chickendirectfarm.purchase;

import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    public PurchaseService(PurchaseRepo purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    /*public Purchase save(PurchaseDto purchaseDto){
        var cumstomerId =
    }
     */



}
