package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.badrequest.OutOfStockException;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.product.event.ProductEventService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ProductOrchestrationService {
    private final ProductService productService;
    private final ProductEventService productEventService;
    private final ProductRepo productRepo;

    public ProductOrchestrationService(ProductService productService, ProductEventService productEventService, ProductRepo productRepo) {
        this.productService = productService;
        this.productEventService = productEventService;
        this.productRepo = productRepo;
    }

    @Transactional
    public void decreaseStock(int productId, int quantity){
        log.info("ENTRY: Decreasing stock for product Id: {} by quantity of {}", productId, quantity);

        Product product = productService.getProductById(productId);

        var previousQuantity = product.getQuantity();
        log.debug("Product Id: {}, breed: {}, has quantity: {}", productId, product.getBreed(), previousQuantity);

        if (product.getQuantity() < quantity) {
            log.warn("Stock insufficient for product Id: {}, available quantity: {}, requested quantity: {}", productId,  product.getQuantity(), quantity);
            throw new OutOfStockException(
                    "Not enough stock!  Available: " + product.getQuantity() +
                            ", requested: " + quantity
            );
        }

        product.setQuantity(product.getQuantity() - quantity);

        if (product.getQuantity() < 100) {
            log.warn("Product with id: {} is low on stock, there's {} units remaining.", productId, product.getQuantity());
        }

        StockStatus newStatus;
        if( product.getQuantity() > 0){
            newStatus = StockStatus.IN_STOCK;
        } else {
            newStatus = StockStatus.OUT_OF_STOCK;
            log.warn("Product with id: {} is out of stock", productId);
        }

        updateProductQuantity(product);
        log.debug("Product stock updated, product Id: {}, has updated quantity: {}", productId, product.getQuantity());

        productEventService.save(newStatus, product, -quantity, previousQuantity, ProductEventAction.PURCHASE);
        log.info("EXIT: Product stock decreased successfully for product Id: {}, quantity: {} -> {}", productId, previousQuantity, product.getQuantity());
    }

    @Transactional
    public void increaseStock(int productId, int quantity, ProductEventAction productEventAction){
        log.info("ENTRY: Increasing stock for product Id: {} by quantity of {}, with action of {}", productId, quantity, productEventAction);

        Product product = productService.getProductById(productId);
        var previousQuantity = product.getQuantity();
        log.debug("Product Id: {}, breed: {}, has quantity: {}", productId, product.getBreed(), previousQuantity);

        product.setQuantity(product.getQuantity() + quantity);

        StockStatus newStatus;
        if( product.getQuantity() > 0){
            newStatus = StockStatus.IN_STOCK;
        } else  {
            newStatus = StockStatus.OUT_OF_STOCK;
        }
        updateProductQuantity(product);
        log.debug("Product stock updated, product Id: {}, has updated quantity: {}", productId, product.getQuantity());

        productEventService.save(newStatus, product, quantity, previousQuantity, productEventAction);
        log.info("EXIT: Product stock increased successfully for product Id: {}, quantity: {} -> {}", productId, previousQuantity, product.getQuantity());
    }

    private void updateProductQuantity(Product product) {
        log.debug("Updating product with id: {}, breed: {}, with a new quantity: {}.",
                product.getId(), product.getBreed(), product.getQuantity());

        Product updatedProduct = productRepo.save(product);
        log.debug("Product Id: {} updated successfully! New quantity: {}",
                updatedProduct.getId(), updatedProduct.getQuantity());

    }

}
