package com.example.cake.category.controller;


import com.example.cake.category.dto.CategoryCreateRequest;
import com.example.cake.category.dto.CategoryDelete;
import com.example.cake.category.dto.CategoryUpdate;
import com.example.cake.category.model.Categories;
import com.example.cake.category.service.CategoryService;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.OpenOption;
import java.util.List;

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

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<String>> deleteCategory (@RequestBody CategoryDelete categoryDelete){
        return  ResponseEntity.ok(categoryService.deleteCategory(categoryDelete));
    }

}
