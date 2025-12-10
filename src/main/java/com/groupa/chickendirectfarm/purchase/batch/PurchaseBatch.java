package com.groupa.chickendirectfarm.purchase.batch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.purchase.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "product", "quantity", "batchPrice", "purchase"})
public class PurchaseBatch {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_batch_seq")
    @SequenceGenerator(name = "purchase_batch_seq", sequenceName = "purchase_batch_seq", allocationSize = 1)
    @Id
    private int id;
    private int quantity;
    private int batchPrice;

    @ManyToOne()
    @JoinColumn(name = "purchase_id")
    @JsonIgnore
    private Purchase purchase;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"purchaseBatches", "stockStatus", "description", "quantity",})
    private Product product;

    public PurchaseBatch(int quantity, int batchPrice, Purchase purchase, Product product) {
        this.quantity = quantity;
        this.batchPrice = batchPrice;
        this.purchase = purchase;
        this.product = product;
    }
}