package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatchDto;

import java.util.List;

public record PurchaseDto(
        int customerId,
        int customerAddressId,
        int shippingPrice,
        List<PurchaseBatchDto> purchaseBatchesDto
) {}
