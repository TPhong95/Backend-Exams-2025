package com.groupa.chickendirectfarm.unittests;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseMoreDetailsResponseDto;
import com.groupa.chickendirectfarm.mapper.DtoMapper;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("DtoMapper Unit Tests")
public class DtoUnitTests {

    @InjectMocks
    private DtoMapper dtoMapper;

    private Customer customer1;
    private CustomerAddress address1;
    private Product product1;
    private Purchase purchase1;

    @BeforeEach
    void setUp(){
        customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("Customer 1");
        customer1.setPrimaryPhone("12345678");
        customer1.setPrimaryEmail("customer1@gmail.com");
        customer1.setCustomerAddresses(new ArrayList<>());
        customer1.setPurchases(new ArrayList<>());

        address1 = new CustomerAddress();
        address1.setId(1);
        address1.setStreetName("Address 1");
        address1.setPhone("87654321");
        address1.setEmail("address email1");
        address1.setCustomer(customer1);
        customer1.setPurchases(new ArrayList<>());

        product1 = new Product();
        product1.setId(1);
        product1.setBreed(Breed.BLACK);
        product1.setPrice(10);
        product1.setQuantity(5);
        product1.setDescription("Test Product");

        purchase1 = new Purchase();
        purchase1.setId(1);
        purchase1.setCustomer(customer1);
        purchase1.setCustomerAddress(address1);
        purchase1.setShippingCharge(100);
        purchase1.setTotalQuantity(5);
        purchase1. setTotalPrice(150);
        purchase1. setPurchaseBatches(new ArrayList<>());
        purchase1. setPurchaseEvents(new ArrayList<>());
    }

    @Test
    @DisplayName("Should convert purchase to PurchaseDetailsRespnseDto with multiple events sorted descending")
    void shouldConvertToDetailsDtoWithEventsSortedDescending(){
        PurchaseEvent event1 = new PurchaseEvent(ShippedStatus.NOT_SHIPPED, purchase1);
        event1.setTimestamp(LocalDateTime.of(2025, 12, 12, 12, 0));

        PurchaseEvent event2 = new PurchaseEvent(ShippedStatus.SHIPPED, purchase1);
        event2.setTimestamp(LocalDateTime.of(2025, 12, 14, 12, 0));

        PurchaseEvent event3 = new PurchaseEvent(ShippedStatus.DELIVERED, purchase1);
        event3.setTimestamp(LocalDateTime.of(2025, 12, 16, 12, 0));

        purchase1.getPurchaseEvents().add(event2);
        purchase1.getPurchaseEvents().add(event3);
        purchase1.getPurchaseEvents().add(event1);

        PurchaseMoreDetailsResponseDto result = dtoMapper.toPurchaseMoreDetailsDto(purchase1);

        assertThat(result.statusHistory().size()).isEqualTo(3);
        assertThat(result.statusHistory().getFirst().status()).isEqualTo("DELIVERED");
        assertThat(result.statusHistory().get(1).status()).isEqualTo("SHIPPED");
        assertThat(result.statusHistory().getLast().status()).isEqualTo("NOT_SHIPPED");
    }

}
