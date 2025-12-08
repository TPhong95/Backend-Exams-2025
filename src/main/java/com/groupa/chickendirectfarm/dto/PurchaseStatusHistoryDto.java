package com.groupa.chickendirectfarm.dto;

import java.time.LocalDateTime;

public record PurchaseStatusHistoryDto(
        String status,
        LocalDateTime timestamp
) {}
