package com.groupa.chickendirectfarm.unittests;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseBatchCreateDto;
import com.groupa.chickendirectfarm.dto.purchasedtos.PurchaseCreateDto;
import com.groupa.chickendirectfarm.exception.conflict.BatchRequiredInPurchaseException;
import com.groupa.chickendirectfarm.exception.conflict.DuplicateProductInPurchaseException;
import com.groupa.chickendirectfarm.exception.conflict.PurchaseAlreadyHandledException;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.PurchaseOrchestrationService;
import com.groupa.chickendirectfarm.purchase.PurchaseService;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEvent;
import com.groupa.chickendirectfarm.purchase.event.PurchaseEventService;
import com.groupa.chickendirectfarm.purchase.event.ShippedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private PurchaseBatch purchaseBatch1;
    private PurchaseEvent purchaseEvent1;

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

        purchaseBatch1 = new PurchaseBatch();
        purchaseBatch1.setId(1);
        purchaseBatch1.setProduct(product1);
        purchaseBatch1.setQuantity(5);
        purchaseBatch1.setBatchPrice(500);
        purchaseBatch1.setPurchase(purchase1);

        purchaseEvent1 = new PurchaseEvent();
        purchaseEvent1.setId(1);
        purchaseEvent1.setPurchase(purchase1);
    }




    @Test
    @DisplayName("Should throw exception when duplicate products in purchase batch")
    void shouldThrowExceptionWhenDuplicateProductIdInBatch() {
        PurchaseBatchCreateDto batch1 = new PurchaseBatchCreateDto(10, 1);
        PurchaseBatchCreateDto batch2 = new PurchaseBatchCreateDto(5, 1);
        PurchaseCreateDto dto = new PurchaseCreateDto(1, 500, List.of(batch1, batch2));

        when(customerAddressService.getCustomerAddressById(1)).thenReturn(address1);
        when(customerService.getCustomerById(1)).thenReturn(customer1);

        assertThatThrownBy(() -> purchaseOrchestrationService. create(dto))
                .isInstanceOf(DuplicateProductInPurchaseException.class)
                .hasMessageContaining("Duplicate product");


        verify(productService, never()).getProductById(anyInt());
        verify(productOrchestrationService, never()).decreaseStock(anyInt(), anyInt());
        verify(purchaseService, never()).save(any());
        verify(purchaseEventService, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when no batches provided")
    void shouldThrowExceptionWhenNoBatchesProvided() {
        PurchaseCreateDto dto = new PurchaseCreateDto(1, 150, List.of());

        when(customerAddressService.getCustomerAddressById(1)).thenReturn(address1);
        when(customerService.getCustomerById(1)).thenReturn(customer1);

        assertThatThrownBy(() -> purchaseOrchestrationService.create(dto))
                .isInstanceOf(BatchRequiredInPurchaseException.class)
                .hasMessageContaining("No batches provided");


        verify(productService, never()).getProductById(anyInt());
        verify(productOrchestrationService, never()).decreaseStock(anyInt(), anyInt());
        verify(purchaseService, never()).save(any());
        verify(purchaseEventService, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should calculate total price correctly")
    void shouldCalculateTotalPriceCorrectly() {
        PurchaseBatchCreateDto batch = new PurchaseBatchCreateDto(5, 1);
        PurchaseCreateDto dto = new PurchaseCreateDto(1, 150, List.of(batch));

        when(customerAddressService.getCustomerAddressById(1)).thenReturn(address1);
        when(customerService.getCustomerById(1)).thenReturn(customer1);
        when(productService.getProductById(1)).thenReturn(product1);
        when(purchaseService.save(any(Purchase.class))).thenReturn(purchase1);

        Purchase result = purchaseOrchestrationService.create(dto);

        assertThat(result.getTotalPrice()).isEqualTo(650);
        assertThat(result.getTotalQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should throw exception when canceling already delivered purchase")
    void shouldThrowExceptionWhenCancelingDeliveredPurchase() {
        purchaseEvent1.setShippedStatus(ShippedStatus.DELIVERED);
        purchase1.setPurchaseEvents(List.of(purchaseEvent1));
        purchase1.setPurchaseBatches(List.of(purchaseBatch1));

        when(purchaseService.getPurchaseById(1)).thenReturn(purchase1);

        assertThatThrownBy(() -> purchaseOrchestrationService.cancelPurchaseById(1))
                .isInstanceOf(PurchaseAlreadyHandledException.class)
                .hasMessageContaining("cannot be canceled since it's already delivered");

        verify(productOrchestrationService, never()).increaseStock(anyInt(), anyInt(), any());
        verify(purchaseEventService, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when canceling already shipped purchase")
    void shouldThrowExceptionWhenCancelingShippedPurchase() {
        purchaseEvent1.setShippedStatus(ShippedStatus.SHIPPED);
        purchase1.setPurchaseEvents(List.of(purchaseEvent1));
        purchase1.setPurchaseBatches(List.of(purchaseBatch1));

        when(purchaseService.getPurchaseById(1)).thenReturn(purchase1);

        assertThatThrownBy(() -> purchaseOrchestrationService.cancelPurchaseById(1))
                .isInstanceOf(PurchaseAlreadyHandledException.class)
                .hasMessageContaining("cannot be canceled since it's already shipped");

        verify(productOrchestrationService, never()).increaseStock(anyInt(), anyInt(), any());
        verify(purchaseEventService, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when canceling already cancelled purchase")
    void shouldThrowExceptionWhenCancelingAlreadyCancelledPurchase() {
        purchaseEvent1.setShippedStatus(ShippedStatus.CANCELLED);
        purchase1.setPurchaseEvents(List.of(purchaseEvent1));
        purchase1.setPurchaseBatches(List.of(purchaseBatch1));

        when(purchaseService.getPurchaseById(1)).thenReturn(purchase1);

        assertThatThrownBy(() -> purchaseOrchestrationService.cancelPurchaseById(1))
                .isInstanceOf(PurchaseAlreadyHandledException.class)
                .hasMessageContaining("cannot be canceled since it's already cancelled");

        verify(productOrchestrationService, never()).increaseStock(anyInt(), anyInt(), any());
        verify(purchaseEventService, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should successfully cancel purchase with NOT_SHIPPED status")
    void shouldSuccessfullyCancelNotShippedPurchase() {
        purchaseEvent1.setShippedStatus(ShippedStatus.NOT_SHIPPED);
        purchase1.setPurchaseEvents(List.of(purchaseEvent1));
        purchase1.setPurchaseBatches(List.of(purchaseBatch1));

        when(purchaseService.getPurchaseById(1)).thenReturn(purchase1);

        Purchase result = purchaseOrchestrationService.cancelPurchaseById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);


        verify(productOrchestrationService, times(1))
                .increaseStock(product1.getId(), purchaseBatch1.getQuantity(), ProductEventAction.CANCELED);


        verify(purchaseEventService, times(1))
                .save(ShippedStatus.CANCELLED, purchase1);
    }



}
