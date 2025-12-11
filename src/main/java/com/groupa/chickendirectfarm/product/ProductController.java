package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.dto.ProductEventResponseDto;
import com.groupa.chickendirectfarm.dto.ProductResponseDto;
import com.groupa.chickendirectfarm.dto.ProductRestockDto;
import com.groupa.chickendirectfarm.dtomappers.ProductDtoMapper;
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
    private final ProductDtoMapper productDtoMapper;

    public ProductController(ProductService productService, ProductEventService productEventService, ProductOrchestrationService productOrchestrationService, ProductDtoMapper productDtoMapper) {
        this.productService = productService;
        this.productEventService = productEventService;
        this.productOrchestrationService = productOrchestrationService;
        this.productDtoMapper = productDtoMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody Product product){
        Product result = productService.save(product);
        return ResponseEntity.ok(productDtoMapper.toProductDto(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id){
        var result = productService.getProductById(id);
        return ResponseEntity.ok(productDtoMapper.toProductDto(result));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
       List<Product> products = productService.getAllProducts();
       if (products.isEmpty()) {
           return ResponseEntity.notFound().build();
       }
       List<ProductResponseDto> dtos = products.stream()
               .map(productDtoMapper::toProductDto)
               .toList();

       return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product with id " + id + " was deleted");
    }


    @GetMapping("/event/{id}")
    public ResponseEntity<ProductEventResponseDto> getProductEventById(@PathVariable int id){
        ProductEvent result = productEventService.getEventById(id);
        return ResponseEntity.ok(productDtoMapper.toProductEventDto(result));
    }

    @GetMapping("/event")
    public ResponseEntity<List<ProductEventResponseDto>> getAllProductEvents(){
        List<ProductEvent> events = productEventService.getAllProductEvents();
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductEventResponseDto> dtos = events.stream()
                .map(productDtoMapper::toProductEventDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    //Må fikse responseEntity her
    @PostMapping("/restock")
    public ResponseEntity<Product> restockProduct(@RequestBody ProductRestockDto productRestockDto){
        productOrchestrationService.increaseStock(productRestockDto.productId(), productRestockDto.quantity(), ProductEventAction.RESTOCK);
        return ResponseEntity.ok(productService.getProductById(productRestockDto.productId()));

    }

}
