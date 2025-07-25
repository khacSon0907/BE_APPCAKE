package com.example.cake.favorite.controller;

import com.example.cake.favorite.dto.FavoriteRequest;
import com.example.cake.favorite.dto.FavoriteResponse;
import com.example.cake.favorite.service.FavoriteService;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites/")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")

public class FavoriteController {
    private final FavoriteService favoriteService;


    @PostMapping("/{userId}")

    public ResponseEntity<ResponseMessage<FavoriteResponse>> addToFavorite(
            @PathVariable String userId,
            @RequestBody FavoriteRequest request) {
        try {
            FavoriteResponse response = favoriteService.addToFavorite(userId, request);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Đã thêm sản phẩm vào danh sách yêu thích", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Lấy danh sách yêu thích của user
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseMessage<List<FavoriteResponse>>> getFavoritesByUserId(
            @PathVariable String userId) {
        try {
            List<FavoriteResponse> favorites = favoriteService.getFavoritesByUserId(userId);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Lấy danh sách yêu thích thành công", favorites));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Xóa sản phẩm khỏi danh sách yêu thích
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<ResponseMessage<Void>> removeFromFavorite(
            @PathVariable String userId,
            @PathVariable String productId) {
        try {
            favoriteService.removeFromFavorite(userId, productId);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Đã xóa sản phẩm khỏi danh sách yêu thích", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Kiểm tra sản phẩm có trong danh sách yêu thích không
    @GetMapping("/{userId}/check/{productId}")
    public ResponseEntity<ResponseMessage<Boolean>> checkProductInFavorite(
            @PathVariable String userId,
            @PathVariable String productId) {
        try {
            boolean isInFavorite = favoriteService.isProductInFavorite(userId, productId);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Kiểm tra thành công", isInFavorite));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Đếm số lượng sản phẩm yêu thích
    @GetMapping("/{userId}/count")
    public ResponseEntity<ResponseMessage<Long>> countFavorites(
            @PathVariable String userId) {
        try {
            long count = favoriteService.countFavoritesByUserId(userId);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Đếm thành công", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Cập nhật trạng thái selected
    @PutMapping("/{userId}/{productId}/select")
    public ResponseEntity<ResponseMessage<FavoriteResponse>> updateSelectedStatus(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestParam boolean selected) {
        try {
            FavoriteResponse response = favoriteService.updateSelectedStatus(userId, productId, selected);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Cập nhật trạng thái thành công", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }

    // Xóa tất cả sản phẩm yêu thích
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ResponseMessage<Void>> clearFavorites(
            @PathVariable String userId) {
        try {
            favoriteService.clearFavorites(userId);
            return ResponseEntity.ok(new ResponseMessage<>(true, "Đã xóa tất cả sản phẩm yêu thích", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage<>(false, "Lỗi server: " + e.getMessage(), null));
        }
    }
}