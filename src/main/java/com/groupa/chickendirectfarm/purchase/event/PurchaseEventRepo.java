package com.groupa.chickendirectfarm.purchase.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseEventRepo extends JpaRepository<PurchaseEvent, Integer> {
    @Query("SELECT pe FROM PurchaseEvent pe WHERE pe.purchase.id = :purchaseId")
    List<PurchaseEvent> findByPurchaseId(@Param("purchaseId") int purchaseId);
}
