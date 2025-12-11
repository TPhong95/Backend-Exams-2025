package com.groupa.chickendirectfarm.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository <Product, Integer> {
    Boolean existsByBreed(Breed breed);
    List<Product> findAllByOrderByIdAsc();

}
