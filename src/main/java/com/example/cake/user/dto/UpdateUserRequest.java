package com.example.cake.user.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String fullname;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private String address;
}
