package com.groupa.chickendirectfarm.dtomappers;

import com.groupa.chickendirectfarm.dto.PurchaseBatchResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseDetailsResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseStatusHistoryDto;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class PurchaseDtoMapper {
    private final CustomerDtoMapper customerDtoMapper;

    public PurchaseDtoMapper(CustomerDtoMapper customerDtoMapper) {
        this.customerDtoMapper = customerDtoMapper;
    }


    // ==================== PURCHASE BATCH ====================

    public PurchaseBatchResponseDto toPurchaseBatchDto(PurchaseBatch batch) {
        log.debug("Converting batch with id {} to DTO", batch.getId());

        return new PurchaseBatchResponseDto(
                batch.getProduct().getBreed().toString(),
                batch.getQuantity(),
                batch.getProduct().getPrice(),
                batch.getBatchPrice()
        );
    }

    // ==================== PURCHASE ====================

    public PurchaseResponseDto toPurchaseDto(Purchase purchase) {
        log.debug("Converting purchase with id {} to DTO", purchase.getId());

        List<PurchaseBatchResponseDto> batches = purchase.getPurchaseBatches()
                .stream()
                .map(this::toPurchaseBatchDto)
                .toList();

        String shippedStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            shippedStatus = "UNKNOWN";
        } else {
            shippedStatus = purchase.getPurchaseEvents().getFirst().getShippedStatus().toString();
        }

        LocalDateTime orderDate;
        if (purchase.getPurchaseEvents().isEmpty()) {
            orderDate = null;
        } else {
            orderDate = purchase.getPurchaseEvents().getLast().getTimestamp();
        }

        return new PurchaseResponseDto(
                purchase.getId(),
                batches,
                purchase.getTotalQuantity(),
                purchase.getShippingCharge(),
                purchase.getTotalPrice(),
                shippedStatus,
                purchase.getCustomerAddress().getStreetName(),
                orderDate
        );
    }

    public PurchaseDetailsResponseDto toPurchaseDetailsDto(Purchase purchase) {
        log.debug("Converting purchase with id {} to detailed DTO", purchase.getId());

        List<PurchaseBatchResponseDto> batches = purchase.getPurchaseBatches()
                .stream()
                .map(this::toPurchaseBatchDto)
                .toList();

        List<PurchaseStatusHistoryDto> statusHistory = purchase.getPurchaseEvents()
                .stream()
                .map(event -> new PurchaseStatusHistoryDto(
                        event.getShippedStatus().toString(),
                        event.getTimestamp()
                ))
                .toList();

        String currentStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            currentStatus = "UNKNOWN";
        } else {
            currentStatus = purchase.getPurchaseEvents().getFirst().getShippedStatus().toString();
        }

        LocalDateTime orderDate;
        if (purchase.getPurchaseEvents().isEmpty()) {
            orderDate = null;
        } else {
            orderDate = purchase.getPurchaseEvents().getLast().getTimestamp();
        }

        log.debug("Purchase DTO conversion completed, {} batches, {} status events converted", batches.size(), statusHistory.size());

        return new PurchaseDetailsResponseDto(
                purchase.getId(),
                orderDate,
                currentStatus,
                purchase.getCustomer().getName(),
                purchase.getCustomer().getPrimaryPhone(),
                purchase.getCustomer().getPrimaryEmail(),
                customerDtoMapper.toCustomerAddressDtoSimple(purchase.getCustomerAddress()),
                batches,
                statusHistory,
                purchase.getTotalQuantity(),
                purchase.getShippingCharge(),
                purchase.getTotalPrice()
        );
    }
}
