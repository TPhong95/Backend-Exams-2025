package com.groupa.chickendirectfarm.purchaseBatch;

public record PurchaseBatchDto(
       int id,
       int quantity,
       int totalPrice,
       int purchaseId,
       int productId
) {}
