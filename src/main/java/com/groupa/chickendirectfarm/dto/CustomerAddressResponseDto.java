package com.groupa.chickendirectfarm.dto;

public record CustomerAddressResponseDto(
        Integer id,
        String streetName,
        String phone,
        String email
) {}
