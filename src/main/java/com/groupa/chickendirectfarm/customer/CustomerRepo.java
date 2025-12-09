package com.groupa.chickendirectfarm.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);


}
