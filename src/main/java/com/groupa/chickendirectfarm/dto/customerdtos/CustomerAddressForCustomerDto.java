package com.groupa.chickendirectfarm.dto.customerdtos;

public record CustomerAddressForCustomerDto(
        Integer customerAddressId,
        String streetName,
        String phone,
        String email
)
{}
