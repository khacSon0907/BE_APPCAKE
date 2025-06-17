package com.example.cake.cart.repository;

import com.example.cake.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart,String> {

}
