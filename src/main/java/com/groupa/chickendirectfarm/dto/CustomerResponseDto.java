package com.groupa.chickendirectfarm.dto;

import java.util.List;

public record CustomerResponseDto(
        Integer customerId,
        String name,
        String primaryPhone,
        String primaryEmail,
        List<CustomerAddressForCustomerDto> addresses,
        List<PurchaseResponseDto> purchaseHistory
) {}
