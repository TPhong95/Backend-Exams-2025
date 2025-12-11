package com.groupa.chickendirectfarm.dto.productdtos;

import com.groupa.chickendirectfarm.product.StockStatus;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;

import java.time.LocalDateTime;

public record ProductEventResponseDto(
        Integer productEventId,
        StockStatus stockStatus,
        int previousQuantity,
        int incomingQuantity,
        int newQuantity,
        ProductEventAction productEventAction,
        LocalDateTime timestamp
) {
}
