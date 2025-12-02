package com.groupa.chickendirectfarm.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
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
public class Customer {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    @Id
    private int id;

    private String name;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("customer")
    private List<CustomerAddress> customerAddresses;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Purchase> purchases;

    public Customer(String name) {
        this.name = name;
    }
}
