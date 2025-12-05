package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        var result = productService.getProductById(id);
        if (result == null){throw new ProductNotFoundException("Product not found");}
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts(){
        var result = productService.getAllProducts();
        if (result == null){throw new ProductNotFoundException("Products not found");}
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product with id " + id + " was deleted");
    }



}
