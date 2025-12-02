package com.groupa.chickendirectfarm.purchase.batch;

public record PurchaseBatchDto(
       int id,
       int quantity,
       int totalPrice,
       int purchaseId,
       int productId
) {}
