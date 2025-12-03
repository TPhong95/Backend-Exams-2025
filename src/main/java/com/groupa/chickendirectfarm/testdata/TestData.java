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
import com.groupa.chickendirectfarm.product.StockStatus;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.PurchaseRepo;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatchRepo;
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
    private final PurchaseBatchRepo purchaseBatchRepo;

    private final CustomerService customerService;

    private Map<Breed, Product> testProducts = new HashMap<>();

    public TestData(CustomerRepo customerRepo, ProductRepo productRepo, CustomerAddressRepo customerAddressRepo, PurchaseRepo purchaseRepo, PurchaseBatchRepo purchaseBatchRepo, CustomerService customerService) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.customerAddressRepo = customerAddressRepo;
        this.purchaseRepo = purchaseRepo;
        this.purchaseBatchRepo = purchaseBatchRepo;
        this.customerService = customerService;
    }

    public void createTestData() {
        createCustomers();
        createAddresses();
        createProducts();
        createPurchases();
    }

    private List<PurchaseBatch> createPurchaseBatches(Purchase purchase) {
        List<PurchaseBatch> purchaseBatches = new ArrayList<>();

        List<Breed> breeds = new ArrayList<>(Arrays.asList(Breed.values()));
        Collections.shuffle(breeds, random);
        int howManyBreeds = random.nextInt(breeds.size()+1);
        List<Breed> randomBreeds = breeds.subList(0, Math.min(howManyBreeds, breeds.size()));

        for (Breed breed : randomBreeds) {
            Product product = testProducts.get(breed);
            int amountOfChickens = random.nextInt(11);

            PurchaseBatch chickenBatch = purchaseBatchRepo.save(new PurchaseBatch(
                    amountOfChickens,
                    amountOfChickens * product.getPrice(),
                    purchase,
                    product
            ));
            purchaseBatches.add(chickenBatch);
        }
        return purchaseBatches;
    }

    private void createPurchases() {
        for (int i = 0; i < 50; i++) {
            CustomerAddress customerAddress = customerAddressRepo.findById(random.nextInt(100)+1).orElseThrow();

            int shippingPrice = random.nextInt(200, 500) +1;

            Purchase purchase = new Purchase(
                    shippingPrice,
                    shippingPrice,
                    customerAddress.getCustomer(),
                    customerAddress,
                    null
            );
            purchase = purchaseRepo.save(purchase);

            List<PurchaseBatch> listOfChickenBatches = createPurchaseBatches(purchase);
            purchase.setPurchaseBatches(listOfChickenBatches);

            long totalPrice = purchase.getTotalPrice();

            for (PurchaseBatch ChickenBatch : listOfChickenBatches) {
                totalPrice += ChickenBatch.getTotalPrice();
            }
            purchase.setTotalPrice(totalPrice);

            purchaseRepo.save(purchase);
        }
    }


    private void createProducts() {
        for (Breed breed : Breed.values()) {
            Product product = productRepo.save(new Product(
                    breed.toString(),
                    "The color of the chicken is " + breed.toString().toLowerCase() + ".",
                    random.nextInt(50, 200) + 1,
                    StockStatus.IN_STOCK,
                    random.nextInt(500, 1000) + 1
            ));
            testProducts.put(breed, product);
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
            faker.company().name()));
        }
    }

}
