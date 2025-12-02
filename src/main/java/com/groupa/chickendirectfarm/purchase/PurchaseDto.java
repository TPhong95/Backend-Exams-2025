package com.groupa.chickendirectfarm.purchase;


import java.util.List;

public record PurchaseDto(
        int id,
        int shippingCharge,
        long totalPrice,
        int customerId,
        int customerAddressId,
        List<Integer> purchaseBatchIds
) {}
