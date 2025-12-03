package com.groupa.chickendirectfarm.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_seq")
    @SequenceGenerator(name = "purchase_seq", sequenceName = "purchase_seq", allocationSize = 1)
    private int id;
    private int shippingCharge;
    private long totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("purchase")
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "customer_address_id")
    @JsonIgnoreProperties("purchase")
    private CustomerAddress customerAddress;

    @OneToMany (mappedBy = "purchase")
    @JsonIgnoreProperties("purchase")
    private List<PurchaseBatch> purchaseBatches;


    @JsonIgnoreProperties("purchase")
    @OneToMany(mappedBy = "purchase")
    private List<PurchaseEvent> purchaseEvents;


    public Purchase(int shippingCharge, long totalPrice, Customer customer, CustomerAddress customerAddress, List<PurchaseBatch> purchaseBatches) {
        this.shippingCharge = shippingCharge;
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.customerAddress = customerAddress;
        this.purchaseBatches = purchaseBatches;
    }
}
