package com.groupa.chickendirectfarm.address;

import com.groupa.chickendirectfarm.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Address {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1)
    @Id
    private Integer id;

    private String streetName;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
