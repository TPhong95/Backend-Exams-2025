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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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
        return purchaseRepo.save(purchase);
    }

    public Purchase getPurchaseById(int id) {
        return purchaseRepo.findById(id).orElseThrow(() -> new PurchaseNotFoundException("Purchase with id " + id + " not found"));
    }

    public PurchaseDetailsResponseDto getPurchaseDtoById(int id) {
        Purchase purchase = getPurchaseById(id);
        return convertToDetailsDto(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepo.findAll();
    }

    public List<PurchaseDetailsResponseDto> getAllPurchaseDtos() {
        return purchaseRepo.findAll().stream().map(this::convertToDetailsDto).toList();
    }

    public void deletePurchaseById(int id) {
        if (!purchaseRepo.existsById(id)) {
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }
        purchaseRepo.deleteById(id);
    }

    public void cancelPurchaseById(int id) {
        Purchase purchase = getPurchaseById(id);

        if (purchase == null) {
            throw new PurchaseNotFoundException("Purchase with id " + id + " not found");
        }


        ShippedStatus shippedStatus = purchase.getPurchaseEvents().stream()
                .map(PurchaseEvent::getShippedStatus)
                .filter(status -> status == ShippedStatus.CANCELLED || status == ShippedStatus.DELIVERED)
                .findFirst()
                .orElse(null);

        if (shippedStatus == ShippedStatus.CANCELLED || shippedStatus == ShippedStatus.DELIVERED) {
            throw new PurchaseAlreadyHandledException("Purchase with id " + id + " cannot be canceled since it's already " + shippedStatus.toString().toLowerCase() + ".");
        }

        List<PurchaseBatch> batches = purchase.getPurchaseBatches();
        for (PurchaseBatch purchaseBatch : batches) {
            productOrchestrationService.increaseStock(purchaseBatch.getProduct().getId(), purchaseBatch.getQuantity(), ProductEventAction.CANCELED);
        }
        purchaseEventService.save(ShippedStatus.CANCELLED, purchase);
    }

    private PurchaseBatchResponseDto convertBatchToDto(PurchaseBatch batch) {
        return new PurchaseBatchResponseDto(
                batch.getProduct().getBreed().toString(),
                batch.getQuantity(),
                batch.getProduct().getPrice(),
                batch.getTotalPrice()
        );
    }

    private CustomerAddressResponseDto convertAddressToDto(Purchase purchase) {
        return new CustomerAddressResponseDto(
                purchase.getCustomerAddress().getId(),
                purchase.getCustomerAddress().getStreetName(),
                purchase.getCustomerAddress().getPhone(),
                purchase.getCustomerAddress().getEmail()
        );
    }

    private PurchaseStatusHistoryDto convertEventToDto(PurchaseEvent event) {
        return new PurchaseStatusHistoryDto(
                event.getShippedStatus().toString(),
                event.getTimestamp()
        );
    }


    public PurchaseDetailsResponseDto convertToDetailsDto(Purchase purchase) {
        List<PurchaseBatchResponseDto> batches = purchase.getPurchaseBatches()
                .stream()
                .map(this::convertBatchToDto)
                .toList();

        List<PurchaseStatusHistoryDto> statusHistory = purchase.getPurchaseEvents()
                .stream()
                .map(this::convertEventToDto)
                .toList();

        String currentStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            currentStatus = "UNKNOWN";
        } else {
            currentStatus = purchase.getPurchaseEvents().getFirst().getShippedStatus(). toString();
        }

        LocalDateTime orderDate;
        if (purchase.getPurchaseEvents().isEmpty()) {
            orderDate = null;
        } else {
            orderDate = purchase.getPurchaseEvents().getLast().getTimestamp();
        }

        return new PurchaseDetailsResponseDto(
                purchase. getId(),
                orderDate,
                currentStatus,
                purchase. getCustomer().getName(),
                purchase.getCustomer().getPrimaryPhone(),
                purchase.getCustomer().getPrimaryEmail(),
                convertAddressToDto(purchase),
                batches,
                statusHistory,
                purchase.getTotalQuantity(),
                purchase.getShippingCharge(),
                purchase.getTotalPrice()
        );
    }
}