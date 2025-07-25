package com.example.cake.favorite.dto;

import com.example.cake.favorite.model.FavoriteItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteResponse {
    private String idFavorite;
    private String userId;
    private List<FavoriteItem> favoriteItem;
}