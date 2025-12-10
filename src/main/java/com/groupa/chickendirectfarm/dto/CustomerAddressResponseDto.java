package com.groupa.chickendirectfarm.dto;

import java.util.List;

public record CustomerAddressResponseDto(
        Integer customerAddressId,
        String streetName,
        String phone,
        String email,
        Integer customerId,
        String customerName,
        List<PurchaseResponseDto> purchaseHistory
) {}