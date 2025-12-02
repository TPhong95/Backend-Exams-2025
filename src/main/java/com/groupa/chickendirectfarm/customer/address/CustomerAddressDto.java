package com.groupa.chickendirectfarm.customer.address;

public record CustomerAddressDto(
        int id,
        String streetName,
        String phone,
        String email,
        int customerId
) {}
