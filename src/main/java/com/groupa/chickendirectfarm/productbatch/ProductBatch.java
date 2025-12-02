package com.groupa.chickendirectfarm.productbatch;


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
public class ProductBatch {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_batch_seq")
    @SequenceGenerator(name = "product_batch_seq", sequenceName = "product_batch_seq", allocationSize = 1)
    @Id
    private int id;
    private int quantity;
    private int totalPrice;

    @ManyToOne()
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("productBatches")
    private Product product;

    public ProductBatch(int quantity, int totalPrice, Purchase purchase, Product product) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchase = purchase;
        this.product = product;
    }
}
