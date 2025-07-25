package com.example.cake.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String street;       // Số nhà, tên đường
    private String ward;         // Phường/Xã
    private String district;     // Quận/Huyện
    private String city;         // Thành phố/Tỉnh
}
