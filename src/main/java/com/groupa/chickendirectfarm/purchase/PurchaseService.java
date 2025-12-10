package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.dto.CustomerAddressResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseBatchResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseDetailsResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseStatusHistoryDto;
import com.groupa.chickendirectfarm.exception.conflict.PurchaseAlreadyHandledException;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepo purchaseRepo;
    private final ProductOrchestrationService productOrchestrationService;
    private final PurchaseEventService purchaseEventService;

    public PurchaseService(PurchaseRepo purchaseRepo, ProductOrchestrationService productOrchestrationService, PurchaseEventService purchaseEventService) {
        this.purchaseRepo = purchaseRepo;
        this.productOrchestrationService = productOrchestrationService;
        this.purchaseEventService = purchaseEventService;
    }

    public Purchase save(Purchase purchase) {
        log.info("Saving purchase id {} with Customer Id: {}, total Price: {}, total quantity: {}", purchase.getId(), purchase.getCustomer().getId(), purchase.getTotalPrice(), purchase.getTotalQuantity());

        Purchase savedPurchase = purchaseRepo.save(purchase);
        log.info("Purchase saved successfully with id: {}", savedPurchase.getId());

        return savedPurchase;
    }

    public Purchase getPurchaseById(int id) {
        log.debug("Getting purchase with id: {}", id);
        return purchaseRepo.findById(id).orElseThrow(() -> {
            log.warn("Purchase not found with id: {}", id);
            return new PurchaseNotFoundException("Purchase with id " + id + " not found");
        });
    }


    public List<Purchase> getAllPurchases() {
        log.debug("Retrieving all purchases");
        List<Purchase> purchases = purchaseRepo.findAll();
        log.debug("Retrieved {} purchases", purchases.size());
        return purchases;
    }


    public void deletePurchaseById(int id) {
        log.info("ENTRY: Deleting purchase with id: {}", id);

        if (!purchaseRepo.existsById(id)) {
            log.warn("Delete failed, purchase with id: {} not found", id);
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }
        purchaseRepo.deleteById(id);
        log.info("EXIT: Purchase with id: {} deleted successfully", id);
    }

    public Purchase cancelPurchaseById(int id) {
        log.info("ENTRY: Canceling purchase with id: {}", id);

        Purchase purchase = getPurchaseById(id);

        if (purchase == null) {
            log.warn("Cancel failed, purchase with id: {} not found", id);
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }

        ShippedStatus shippedStatus = purchase.getPurchaseEvents().stream()
                .map(PurchaseEvent::getShippedStatus)
                .filter(status -> status == ShippedStatus.CANCELLED || status == ShippedStatus.DELIVERED)
                .findFirst()
                .orElse(null);

        if (shippedStatus == ShippedStatus.CANCELLED || shippedStatus == ShippedStatus.DELIVERED) {
            log.warn("Cancel failed, purchase already {} on purchase id: {}", shippedStatus.toString().toLowerCase(), id);
            throw new PurchaseAlreadyHandledException("Purchase with id " + id + " cannot be canceled since it's already " + shippedStatus.toString().toLowerCase() + ".");
        }

        log.debug("Processing cancellation of  purchase with id: {}, Restocking canceled products", id);

        List<PurchaseBatch> batches = purchase.getPurchaseBatches();
        for (PurchaseBatch purchaseBatch : batches) {
            log.debug("Restocking product Id: {}, breed: {}, with quantity of {} products",
                    purchaseBatch.getProduct().getId(),
                    purchaseBatch.getProduct().getBreed(),
                    purchaseBatch.getQuantity());

            productOrchestrationService.increaseStock(purchaseBatch.getProduct().getId(), purchaseBatch.getQuantity(), ProductEventAction.CANCELED);
        }
        purchaseEventService.save(ShippedStatus.CANCELLED, purchase);
        log.info("EXIT: Purchase with id: {} cancelled successfully and product stock restocked", id);
        return purchase;
    }
}