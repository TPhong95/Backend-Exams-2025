package com.groupa.chickendirectfarm.integrationtests;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.customerdtos.CustomerAddressCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.conflict.CustomerAlreadyExistException;
import com.groupa.chickendirectfarm.exception.conflict.CustomerHasPurchasesException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerAddressNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.PurchaseOrchestrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class IT02_CustomerIT extends BaseIntegrationTest {


    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PurchaseOrchestrationService purchaseOrchestrationService;

    @Test
    void shouldSaveAndRetrieveCustomer() {
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomerWithId = customerService.save(customer);
        Customer savedCustomer = customerService.getCustomerById(savedCustomerWithId.getId());
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer).isEqualTo(customer);
        assertThat(savedCustomer.getName()).isEqualTo("Bob Bob");
        assertThat(savedCustomer.getPrimaryPhone()).isEqualTo("808808");
        assertThat(savedCustomer.getPrimaryEmail()).isEqualTo("BobBob@ChickenDirect.com");
        assertThat(savedCustomer.getCustomerAddresses()).isEmpty();
    }

    @Test
    void shouldGiveCustomerAlreadyExistException(){
        Customer customer1 = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer customer2 = new Customer("Lucky", "777777", "BobBob@ChickenDirect.com");
        Customer customer3 = new Customer("John", "808808", "Hello@ChickenDirect.com");
        customerService.save(customer1);

        assertThat(customerService.getAllCustomers().size()).isEqualTo(1);
        assertThrows(CustomerAlreadyExistException.class, () -> customerService.save(customer1));
        assertThrows(CustomerAlreadyExistException.class, () -> customerService.save(customer2));
        assertThrows(CustomerAlreadyExistException.class, () -> customerService.save(customer3));
    }

    @Test
    void shouldDeleteCustomerAndGiveEmptyList(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomer = customerService.save(customer);

        assertThat(customerService.getAllCustomers().size()).isEqualTo(1);
        customerService.deleteCustomerById(savedCustomer.getId());
        assertThat(customerService.getAllCustomers().size()).isEqualTo(0);
    }

    @Test
    void shouldGiveCustomerNotFoundExceptionForGetCustomerByIdAndDeleteCustomer(){
        assertThat(customerService.getAllCustomers()).isEmpty();
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1));
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(1));
    }

    @Test
    void shouldSaveCustomerAddressAndRetrieveCustomerAddress(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomer = customerService.save(customer);
        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId())).isNotNull();
        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId()).getStreetName()).isEqualTo("Fancy street");
        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId()).getPhone()).isEqualTo("192949");
        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId()).getEmail()).isEqualTo("BobBurger@Burger.com");
        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId()).getCustomer()).isEqualTo(savedCustomer);
        assertThat(customerAddressService.getCustomerAddressById(savedCustomerAddress.getId()).getPurchases()).isEmpty();
    }

    @Test
    void shouldGiveEmptyListFromGetAllAndCustomerAddressNotfoundFromGetCustomerAddressById(){
        assertThat(customerAddressService.getAllCustomerAddresses().size()).isEqualTo(0);

        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.getCustomerAddressById(customerService.getAllCustomers().size()));
        assertThrows(CustomerAddressNotFoundException.class, () -> customerAddressService.deleteCustomerAddressById(customerService.getAllCustomers().size()));
    }

    @Test
    void shouldDeleteCustomer(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        customerService.save(customer);
        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        assertThat(customerAddressService.getAllCustomerAddresses().size()).isEqualTo(1);

        customerAddressService.deleteCustomerAddressById(savedCustomerAddress.getId());

        assertThat(customerAddressService.getAllCustomerAddresses().size()).isEqualTo(0);

    }



    @Test
    void shouldGiveCustomerHasPurchasesException(){
        Customer customer = new Customer("Bob Bob", "808808", "BobBob@ChickenDirect.com");
        Customer savedCustomer = customerService.save(customer);

        Product product = new Product(Breed.BROWN, "This is the brownest chickens", 200, 40);
        Product savedProduct = productService.save(product);

        CustomerAddressCreateDto customerAddress = new CustomerAddressCreateDto("Fancy street", "192949", "BobBurger@Burger.com", customer.getId());
        CustomerAddress savedCustomerAddress = customerAddressService.save(customerAddress);

        PurchaseBatchCreateDto purchaseBatch = new PurchaseBatchCreateDto(10, savedProduct.getId());
        List<PurchaseBatchCreateDto> purchaseBatches = new ArrayList<>();
        purchaseBatches.add(purchaseBatch);

        assertThat(customerService.getCustomerById(savedCustomer.getId()).getPurchases()).isEmpty();

        PurchaseCreateDto purchase = new PurchaseCreateDto(savedCustomerAddress.getId(), 100, purchaseBatches);
        purchaseOrchestrationService.create(purchase);

        assertThat(customerService.getCustomerById(savedCustomer.getId()).getPurchases().size()).isEqualTo(1);
        assertThrows(CustomerHasPurchasesException.class, () -> customerService.deleteCustomerById(savedCustomer.getId()));
    }

}
