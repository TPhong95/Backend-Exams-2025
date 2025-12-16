package com.groupa.chickendirectfarm.integrationtests;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.customerdtos.CustomerAddressCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.conflict.BatchRequiredInPurchaseException;
import com.groupa.chickendirectfarm.exception.conflict.PurchaseAlreadyHandledException;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.PurchaseOrchestrationService;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatchRepo;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventRepo;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class IT04_PurchaseIT extends BaseIntegrationTest{

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private PurchaseOrchestrationService purchaseOrchestrationService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseEventRepo purchaseEventRepo;
    @Autowired
    private PurchaseBatchRepo purchaseBatchRepo;
    @Autowired
    EntityManager entityManager;

    @Test
    void shouldSaveAndRetrievePurchase(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomer = customerService.save(customer);

        Product product = new Product(Breed.BROWN, "This is the brownest chickens", 200, 40);
        Product savedProduct = productService.save(product);
        Product product2 = new Product(Breed.GOLDEN, "It's actually yellow chicken painted gold", 700, 500);
        Product savedProduct2 = productService.save(product2);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, savedProduct.getId());
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(10, savedProduct2.getId());
        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        purchaseBatches.add(batch1);
        purchaseBatches.add(batch2);

        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        Purchase savedPurchase = purchaseOrchestrationService.create(purchase);

        assertThat(purchaseService.getPurchaseById(savedPurchase.getId())).isNotNull();
        assertThat(purchaseService.getAllPurchases().size()).isEqualTo(1);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseBatches().size()).isEqualTo(2);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseBatches().getFirst().getProduct().getBreed()).isEqualTo(Breed.BROWN);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getCustomer().getId()).isEqualTo(savedCustomer.getId());
        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(30);
    }

    @Test
    void shouldDeletePurchaseAndGivePurchaseNotFoundExceptionWhenGettingAndDeletingAndCanceling(){
        assertThat(purchaseService.getAllPurchases().size()).isEqualTo(0);
        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.getPurchaseById(purchaseService.getAllPurchases().size()));
        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.deletePurchaseById(purchaseService.getAllPurchases().size()));
        assertThrows(PurchaseNotFoundException.class, () -> purchaseOrchestrationService.cancelPurchaseById(purchaseService.getAllPurchases().size()));

        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomer = customerService.save(customer);

        Product product = new Product(Breed.BROWN, "This is the brownest chickens", 200, 40);
        Product savedProduct = productService.save(product);
        Product product2 = new Product(Breed.GOLDEN, "It's actually yellow chicken painted gold", 700, 500);
        Product savedProduct2 = productService.save(product2);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, savedProduct.getId());
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(10, savedProduct2.getId());
        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        purchaseBatches.add(batch1);
        purchaseBatches.add(batch2);

        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        Purchase savedPurchase = purchaseOrchestrationService.create(purchase);

        purchaseService.deletePurchaseById(savedPurchase.getId());

        assertThat(purchaseService.getAllPurchases().size()).isEqualTo(0);
        assertThat(customerService.getCustomerById(savedCustomer.getId()).getPurchases().size()).isEqualTo(0);
        assertThat(purchaseEventRepo.findAll().size()).isEqualTo(0);
        assertThat(purchaseBatchRepo.findAll().size()).isEqualTo(0);
    }

    @Test
    void shouldGiveBatchRequiredInPurchaseException(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        customerService.save(customer);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        assertThrows(BatchRequiredInPurchaseException.class, () -> purchaseOrchestrationService.create(purchase));
    }

    @Test
    void shouldCreateCanceledEventForPurchaseAndReturnProducts(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        customerService.save(customer);

        Product product = new Product(Breed.BROWN, "This is the brownest chickens", 200, 40);
        Product savedProduct = productService.save(product);
        Product product2 = new Product(Breed.GOLDEN, "It's actually yellow chicken painted gold", 700, 500);
        Product savedProduct2 = productService.save(product2);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);


        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, savedProduct.getId());
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(10, savedProduct2.getId());
        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        purchaseBatches.add(batch1);
        purchaseBatches.add(batch2);

        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        Purchase savedPurchase = purchaseOrchestrationService.create(purchase);

        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(30);
        assertThat(productService.getProductById(savedProduct2.getId()).getQuantity()).isEqualTo(490);

        entityManager.flush();
        entityManager.clear();

        purchaseOrchestrationService.cancelPurchaseById(savedPurchase.getId());
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseEvents().getLast().getShippedStatus()).isEqualTo(ShippedStatus.CANCELLED);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseBatches().size()).isEqualTo(2);
        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(40);
        assertThat(productService.getProductById(savedProduct2.getId()).getQuantity()).isEqualTo(500);

    }

    @Test
    void shouldUpdatePurchaseAndCreateEvent(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        customerService.save(customer);

        Product product = new Product(Breed.BROWN, "This is the brownest chickens", 200, 40);
        Product savedProduct = productService.save(product);
        Product product2 = new Product(Breed.GOLDEN, "It's actually yellow chicken painted gold", 700, 500);
        Product savedProduct2 = productService.save(product2);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);


        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, savedProduct.getId());
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(10, savedProduct2.getId());
        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        purchaseBatches.add(batch1);
        purchaseBatches.add(batch2);

        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        Purchase savedPurchase = purchaseOrchestrationService.create(purchase);

        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseEvents().size()).isEqualTo(1);

        entityManager.flush();
        entityManager.clear();

        purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.SHIPPED);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseEvents().getLast().getShippedStatus()).isEqualTo(ShippedStatus.SHIPPED);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseEvents().size()).isEqualTo(2);
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.SHIPPED));


        purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.DELIVERED);
        assertThat(purchaseService.getPurchaseById(savedPurchase.getId()).getPurchaseEvents().size()).isEqualTo(3);
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.SHIPPED));
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.NOT_SHIPPED));
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.CANCELLED));
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.updatePurchaseById(savedPurchase.getId(), ShippedStatus.DELIVERED));
        assertThrows(PurchaseAlreadyHandledException.class, () -> purchaseOrchestrationService.cancelPurchaseById(savedPurchase.getId()));

    }





}
