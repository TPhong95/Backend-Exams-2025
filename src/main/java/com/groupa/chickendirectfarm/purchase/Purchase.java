package com.groupa.chickendirectfarm.purchase;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"id", "customer", "customerAddress", "purchaseBatches", "shippingCharge", "totalPrice"})
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_seq")
    @SequenceGenerator(name = "purchase_seq", sequenceName = "purchase_seq", allocationSize = 1)
    private int id;
    private int shippingCharge;
    private long totalPrice;
    private int totalQuantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "customer_address_id")
    private CustomerAddress customerAddress;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseBatch> purchaseBatches = new ArrayList<>();

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseEvent> purchaseEvents = new ArrayList<>();

}
