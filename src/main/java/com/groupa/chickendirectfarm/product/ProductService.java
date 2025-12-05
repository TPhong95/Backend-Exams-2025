package com.groupa.chickendirectfarm.product;

import com.groupa.chickendirectfarm.exception.alreadyexist.ProductAlreadyExistsException;
import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepo productRepo;
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product save(Product product)
    {
        if (productRepo.existsByBreed(product.getBreed())){
            throw new ProductAlreadyExistsException(
                    String.format("Product " +  product.getBreed() + " already exists!")
            );
        };
        return productRepo.save(product);
    }

    public Product getProductById(int id){
        return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public void deleteProductById(int id){
        if (!productRepo.existsById(id)){
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepo.deleteById(id);
    }

    public Product update(Product product) {
        return productRepo.save(product);
    }
}
