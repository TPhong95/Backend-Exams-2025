package com.groupa.chickendirectfarm.customer.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepo extends JpaRepository<CustomerAddress, Integer> {
    Page<CustomerAddress> findAll(Pageable pageable);

    @Query("SELECT DISTINCT ca FROM CustomerAddress ca WHERE SIZE(ca.purchases) > 0")
    Page<CustomerAddress> findAddressesWithPurchases(Pageable pageable);
}
