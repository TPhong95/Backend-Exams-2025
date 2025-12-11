package com.groupa.chickendirectfarm.dto.purchasedtos;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseResponseDto(
        Integer purchaseId,
        List<PurchaseBatchResponseDto> batches,
        Integer totalQuantity,
        Integer shippingCharge,
        Long totalPrice,
        String shippedStatus,
        String shippingAddress,
        LocalDateTime orderDate
) {}
