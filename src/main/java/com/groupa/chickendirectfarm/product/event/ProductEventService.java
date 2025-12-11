package com.groupa.chickendirectfarm.product.event;

import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.StockStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ProductEventService {
    private final ProductEventRepo productEventRepo;

    public ProductEventService(ProductEventRepo productEventRepo) {
        this.productEventRepo = productEventRepo;
    }

    public ProductEvent save(StockStatus stockStatus, Product product, int incomingQuantity, int previousQuantity, ProductEventAction productEventAction) {
        log.info("Saving product event with action: {} to Product Id: {}", productEventAction, product.getId());
        log.debug("Product event details: Breed {}, previous quantity: {}, quantity change: {}, new quantity {}",
                product.getBreed(), previousQuantity, incomingQuantity, previousQuantity + incomingQuantity);

        ProductEvent productEvent = new ProductEvent(stockStatus, product, incomingQuantity, previousQuantity, productEventAction);
        productEvent.setNewQuantity(previousQuantity + incomingQuantity);

        ProductEvent savedEvent = productEventRepo.save(productEvent);
        log.info("Product event action: {} saved to Product Id: {}", productEventAction, savedEvent.getId());
        return savedEvent;
    }


}
