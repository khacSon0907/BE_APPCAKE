package com.example.cake.category.controller;


import com.example.cake.category.model.Product;
import com.example.cake.category.service.ProductUserService;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductUserController {

    private final ProductUserService productUserService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<Product>>> getAllIsvailable (){
        return  ResponseEntity.ok(productUserService.getAllProduct());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Optional<Product>>> getProductbyId (@PathVariable String productId ){
        log.info(" vô chưa" ,productId);
        return  ResponseEntity.ok(productUserService.getProductById(productId));
    }

}
