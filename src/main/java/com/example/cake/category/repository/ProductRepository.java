package com.example.cake.category.repository;

import com.example.cake.category.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product,String> {

    List<Product> findByIsAvailableTrue();
    List<Product> findByCategoryCode(String categoryCode);
}
