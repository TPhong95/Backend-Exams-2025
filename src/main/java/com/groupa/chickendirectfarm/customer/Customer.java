package com.groupa.chickendirectfarm.customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.purchase.Purchase;
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
@JsonPropertyOrder({"id", "name", "primaryPhone", "primaryEmail", "customerAddresses"})
public class Customer {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    @Id
    private int id;
    private String name;
    private String primaryPhone;
    private String primaryEmail;

    @OneToMany(mappedBy = "customer")
    private List<CustomerAddress> customerAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Purchase> purchases = new ArrayList<>();

    public Customer(String name, String primaryPhone, String primaryEmail) {
        this.name = name;
        this.primaryPhone = primaryPhone;
        this.primaryEmail = primaryEmail;
    }
}
