package com.groupa.chickendirectfarm.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String breed;
    private String description;
    private int price;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    private int quantity;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    private List<PurchaseBatch> purchaseBatches;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties({"product"})
    private List<ProductEvent> productEvents;

    public Product(String breed, String description, int price, StockStatus stockStatus, int quantity) {
        this.breed = breed;
        this.description = description;
        this.price = price;
        this.stockStatus = stockStatus;
        this.quantity = quantity;
    }
}