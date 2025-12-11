package com.groupa.chickendirectfarm.dto.customerdtos;

public record CustomerAddressCreateDto(
        String streetName,
        String phone,
        String email,
        int customerId
){}
