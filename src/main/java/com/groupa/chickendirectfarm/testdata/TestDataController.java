package com.groupa.chickendirectfarm.testdata;

import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductOrchestrationService;
import com.groupa.chickendirectfarm.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestDataController {
    private final TestData testData;
    private final ProductOrchestrationService productOrchestrationService;
    private final ProductService productService;

    public TestDataController(TestData testData, ProductOrchestrationService productOrchestrationService, ProductService productService) {
        this.testData = testData;
        this.productOrchestrationService = productOrchestrationService;
        this.productService = productService;
    }

    @GetMapping("/init")
    public ResponseEntity<String> initTestData() {
        testData.createTestData();
        return ResponseEntity.ok("Chicken init BAWK!");
    }

    @PostMapping("/init/decrease/{id}")
    public ResponseEntity<Product> checkDecrease (@PathVariable  int id, @RequestBody int quantity){
       productOrchestrationService.decreaseStock(id, quantity);
       return ResponseEntity.ok(productService.getProductById(id));
    }


}
