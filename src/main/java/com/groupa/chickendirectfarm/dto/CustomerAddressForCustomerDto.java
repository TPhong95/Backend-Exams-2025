package com.groupa.chickendirectfarm.dto;

public record CustomerAddressForCustomerDto(
        Integer customerAddressId,
        String streetName,
        String phone,
        String email)
{}
