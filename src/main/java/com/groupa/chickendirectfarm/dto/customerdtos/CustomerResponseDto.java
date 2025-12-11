package com.groupa.chickendirectfarm.dto.customerdtos;

import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseResponseDto;

import java.util.List;

public record CustomerResponseDto(
        Integer customerId,
        String name,
        String primaryPhone,
        String primaryEmail,
        List<CustomerAddressForCustomerDto> addresses,
        List<PurchaseResponseDto> purchaseHistory
) {}
