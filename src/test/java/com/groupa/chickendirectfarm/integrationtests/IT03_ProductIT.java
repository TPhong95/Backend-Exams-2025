package com.groupa.chickendirectfarm.integrationtests;

import com.groupa.chickendirectfarm.exception.badrequest.OutOfStockException;
import com.groupa.chickendirectfarm.exception.conflict.ProductAlreadyExistsException;
import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import com.groupa.chickendirectfarm.product.*;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.product.event.ProductEventRepo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class IT03_ProductIT extends BaseIntegrationTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private ProductOrchestrationService productOrchestrationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductEventRepo productEventRepo;


    @Test
    void shouldSaveAndRetrieveProduct() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProductWithId = productService.save(product);
        Product savedProduct = productService.getProductById(savedProductWithId.getId());

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

        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(30);
        assertThat(productService.getProductById(savedProduct.getId()).getBreed()).isEqualTo(Breed.BROWN);
        assertThat(productEventRepo.findAll().getFirst().getProductEventAction()).isEqualTo(ProductEventAction.RESTOCK);
        assertThat(productEventRepo.findAll().getFirst().getIncomingQuantity()).isEqualTo(10);
        assertThat(productEventRepo.findAll().getFirst().getPreviousQuantity()).isEqualTo(20);
        assertThat(productEventRepo.findAll().getFirst().getNewQuantity()).isEqualTo(30);
    }

    @Test
    void shouldDecreaseStocksInProduct() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 200);
        Product savedProduct = productService.save(product);
        productOrchestrationService.decreaseStock(savedProduct.getId(), 10);

        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(190);
        assertThat(productService.getProductById(savedProduct.getId()).getBreed()).isEqualTo(Breed.BROWN);
        assertThat(productEventRepo.findAll().getFirst().getProductEventAction()).isEqualTo(ProductEventAction.PURCHASE);
        assertThat(productEventRepo.findAll().getFirst().getIncomingQuantity()).isEqualTo(-10);
        assertThat(productEventRepo.findAll().getFirst().getPreviousQuantity()).isEqualTo(200);
        assertThat(productEventRepo.findAll().getFirst().getNewQuantity()).isEqualTo(190);
    }

    @Test
    void shouldGiveOutOfStockException() {
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);

        assertThrows(OutOfStockException.class, () -> productOrchestrationService.decreaseStock(savedProduct.getId(), 50));
        assertThat(productService.getProductById(savedProduct.getId()).getBreed()).isEqualTo(Breed.BROWN);
    }

    @Test
    void shouldChangeProductToOutOfStock(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        productOrchestrationService.decreaseStock(savedProduct.getId(), 20);

        assertThat(productService.getProductById(savedProduct.getId()).getBreed()).isEqualTo(Breed.BROWN);

        ProductEvent event = productEventRepo.findAll().getFirst();

        assertThat(event.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        assertThat(savedProduct.getId()).isEqualTo(event.getProduct().getId());
    }

    @Test
    void shouldGiveOutOfStockWhenIncreasingStockUnderZero(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, -10);
        Product savedProduct = productService.save(product);
        productOrchestrationService.increaseStock(savedProduct.getId(), 5, ProductEventAction.RESTOCK);

        assertThat(productService.getProductById(savedProduct.getId()).getQuantity()).isEqualTo(-5);
        assertThat(productService.getProductById(savedProduct.getId()).getBreed()).isEqualTo(Breed.BROWN);

        ProductEvent event = productEventRepo.findByProductId(savedProduct.getId()).getFirst();

        assertThat(event.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        assertThat(savedProduct.getId()).isEqualTo(event.getProduct().getId());

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

    @Test
    void shouldGiveEmptyEventListAfterDeletingProduct(){
        Product product = new Product(Breed.BROWN, "The chosen one", 100, 20);
        Product savedProduct = productService.save(product);
        productOrchestrationService.increaseStock(savedProduct.getId(), 10, ProductEventAction.RESTOCK);

        entityManager.flush();
        entityManager.clear();

        assertThat(productEventRepo.findAll().size()).isEqualTo(1);
        productService.deleteProductById(savedProduct.getId());
        assertThat(productEventRepo.findAll().size()).isEqualTo(0);

    }
}
