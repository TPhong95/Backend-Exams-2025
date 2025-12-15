package com.groupa.chickendirectfarm.unittests;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.conflict.DuplicateProductInPurchaseException;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.PurchaseOrchestrationService;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseOrchestration Unit Tests")
public class PurchaseOrchestrationUnitTests {

    @InjectMocks
    private PurchaseOrchestrationService purchaseOrchestrationService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private CustomerAddressService customerAddressService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private PurchaseEventService purchaseEventService;

    @Mock
    private ProductOrchestrationService productOrchestrationService;

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
        address1.setPurchases(new ArrayList<>());

        product1 = new Product();
        product1.setId(1);
        product1.setBreed(Breed.BLACK);
        product1.setPrice(100);
        product1.setQuantity(50);
        product1.setDescription("Test Product");

        purchase1 = new Purchase();
        purchase1.setId(1);
        purchase1.setCustomer(customer1);
        purchase1.setCustomerAddress(address1);
        purchase1.setShippingCharge(150);
        purchase1.setTotalQuantity(5);
        purchase1. setTotalPrice(650);
        purchase1. setPurchaseBatches(new ArrayList<>());
        purchase1. setPurchaseEvents(new ArrayList<>());
    }


    @Test
    @DisplayName("Should throw exception when duplicate products in purchase batch")
    void shouldThrowExceptionWhenDuplicateProductIdInBatch() {
        // Arrange
        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, 1);
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(5, 1);  // Duplicate!
        PurchaseCreateDto dto = new PurchaseCreateDto(1, 500, List.of(batch1, batch2));

        when(customerAddressService.getCustomerAddressById(1)).thenReturn(address1);
        when(customerService.getCustomerById(1)).thenReturn(customer1);

        // Act & Assert
        assertThatThrownBy(() -> purchaseOrchestrationService. create(dto))
                .isInstanceOf(DuplicateProductInPurchaseException.class)
                .hasMessageContaining("Duplicate product");

        // Verify - INGEN metoder kalles etter exception!     ✅
        verify(productService, never()).getProductById(anyInt());
        verify(productOrchestrationService, never()).decreaseStock(anyInt(), anyInt());
        verify(purchaseService, never()).save(any());
        verify(purchaseEventService, never()).save(any(), any());
    }



}
