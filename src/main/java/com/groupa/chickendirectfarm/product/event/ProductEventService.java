package com.groupa.chickendirectfarm.product.event;

import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.StockStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProductEvent> getEventByProductId(int productId) {
        log.debug("Getting product events by product Id: {}", productId);
        List<ProductEvent> productEvents = productEventRepo.findByProductId(productId);
        log.debug("Retrieved {} product events on product Id: {}", productEvents.size(), productId);
        return productEvents;
    }

    //Tror ikke vi trenger denne
    public ProductEvent getEventById(int id) {
        return productEventRepo.findById(id).orElseThrow();
    }

    //Tror ikke vi trenger denne
    public List<ProductEvent> getAllProductEvents() {
        return productEventRepo.findAll();
    }


}
