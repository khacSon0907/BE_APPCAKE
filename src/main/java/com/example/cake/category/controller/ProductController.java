package com.example.cake.category.controller;

import com.example.cake.category.dto.ProductCreateRequest;
import com.example.cake.category.dto.ProductUpdateRequest; // Thêm import này
import com.example.cake.category.dto.ProductDeleteRequest;
import com.example.cake.category.model.Product;
import com.example.cake.category.service.ProductService;
import com.example.cake.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    private final ProductService productService;

    @PutMapping("/update")
    public ResponseEntity<ResponseMessage<Product>> updateProduct(
            @RequestPart("request") String rawJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // để xử lý LocalDateTime

            ProductUpdateRequest request = mapper.readValue(rawJson, ProductUpdateRequest.class);

            System.out.println("okok "+  request);
            // Validate ID
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ResponseMessage<>(false, "ID sản phẩm không được để trống", null)
                );
            }

            ResponseMessage<Product> result = productService.updateProduct(request, imageFile);

            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (Exception e) {
            log.error("❌ Lỗi parse JSON ProductUpdateRequest", e);
            return ResponseEntity.badRequest().body(
                    new ResponseMessage<>(false, "Lỗi parse JSON: " + e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<String>> deleteProductById(@PathVariable String id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/test")
    public String Test(){
        return "Đây là product nè ";
    }

    @PostMapping("create")
    public ResponseEntity<ResponseMessage<Product>> createProduct(
            @RequestPart("request") String rawJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // nếu có trường ngày giờ

            ProductCreateRequest request = mapper.readValue(rawJson, ProductCreateRequest.class);
            return ResponseEntity.ok(productService.createProduct(request, imageFile));

        } catch (Exception e) {
            log.error("❌ Lỗi parse JSON ProductCreateRequest", e);
            return ResponseEntity.badRequest().body(new ResponseMessage<>(false, "Lỗi parse JSON", null));
        }
    }
}