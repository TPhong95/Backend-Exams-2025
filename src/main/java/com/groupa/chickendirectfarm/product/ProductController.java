package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.product.event.ProductEvent;
import com.groupa.chickendirectfarm.product.event.ProductEventService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ProductEventService productEventService;
    public ProductController(ProductService productService, ProductEventService productEventService) {
        this.productService = productService;
        this.productEventService = productEventService;
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
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




}
