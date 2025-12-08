package com.groupa.chickendirectfarm.dto;

public record CustomerAddressCreateDto(
        String streetName,
        String phone,
        String email,
        int customerId
){}
