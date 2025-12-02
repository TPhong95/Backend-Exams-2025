package com.groupa.chickendirectfarm.purchase;
public record PurchaseDto(
        int id,
        int shippingCharge,
        long totalPrice,
        int customerId,
        int customerAddressId
) {}
