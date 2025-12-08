package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.product.event.ProductEventAction;
import com.groupa.chickendirectfarm.product.event.ProductEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ProductEventService productEventService;
    private final ProductOrchestrationService productOrchestrationService;

    public ProductController(ProductService productService, ProductEventService productEventService, ProductOrchestrationService productOrchestrationService) {
        this.productService = productService;
        this.productEventService = productEventService;
        this.productOrchestrationService = productOrchestrationService;
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        var result = productService.getProductById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts(){
        var result = productService.getAllProducts();
        if (result.isEmpty()) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product with id " + id + " was deleted");
    }


    @GetMapping("/event/{id}")
    public ResponseEntity<ProductEvent> getProductEventById(@PathVariable int id){
        return ResponseEntity.ok(productEventService.getEventById(id));
    }

    @GetMapping("/event")
    public ResponseEntity<List<ProductEvent>> getAllProductEvents(){
        return ResponseEntity.ok(productEventService.getAllProductEvents());
    }

    @PostMapping("/restock")
    public ResponseEntity<Product> restockProduct(@RequestBody ProductDto productDto){
        productOrchestrationService.increaseStock(productDto.productId(), productDto.quantity(), ProductEventAction.RESTOCK);
        return ResponseEntity.ok(productService.getProductById(productDto.productId()));

    }

}
