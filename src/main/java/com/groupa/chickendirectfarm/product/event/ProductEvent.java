package com.groupa.chickendirectfarm.product.event;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.StockStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"id","stockStatus","previousQuantity","incomingQuantity", "newQuantity","productEventAction","timestamp"})

public class ProductEvent {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_event_seq")
    @SequenceGenerator(name = "product_event_seq", sequenceName = "product_event_seq", allocationSize = 1)

    @Id
    private int id;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @Enumerated(EnumType.STRING)
    private ProductEventAction productEventAction;

    private int previousQuantity;
    private int incomingQuantity;
    private int newQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductEvent(StockStatus stockStatus, Product product, int incomingQuantity, int previousQuantity, ProductEventAction productEventAction) {
        this.timestamp = LocalDateTime.now();
        this.stockStatus = stockStatus;
        this.product = product;
        this.incomingQuantity = incomingQuantity;
        this.previousQuantity = previousQuantity;
        this.productEventAction = productEventAction;
    }
}
