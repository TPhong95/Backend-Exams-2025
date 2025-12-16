package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.dto.productdtos.ProductResponseDto;
import com.groupa.chickendirectfarm.dto.productdtos.ProductRestockDto;
import com.groupa.chickendirectfarm.mapper.DtoMapper;
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
    private final DtoMapper dtoMapper;

    public ProductController(ProductService productService, ProductEventService productEventService, ProductOrchestrationService productOrchestrationService, DtoMapper dtoMapper) {
        this.productService = productService;
        this.productEventService = productEventService;
        this.productOrchestrationService = productOrchestrationService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody Product product){
        Product result = productService.save(product);
        return ResponseEntity.ok(dtoMapper.toProductDto(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id){
        var result = productService.getProductById(id);
        return ResponseEntity.ok(dtoMapper.toProductDto(result));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
       List<Product> products = productService.getAllProducts();
       if (products.isEmpty()) {
           return ResponseEntity.notFound().build();
       }
       List<ProductResponseDto> dtos = products.stream()
               .map(dtoMapper::toProductDto)
               .toList();

       return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product with id " + id + " was deleted");
    }

    @PostMapping("/restock")
    public ResponseEntity<ProductResponseDto> restockProduct(@RequestBody ProductRestockDto productRestockDto){
        productOrchestrationService.increaseStock(
                productRestockDto. productId(),
                productRestockDto.quantity(),
                ProductEventAction.RESTOCK
        );
        Product result = productService.getProductById(productRestockDto.productId());
        return ResponseEntity.ok(dtoMapper.toProductDto(result));
    }

}
