package com.groupa.chickendirectfarm.purchase.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"id", "purchase", "shippedStatus", "timestamp"})
public class PurchaseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_event_seq")
    @SequenceGenerator(name = "purchase_event_seq", sequenceName = "purchase_event_seq", allocationSize = 1)
    private int id;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ShippedStatus shippedStatus;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public PurchaseEvent(ShippedStatus shippedStatus, Purchase purchase) {
        this.timestamp = LocalDateTime.now();
        this.shippedStatus = shippedStatus;
        this.purchase = purchase;
    }
}
