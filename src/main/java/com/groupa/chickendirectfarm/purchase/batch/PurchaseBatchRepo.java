package com.groupa.chickendirectfarm.purchase.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseBatchRepo extends JpaRepository<PurchaseBatch, Integer> {
}
