package com.groupa.chickendirectfarm.customerAddress;

public record CustomerAddressDto(
        int id,
        String streetName,
        String phone,
        String email,
        int customerId
) {}
