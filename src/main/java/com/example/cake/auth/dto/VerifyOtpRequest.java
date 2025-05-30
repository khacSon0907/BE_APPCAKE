
package com.example.cake.auth.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    private String token;
    @NotBlank(message = "OTP không được bỏ trống  ")
    private String otp;
}
