package com.cfs.Ecommerce.repo;

import com.cfs.Ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Product findByName(String name);
    List<Product> findByCategory(String category);

}
