package com.groupa.chickendirectfarm.purchaseevent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.groupa.chickendirectfarm.purchase.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PurchaseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_event_seq")
    @SequenceGenerator(name = "purchase_event_seq", sequenceName = "purchase_event_seq", allocationSize = 1)
    private int id;
    private LocalDateTime timestamp;
    private ShippedStatus shippedStatus;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @JsonIgnoreProperties("purchase_event")
    private Purchase purchase;

    public PurchaseEvent(LocalDateTime timeStamp, ShippedStatus shippedStatus, Purchase purchase) {
        this.timestamp = LocalDateTime.now();
        this.shippedStatus = shippedStatus;
        this.purchase = purchase;
    }
}
