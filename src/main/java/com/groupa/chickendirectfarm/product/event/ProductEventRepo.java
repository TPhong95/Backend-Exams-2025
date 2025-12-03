package com.groupa.chickendirectfarm.product.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductEventRepo extends JpaRepository<ProductEvent, Integer> {
    @Query("SELECT pe FROM ProductEvent pe WHERE pe.product.id = :productId")
    List<ProductEvent> findByProductId(@Param("productId") int productId);
}
