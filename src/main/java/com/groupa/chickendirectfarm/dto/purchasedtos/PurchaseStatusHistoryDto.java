package com.groupa.chickendirectfarm.dto.purchasedtos;

import java.time.LocalDateTime;

public record PurchaseStatusHistoryDto(
        String status,
        LocalDateTime timestamp
) {}
