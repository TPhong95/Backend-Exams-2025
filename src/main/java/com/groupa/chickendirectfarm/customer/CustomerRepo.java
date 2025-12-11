package com.groupa.chickendirectfarm.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByPrimaryPhone(String primaryPhone);


}
