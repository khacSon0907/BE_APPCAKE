package com.example.cake.category.repository;


import com.example.cake.category.model.Categories;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Categories,String> {
    Optional<Categories> findCode(String code);
    boolean existsByCode(String code);

}
