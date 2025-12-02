package com.groupa.chickendirectfarm.purchaseevent;

import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.ShippedStatus;
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
    private LocalDateTime purchaseDate;
    private ShippedStatus shippedStatus;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
}
