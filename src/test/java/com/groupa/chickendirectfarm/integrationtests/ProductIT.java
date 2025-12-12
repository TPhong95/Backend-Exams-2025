package com.groupa.chickendirectfarm.integrationtests;

import com.groupa.chickendirectfarm.exception.badrequest.OutOfStockException;
import com.groupa.chickendirectfarm.exception.conflict.ProductAlreadyExistsException;
import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import com.groupa.chickendirectfarm.product.*;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.product.event.ProductEventRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ProductIT extends BaseIntegrationTest {

    @Autowired
    private ProductOrchestrationService productOrchestrationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductEventRepo productEventRepo;


    @Test
    void shouldSaveAndRetrieveProduct() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
        assertThat(savedProduct.getDescription()).isEqualTo("The chosen one");
        assertThat(savedProduct.getPrice()).isEqualTo(100);
        assertThat(savedProduct.getQuantity()).isEqualTo(20);
    }

    @Test
    void shouldIncreaseStocksInProduct() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        productOrchestrationService.increaseStock(savedProduct.getId(), 10, ProductEventAction.RESTOCK);
        assertEquals(30, productService.getProductById(savedProduct.getId()).getQuantity(), "Should return 30");
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
    }

    @Test
    void shouldDecreaseStocksInProduct() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 200);
        Product savedProduct = productService.save(product);
        productOrchestrationService.decreaseStock(savedProduct.getId(), 10);
        assertEquals(190, productService.getProductById(savedProduct.getId()).getQuantity(), "Should return 190");
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
    }

    @Test
    void shouldGiveOutOfStockException() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        assertThrows(OutOfStockException.class, () -> productOrchestrationService.decreaseStock(savedProduct.getId(), 50));
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
    }

    @Test
    void shouldChangeProductToOutOfStock(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        productOrchestrationService.decreaseStock(savedProduct.getId(), 20);
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
        ProductEvent event = productEventRepo.findByProductId(savedProduct.getId()).getFirst();
        assertThat(event.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        assertEquals(savedProduct.getId(), event.getProduct().getId());
    }

    @Test
    void shouldGiveOutOfStockWhenIncreasingStockUnderZero(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, -10);
        Product savedProduct = productService.save(product);
        productOrchestrationService.increaseStock(savedProduct.getId(), 5, ProductEventAction.RESTOCK);
        assertEquals(-5, productService.getProductById(savedProduct.getId()).getQuantity(), "Should return -5");
        assertThat(savedProduct.getBreed()).isEqualTo(Breed.BROWN);
        ProductEvent event = productEventRepo.findByProductId(savedProduct.getId()).getFirst();
        assertThat(event.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        assertEquals(savedProduct.getId(), event.getProduct().getId());

    }

    @Test
    void shouldGiveProductAlreadyExistException(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, -10);
        productService.save(product);
        assertThrows(ProductAlreadyExistsException.class, () -> productService.save(product));
        assertThat(productService.getAllProducts()).size().isEqualTo(1);
    }

    @Test
    void shouldGiveEmptyListAndGiveProductNotFoundExceptionOnDeletingProduct(){
        productService.getAllProducts();
        assertThat(productService.getAllProducts()).isEmpty();
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(productService.getAllProducts().size()));

    }

    @Test
    void shouldDeleteAndGiveProductNotFoundExceptionOnGetProductOnID(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, -10);
        Product savedProduct = productService.save(product);
        assertThat(productService.getProductById(savedProduct.getId())).isNotNull();
        productService.deleteProductById(savedProduct.getId());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(savedProduct.getId()));
        assertThat(productService.getAllProducts()).isEmpty();

    }
}
