package com.groupa.chickendirectfarm.purchase;

import com.groupa.chickendirectfarm.customerAddress.CustomerAddress;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.productbatch.ProductBatch;
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
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "customer_address_id")
    private CustomerAddress customerAddress;

    @OneToMany (mappedBy = "purchase")
    private List<ProductBatch> productBatches;
}
