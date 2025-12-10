package com.groupa.chickendirectfarm.dto;

public record PurchaseBatchResponseDto(
        String breed,
        Integer quantity,
        Integer pricePerUnit,
        Integer batchPrice

) {
}
