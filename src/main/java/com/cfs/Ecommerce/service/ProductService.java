package com.cfs.Ecommerce.service;

import com.cfs.Ecommerce.model.Product;
import com.cfs.Ecommerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product getProductById(Long id){
        return productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product){
        return productRepo.save(product);
    }

    public Product getProductByName(String name){
        return productRepo.findByName(name);
    }

    public List<Product> getProductByCategory(String category){
        return productRepo.findByCategory(category);
    }

    public void deleteProduct(Long id){
        productRepo.deleteById(id);
    }


}
