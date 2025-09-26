package com.cfs.Ecommerce.controller;


import com.cfs.Ecommerce.model.Product;
import com.cfs.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable  Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/name/{name}")
    public Product getProductByName(@PathVariable String name){
        return productService.getProductByName(name);
    }

    @PostMapping("/add-products")
    public Product addProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }

}
