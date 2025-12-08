package com.groupa.chickendirectfarm.dto;

import java.util.List;

public record CustomerResponseDto(
        Integer id,
        String name,
        String primaryPhone,
        String primaryEmail,
        List<CustomerAddressResponseDto> addresses,
        List<PurchaseResponseDto> purchaseHistory
) {
}
