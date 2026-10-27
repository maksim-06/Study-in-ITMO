package org.example.nosqllab1.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {}
