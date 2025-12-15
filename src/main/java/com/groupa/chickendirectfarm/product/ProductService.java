package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.conflict.ProductAlreadyExistsException;
import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepo productRepo;
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product save(Product product)
    {
        log.info("ENTRY: Creating new product with Breed: {}, price {}, Quantity {}",
                product.getBreed(), product.getPrice(), product.getQuantity());

        if (productRepo.existsByBreed(product.getBreed())){
            log.warn("Product with breed {} already exists", product.getBreed());

            throw new ProductAlreadyExistsException(
                    String.format("Product " +  product.getBreed() + " already exists!")
            );
        }
        Product savedProduct = productRepo.save(product);

        log.info("EXIT: Saving product with id: {} and breed {}",
                savedProduct.getId(),savedProduct.getBreed());

        return savedProduct;
    }

    public Product getProductById(int id){
        log.debug("Retrieving product with id: {}", id);
        return productRepo.findById(id).orElseThrow(() -> {
            log.warn("Product with id {} not found", id);
            return new ProductNotFoundException("Product with id " + id + " not found");
        });
    }

    public List<Product> getAllProducts(){
        log.debug("Retrieving all products...");
        List<Product> allProducts = productRepo.findAllByOrderByIdAsc();
        log.debug("Retrieved {} products", allProducts.size());
        return allProducts;
    }

    public void deleteProductById(int id){
        log.info("ENTRY: Deleting product with id: {}", id);

        if (!productRepo.existsById(id)){
            log.warn("Delete failed, Product with id {} not found", id);
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        Product product = getProductById(id);

        productRepo.delete(product);
        log.info("EXIT: Deleted product with id: {} successfully!", id);
    }

}
