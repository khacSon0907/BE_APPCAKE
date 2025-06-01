package com.example.cake.category.controller;


import com.example.cake.category.dto.CategoryCreateRequest;
import com.example.cake.category.dto.CategoryDelete;
import com.example.cake.category.dto.CategoryUpdate;
import com.example.cake.category.model.Categories;
import com.example.cake.category.service.CategoryService;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.OpenOption;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<Categories>> create(@RequestBody CategoryCreateRequest request)
    {
        System.out.println("zo roi ak " + request.getName());
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<List<Categories>>> getAll(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<Categories>> update(@RequestBody CategoryUpdate categoryUpdate){
        return ResponseEntity.ok(categoryService.updateCategory(categoryUpdate));
    }

        @DeleteMapping("/delete/{code}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseMessage<String>> deleteCategory (@PathVariable String code ){
            return  ResponseEntity.ok(categoryService.deleteCategory(code));
        }

}
