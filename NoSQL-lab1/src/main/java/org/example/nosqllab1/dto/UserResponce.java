package org.example.nosqllab1.dto;

public record UserResponce(
        String token,
        String name,
        String email,
        String role
) {}
