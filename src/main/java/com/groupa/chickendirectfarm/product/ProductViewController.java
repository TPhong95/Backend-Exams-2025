package com.groupa.chickendirectfarm.product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public String listOfProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String viewProductDetails(Model model, @PathVariable int id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/view";
    }
}
