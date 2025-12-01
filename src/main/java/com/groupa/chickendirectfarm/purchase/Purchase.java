package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.address.Address;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.productbatch.ProductBatch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private  ShippedStatus shippedStatus;
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany (mappedBy = "purchase")
    private List<ProductBatch> ProductBatches;
}
