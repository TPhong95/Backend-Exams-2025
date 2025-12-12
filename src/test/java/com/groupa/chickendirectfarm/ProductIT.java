package com.groupa.chickendirectfarm;

import com.groupa.chickendirectfarm.integrationtests.BaseIntegrationTest;
import com.groupa.chickendirectfarm.product.*;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIT extends BaseIntegrationTest {

    @Autowired
    private ProductOrchestrationService productOrchestrationService;
    @Autowired
    private ProductService productService;


    private final Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);

    @Test
    @Order(1)
    void shouldSaveAndRetrieveProduct() {
        Product savedProduct = productService.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
        assertThat(savedProduct.getDescription()).isEqualTo("The chosen one");
        assertThat(savedProduct.getPrice()).isEqualTo(100);
        assertThat(savedProduct.getQuantity()).isEqualTo(20);
    }

    @Test
    @Order(2)
    void shouldIncreaseStocksInProduct() {
        Product savedProduct = productService.save(product);
        productOrchestrationService.increaseStock(savedProduct.getId(), 10, ProductEventAction.RESTOCK);
        assertEquals(30, productService.getProductById(savedProduct.getId()).getQuantity(), "Should return 30");
    }

    @Test
    @Order(3)
    void shouldDecreaseStocksInProduct() {
        Product savedProduct = productService.save(product);
        productOrchestrationService.decreaseStock(savedProduct.getId(), 10);
        assertEquals(10, productService.getProductById(savedProduct.getId()).getQuantity(), "Should return 10");
    }
}
