package com.groupa.chickendirectfarm.purchase.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseEventRepo extends JpaRepository<PurchaseEvent, Integer> {
}
