package com.example.cake.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    @NotBlank(message = "quyền user không được để trống ")
    private String role;
}
