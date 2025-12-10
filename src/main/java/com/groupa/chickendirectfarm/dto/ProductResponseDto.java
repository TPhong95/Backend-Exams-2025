package com.groupa.chickendirectfarm.dto;

import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.event.ProductEvent;

import java.util.List;

public record ProductResponseDto(
        Integer productId,
        Breed breed,
        String description,
        int price,
        int quantity,
        List<ProductEventResponseDto> productEvents
) {}
