package com.example.cake.category.service;


import com.example.cake.category.model.Product;
import com.example.cake.category.repository.ProductRepository;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductUserService {

    private  final ProductRepository repository;
    public ResponseMessage<List<Product>>  getAllProduct (){
        List<Product> productList = repository.findByIsAvailableTrue();
        return new ResponseMessage<>(true, "Danh sách sản phẩm khả dụng", productList);
    }
}
