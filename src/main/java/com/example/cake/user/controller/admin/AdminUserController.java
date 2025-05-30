package com.example.cake.user.controller.admin;



import com.example.cake.auth.model.User;
import com.example.cake.auth.model.UserPrincipal;
import com.example.cake.response.ResponseMessage;
import com.example.cake.user.dto.UpdateUserActiveRequest;
import com.example.cake.user.dto.UpdateUserRoleRequest;
import com.example.cake.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor

public class AdminUserController {

    private final UserService userService ;

    @PutMapping("/active/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<String>> updateUserActive(
            @PathVariable String id,
            @RequestBody UpdateUserActiveRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserActive(id, request.isActive()));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<String>> updateUserRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRoleRequest request,
            @AuthenticationPrincipal UserPrincipal admin) {
        return ResponseEntity.ok(userService.updateUserRole(id, admin.getEmail(), request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/read-users")
    public ResponseEntity<ResponseMessage<List<User>>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }
}
