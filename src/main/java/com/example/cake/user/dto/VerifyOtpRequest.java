package com.example.cake.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String otp;
}
