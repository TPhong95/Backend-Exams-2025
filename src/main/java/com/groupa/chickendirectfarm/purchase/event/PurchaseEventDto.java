package com.groupa.chickendirectfarm.purchase.event;

import java.time.LocalDateTime;

public record PurchaseEventDto(
        ShippedStatus shippedStatus,
        int purchaseId
) {}
