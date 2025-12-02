package com.groupa.chickendirectfarm.purchaseevent;

import java.time.LocalDateTime;

public record PurchaseEventDto(
        int id,
        LocalDateTime timestamp,
        ShippedStatus shippedStatus,
        int purchaseId
) {}
