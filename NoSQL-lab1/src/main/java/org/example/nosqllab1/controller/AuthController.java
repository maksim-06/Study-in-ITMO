package org.example.nosqllab1.controller;


import org.example.nosqllab1.dto.*;
import org.example.nosqllab1.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponce registerUser(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public UserResponce loginUser(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/forgot-password")
    public void sendResetCode(@RequestBody ResetRequest resetRequest) {
        authService.sendResetCode(resetRequest.email());
    }

    @PostMapping("/reset-password")
    public void verifyResetCodeAndChangePassword(@RequestBody ConfirmResetRequest confirmResetRequest) {
        authService.verifyResetCodeAndChangePassword(
                confirmResetRequest.email(),
                confirmResetRequest.code(),
                confirmResetRequest.newPassword());
    }
}
