package com.example.cake.category.service;


import com.example.cake.category.dto.ProductCreateRequest;
import com.example.cake.category.dto.ProductUpdateRequest;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseMessage<String> deleteProduct(String id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            productRepository.deleteById(id);
            return  new ResponseMessage<>(true ,"Xoá Thành công ", null);
        }
        return new ResponseMessage<>(false,"Xoá thất bại",null);
    }

    public ResponseMessage<Product> updateProduct(ProductUpdateRequest request, MultipartFile imageFile) {
        // Kiểm tra sản phẩm có tồn tại không
        Optional<Product> productOptional = productRepository.findById(request.getId());
        if (productOptional.isEmpty()) {
            return new ResponseMessage<>(false, "Không tìm thấy sản phẩm với ID: " + request.getId(), null);
        }

        Product product = productOptional.get();

        try {
            // Cập nhật thông tin sản phẩm
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                product.setName(request.getName().trim());
            }

            if (request.getDescription() != null) {
                product.setDescription(request.getDescription());
            }

            if (request.getPrice() != null) {
                product.setPrice(request.getPrice());
            }

            if (request.getCategoryCode() != null && !request.getCategoryCode().trim().isEmpty()) {
                product.setCategoryCode(request.getCategoryCode().trim());
            }

            if (request.getStock() != null) {
                product.setStock(request.getStock());
            }

            if (request.getIngredients() != null) {
                product.setIngredients(request.getIngredients());
            }

            if (request.getSize() != null && !request.getSize().trim().isEmpty()) {
                product.setSize(request.getSize().trim());
            }

            if (request.getWeight() != null) {
                product.setWeight(request.getWeight());
            }

            if (request.getIsAvailable() != null) {
                product.setIsAvailable(request.getIsAvailable());
            }

            // Xử lý upload ảnh mới nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleProductImageUpload(imageFile);
                if (imageUrl == null) {
                    return new ResponseMessage<>(false, "Ảnh không hợp lệ hoặc lỗi upload!", null);
                }
                product.setImages(imageUrl);
            }

            // Cập nhật thời gian sửa đổi
            product.setUpdatedAt(LocalDateTime.now());

            // Lưu sản phẩm đã cập nhật
            Product updatedProduct = productRepository.save(product);

            return new ResponseMessage<>(true, "Cập nhật sản phẩm thành công", updatedProduct);

        } catch (Exception e) {
            return new ResponseMessage<>(false, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), null);
        }
    }



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
