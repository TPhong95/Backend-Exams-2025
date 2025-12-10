package com.groupa.chickendirectfarm.customer.address;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.groupa.chickendirectfarm.customer.Customer;
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
@JsonPropertyOrder({"id", "streetName", "phone", "email", "customer", "customerAddress"})
public class CustomerAddress {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_address_seq")
    @SequenceGenerator(name = "customer_address_seq", sequenceName = "customer_address_seq", allocationSize = 1)
    @Id
    private int id;
    private String streetName;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "customerAddress")
    private List<Purchase> purchases = new ArrayList<>();

    public CustomerAddress(String streetName, String phone, String email, Customer customer) {
        this.streetName = streetName;
        this.phone = phone;
        this.email = email;
        this.customer = customer;
    }
}
