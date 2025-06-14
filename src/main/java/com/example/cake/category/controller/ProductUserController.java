package com.example.cake.category.controller;


import com.example.cake.category.model.Product;
import com.example.cake.category.service.ProductUserService;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductUserController {

    private final ProductUserService productUserService;


    @GetMapping
    public ResponseEntity<ResponseMessage<List<Product>>> getAllIsvailable (){

        return  ResponseEntity.ok(productUserService.getAllProduct());
    }

}
