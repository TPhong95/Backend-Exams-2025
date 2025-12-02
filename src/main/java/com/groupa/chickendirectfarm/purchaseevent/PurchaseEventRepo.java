package com.groupa.chickendirectfarm.purchaseevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseEventRepo extends JpaRepository<PurchaseEvent, Integer> {
}
