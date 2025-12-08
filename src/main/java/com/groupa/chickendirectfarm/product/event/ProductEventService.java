package com.groupa.chickendirectfarm.product.event;

import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.StockStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductEventService {
    private final ProductEventRepo productEventRepo;

    public ProductEventService(ProductEventRepo productEventRepo) {

        this.productEventRepo = productEventRepo;
    }

public ProductEvent save(StockStatus stockStatus, Product product, int incomingQuantity, int previousQuantity, ProductEventAction productEventAction){
        ProductEvent productEvent = new ProductEvent(stockStatus, product, incomingQuantity, previousQuantity, productEventAction);
        productEvent.setNewQuantity(previousQuantity + incomingQuantity);
        return productEventRepo.save(productEvent);
}

public List<ProductEvent> getEventByProductId(int productId) {
        return productEventRepo.findByProductId(productId);
}

public ProductEvent getEventById(int id){
    return productEventRepo.findById(id).orElseThrow();
}

public List<ProductEvent> getAllProductEvents(){
        return productEventRepo.findAll();
}


}
