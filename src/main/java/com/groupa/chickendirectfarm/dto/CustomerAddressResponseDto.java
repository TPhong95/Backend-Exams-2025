package com.groupa.chickendirectfarm.dto;

public record CustomerAddressResponseDto(
        Integer customerAddressId,
        String streetName,
        String phone,
        String email
) {}
