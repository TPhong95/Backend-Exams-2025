package com.groupa.chickendirectfarm.testdata;

import com.github.javafaker.Faker;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerRepo;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressRepo;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TestData {

    private final Faker faker = new Faker();
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final CustomerAddressRepo customerAddressRepo;

    private final CustomerService customerService;

    public TestData(CustomerRepo customerRepo, ProductRepo productRepo, CustomerAddressRepo customerAddressRepo, CustomerService customerService) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.customerAddressRepo = customerAddressRepo;
        this.customerService = customerService;
    }

    public void createTestData() {
        createCustomers();
        createAdresses();
        createProducts();
    }

    private void createProducts() {
        for (Breed breed : Breed.values()) {
            productRepo.save(new Product(
                    breed.toString(),
                    "The color of the chicken is " + breed.toString().toLowerCase() + ".",
                    new Random().nextInt(50, 200),
                    "IN_STOCK",
                    new Random().nextInt(100, 200)

            ));
        }
    }

    private void createAdresses() {
        for (int i = 0; i < 100; i++) {
            customerAddressRepo.save(new CustomerAddress(
                    faker.address().streetName(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().emailAddress(),
                    customerService.getCustomerById(new Random().nextInt(21))
            ));
        }
    }

    private void createCustomers() {
        for (int i = 0; i < 20; i++) {
            customerRepo.save(new Customer(
            faker.company().name()));
        }
    }

}
