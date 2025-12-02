package com.groupa.chickendirectfarm.purchase.event;

import java.time.LocalDateTime;

public record PurchaseEventDto(
        int id,
        LocalDateTime timestamp,
        ShippedStatus shippedStatus,
        int purchaseId
) {}
