package com.groupa.chickendirectfarm.dto.purchasedtos;

import com.groupa.chickendirectfarm.dto.customerdtos.CustomerAddressForCustomerDto;
import jakarta.persistence.OrderBy;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseMoreDetailsResponseDto(
        Integer purchaseId,
        LocalDateTime orderDate,
        String currentStatus,

        String customerName,
        String customerPhone,
        String customerEmail,

        CustomerAddressForCustomerDto shippingAddress,

        List<PurchaseBatchResponseDto> batches,

        @OrderBy("timestamp DESC")
        List<PurchaseStatusHistoryDto> statusHistory,

        Integer totalQuantity,
        Integer shippingCharge,
        Long totalPrice

) {
}
