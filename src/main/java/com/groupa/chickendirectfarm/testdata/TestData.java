package com.groupa.chickendirectfarm.testdata;

import com.github.javafaker.Faker;
import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerRepo;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressRepo;
import com.groupa.chickendirectfarm.product.*;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.purchase.PurchaseOrchestrationService;
import com.groupa.chickendirectfarm.purchase.PurchaseRepo;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestData {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final CustomerAddressRepo customerAddressRepo;
    private final PurchaseRepo purchaseRepo;
    private final PurchaseOrchestrationService purchaseOrchestrationService;

    private final CustomerService customerService;
    private final ProductOrchestrationService productOrchestrationService;

    private Map<Breed, Product> testProducts = new HashMap<>();

    public TestData(CustomerRepo customerRepo, ProductRepo productRepo, CustomerAddressRepo customerAddressRepo, PurchaseRepo purchaseRepo, PurchaseOrchestrationService purchaseOrchestrationService, CustomerService customerService, ProductOrchestrationService productOrchestrationService) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.customerAddressRepo = customerAddressRepo;
        this.purchaseRepo = purchaseRepo;
        this.purchaseOrchestrationService = purchaseOrchestrationService;
        this.customerService = customerService;
        this.productOrchestrationService = productOrchestrationService;
    }

    public void createTestData() {
        createProducts();
        createHardCodedData();
        createCustomers();
        createAddresses();
        createTestPurchases();
    }

    private void createHardCodedData() {
        Customer customer = customerRepo.save(new Customer(
                "Jason",
                faker.phoneNumber().phoneNumber(),
                faker.internet().emailAddress()));

            CustomerAddress customerAddress = customerAddressRepo.save(new CustomerAddress(
                    "Jason's Hillybilly Chicken Farm",
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().emailAddress(),
                    customerService.getCustomerById(customer.getId())
            ));


            int shippingPrice = random.nextInt(200, 500) +1;

            PurchaseCreateDto purchaseCreateDto = new PurchaseCreateDto(
                    customerAddress.getId(),
                    shippingPrice,
                    createTestPurchaseBatches()
            );

            var testPurchase = purchaseOrchestrationService.create(purchaseCreateDto);
            purchaseRepo.save(testPurchase);

    }

    private List<PurchaseBatchCreateDto> createTestPurchaseBatches() {
        List<PurchaseBatchCreateDto> testPurchaseBatches = new ArrayList<>();

        List<Breed> breeds = new ArrayList<>(Arrays.asList(Breed.values()));
        Collections.shuffle(breeds, random);


        int howManyBreeds = random.nextInt(1, breeds.size()+1);
        List<Breed> randomBreeds = breeds.subList(0, Math.min(howManyBreeds, breeds.size()));

        for (Breed breed : randomBreeds) {
            Product product = testProducts.get(breed);
            int amountOfChickens = random.nextInt(1, 11);

            PurchaseBatchCreateDto chickenBatch = new PurchaseBatchCreateDto(
                    amountOfChickens,
                    product.getId()
            );
            testPurchaseBatches.add(chickenBatch);
        }
        return testPurchaseBatches;
    }

    private void createTestPurchases() {
        for (int i = 0; i < 50; i++) {
            CustomerAddress customerAddress = customerAddressRepo.findById(random.nextInt(100)+1).orElseThrow();
            int shippingPrice = random.nextInt(200, 500) +1;

            PurchaseCreateDto purchaseCreateDto = new PurchaseCreateDto(
                    customerAddress.getId(),
                    shippingPrice,
                    createTestPurchaseBatches()
            );

            var testPurchase = purchaseOrchestrationService.create(purchaseCreateDto);
            purchaseRepo.save(testPurchase);
        }
    }


    private void createProducts() {
        for (Breed breed : Breed.values()) {
            var quantity = random.nextInt(500, 1000) + 1;
            Product product = productRepo.save(new Product(
                    breed,
                    "The color of the chicken is " + breed.toString().toLowerCase() + ".",
                    random.nextInt(50, 200) + 1,
                    0
            ));
            testProducts.put(breed, product);
            productOrchestrationService.increaseStock(product.getId(), quantity, ProductEventAction.RESTOCK);
        }
    }

    private void createAddresses() {
        for (int i = 0; i < 100; i++) {
            customerAddressRepo.save(new CustomerAddress(
                    faker.address().streetName(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().emailAddress(),
                    customerService.getCustomerById(random.nextInt(20) + 1)
            ));
        }
    }

    private void createCustomers() {
        for (int i = 0; i < 20; i++) {
            customerRepo.save(new Customer(
            faker.company().name(),
            faker.phoneNumber().phoneNumber(),
            faker.internet().emailAddress()
            ));
        }
    }

}
