package com.groupa.chickendirectfarm.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.groupa.chickendirectfarm.productbatch.ProductBatch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Integer id;

    private String breed;
    private String description;
    private int price;
    private String stockStatus;
    private int quantity;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    private List<ProductBatch> productBatches;

    public Product(String breed, String description, int price, String stockStatus, int quantity) {
        this.breed = breed;
        this.description = description;
        this.price = price;
        this.stockStatus = stockStatus;
        this.quantity = quantity;
        this.productBatches = null;
    }
}