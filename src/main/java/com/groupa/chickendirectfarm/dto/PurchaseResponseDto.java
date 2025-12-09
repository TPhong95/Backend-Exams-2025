package com.groupa.chickendirectfarm.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseResponseDto(
        Integer purchaseId,
        List<PurchaseBatchResponseDto> batches,
        Integer totalQuantity,
        Long totalPrice,
        Integer shippingCharge,
        String shippedStatus,
        String shippingAddress,
        LocalDateTime orderDate
) {}
