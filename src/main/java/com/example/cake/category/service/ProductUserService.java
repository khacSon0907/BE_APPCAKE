package com.example.cake.category.service;


import com.example.cake.category.model.Product;
import com.example.cake.category.repository.ProductRepository;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductUserService {

    private  final ProductRepository repository;
    public ResponseMessage<List<Product>>  getAllProduct (){
        List<Product> productList = repository.findByIsAvailableTrue();
        return new ResponseMessage<>(true, "Danh sách sản phẩm khả dụng", productList);
    }

    public  ResponseMessage<Optional<Product>> getProductById (String productId){

        Optional<Product> productOptional = repository.findById(productId);
        if(productOptional.isEmpty()){
            return new ResponseMessage<>(false,"Sản phẩm không tồn tại ", null);
        }

        return  new ResponseMessage<>(true,"Sản phẩm product " , productOptional);

    }
}
