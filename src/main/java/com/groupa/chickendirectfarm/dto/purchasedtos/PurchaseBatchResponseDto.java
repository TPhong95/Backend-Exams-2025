package com.groupa.chickendirectfarm.dto.purchasedtos;

public record PurchaseBatchResponseDto(
        String breed,
        Integer quantity,
        Integer pricePerUnit,
        Integer batchPrice

) {
}
