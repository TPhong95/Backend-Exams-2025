package com.groupa.chickendirectfarm.purchase.batch;
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
    private Purchase purchase;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

}