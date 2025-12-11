package com.groupa.chickendirectfarm.dto.productdtos;

import com.groupa.chickendirectfarm.product.Breed;
import jakarta.persistence.OrderBy;


import java.util.List;

public record ProductResponseDto(
        Integer productId,
        Breed breed,
        String description,
        int price,
        int quantity,
        @OrderBy("timestamp DESC")
        List<ProductEventResponseDto> productEvents
) {}
