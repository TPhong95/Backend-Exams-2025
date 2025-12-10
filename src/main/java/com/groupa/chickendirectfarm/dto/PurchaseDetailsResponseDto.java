package com.groupa.chickendirectfarm.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseDetailsResponseDto(
        Integer purchaseId,
        LocalDateTime orderDate,
        String currentStatus,

        String customerName,
        String customerPhone,
        String customerEmail,

        CustomerAddressForCustomerDto shippingAddress,

        List<PurchaseBatchResponseDto> batches,

        List<PurchaseStatusHistoryDto> statusHistory,

        Integer totalQuantity,
        Integer shippingCharge,
        Long totalPrice

) {
}
