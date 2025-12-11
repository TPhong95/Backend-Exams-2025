package com.groupa.chickendirectfarm.dto.customerdtos;

import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseResponseDto;

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