package com.groupa.chickendirectfarm.dto.purchasedtos;

import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;

public record PurchaseEventCreateDto(
   int purchaseId,
   ShippedStatus shippedStatus
) {}
