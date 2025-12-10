package com.groupa.chickendirectfarm.dto;

import java.util.List;

public record PurchaseCreateDto(
        int customerAddressId,
        int shippingPrice,
        List<PurchaseBatchCreateDto> purchaseBatchesDto
) {}
