package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.OutOfStockException;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.product.event.ProductEventService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductOrchestrationService {
    private final ProductService productService;
    private final ProductEventService productEventService;
    public ProductOrchestrationService(ProductService productService, ProductEventService productEventService) {
        this.productService = productService;
        this.productEventService = productEventService;
    }

    @Transactional
    public void decreaseStock(int productId, int quantity){
        Product product = productService.getProductById(productId);
        var previousQuantity = product.getQuantity();

        if (product. getQuantity() < quantity) {
            throw new OutOfStockException(
                    "Not enough stock!  Available: " + product.getQuantity() +
                            ", requested: " + quantity
            );
        }

        product.setQuantity(product.getQuantity() - quantity);

        StockStatus newStatus;
        if( product.getQuantity() > 0){
            newStatus = StockStatus.IN_STOCK;
        } else {
            newStatus = StockStatus.OUT_OF_STOCK;
        }

        productService.update(product);

        productEventService.save(newStatus, product, -quantity, previousQuantity, ProductEventAction.PURCHASE);
    }

    @Transactional
    public void increaseStock(int productId, int quantity, ProductEventAction productEventAction){
        Product product = productService.getProductById(productId);
        var previousQuantity = product.getQuantity();

        product.setQuantity(product.getQuantity() + quantity);

        StockStatus newStatus;
        if( product.getQuantity() > 0){
            newStatus = StockStatus.IN_STOCK;
        } else  {
            newStatus = StockStatus.OUT_OF_STOCK;
        }
        productService.update(product);
        productEventService.save(newStatus, product, quantity, previousQuantity, productEventAction);
    }

}
