package com.groupa.chickendirectfarm.product.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ProductEvent {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_event_seq")
    @SequenceGenerator(name = "product_event_seq", sequenceName = "product_event_seq", allocationSize = 1)


    @Id
    private int id;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties("productEvents")
    private Product product;

    public ProductEvent(StockStatus stockStatus, Product product) {
        this.timestamp = LocalDateTime.now();
        this.stockStatus = stockStatus;
        this.product = product;
    }

}
