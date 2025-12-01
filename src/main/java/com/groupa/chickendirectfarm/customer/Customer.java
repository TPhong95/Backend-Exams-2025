package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.address.Address;
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
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;
}
