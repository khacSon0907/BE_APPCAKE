package com.example.cake.cart.repository;

import com.example.cake.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart,String> {


    Optional<Cart> findByUserId(String userId);
    boolean existsByUserId(String userId);


}
