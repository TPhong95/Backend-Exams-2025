package com.groupa.chickendirectfarm.purchaseBatch;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PurchaseBatch {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_batch_seq")
    @SequenceGenerator(name = "purchase_batch_seq", sequenceName = "purchase_batch_seq", allocationSize = 1)
    @Id
    private int id;
    private int quantity;
    private int totalPrice;

    @ManyToOne()
    @JoinColumn(name = "purchase_id")
    @JsonIgnoreProperties("purchaseBatches")
    private Purchase purchase;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("purchaseBatches")
    private Product product;

    public PurchaseBatch(int quantity, int totalPrice, Purchase purchase, Product product) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchase = purchase;
        this.product = product;
    }
}
