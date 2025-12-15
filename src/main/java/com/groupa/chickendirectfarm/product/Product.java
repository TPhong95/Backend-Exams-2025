package com.groupa.chickendirectfarm.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
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
@JsonPropertyOrder({"id", "breed", "description", "price", "quantity", "stockStatus", "purchaseBatches"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private int id;

    @Enumerated(EnumType.STRING)
    private Breed breed;
    private String description;
    private int price;
    private int quantity;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseBatch> purchaseBatches = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp DESC")
    private List<ProductEvent> productEvents = new ArrayList<>();

    public Product(Breed breed, String description, int price, int quantity) {
        this.breed = breed;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
}