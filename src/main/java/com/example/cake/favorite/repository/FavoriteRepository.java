package com.example.cake.favorite.repository;

import com.example.cake.favorite.model.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    
    List<Favorite> findByUserId(String userId);
}