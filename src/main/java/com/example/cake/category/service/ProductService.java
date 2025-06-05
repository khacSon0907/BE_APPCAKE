package com.example.cake.category.service;


import com.example.cake.category.dto.ProductCreateRequest;
import com.example.cake.category.model.Product;
import com.example.cake.category.repository.ProductRepository;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseMessage<List<Product>> getAllProduct(){
        List<Product> productList = productRepository.findAll();
        return  new ResponseMessage<>(true,"Danh sách sản phẩm ",productList );
    }

    public ResponseMessage<Product> createProduct(ProductCreateRequest request, MultipartFile imageFile) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .categoryCode(request.getCategoryCode())
                .stock(request.getStock())
                .ingredients(request.getIngredients())
                .size(request.getSize())
                .weight(request.getWeight())
                .isAvailable(request.getIsAvailable())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = handleProductImageUpload(imageFile);
            if (imageUrl == null) {
                return new ResponseMessage<>(false, "Ảnh không hợp lệ hoặc lỗi upload!", null);
            }
            product.setImages(imageUrl);
        }

        Product saved = productRepository.save(product);
        return new ResponseMessage<>(true, "Tạo sản phẩm thành công", saved);
    }

    private String handleProductImageUpload(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            List<String> allowedTypes = List.of(
                    "image/jpeg", "image/png", "image/webp", "image/jpg",
                    "image/gif", "image/bmp", "image/svg+xml", "image/x-icon", "image/heic"
            );

            if (contentType == null || !allowedTypes.contains(contentType)) {
                return null;
            }
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";

            String fileName = UUID.randomUUID() + extension;
            Path uploadDir = Paths.get("uploads/products");
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return "http://localhost:8080/static/products/" + fileName;

        } catch (IOException e) {
            return null;
        }
    }

}
