package com.groupa.chickendirectfarm.customerAddress;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.purchase.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class customerAddress {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_address_seq")
    @SequenceGenerator(name = "customer_address_seq", sequenceName = "customer_address_seq", allocationSize = 1)
    @Id
    private Integer id;

    private String streetName;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "customer_address")
    private List<Purchase> purchases;
}
