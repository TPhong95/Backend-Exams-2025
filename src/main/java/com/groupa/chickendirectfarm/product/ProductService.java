package com.groupa.chickendirectfarm.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product save(Product product){
        return productRepo.save(product);
    }

    public Product getProductById(int id){
        return productRepo.findById(id).orElseThrow();
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public void deleteProductById(int id){
        productRepo.deleteById(id);
    }
}
