package org.example.nosqllab1.dto;

public record AuthRequest(
        String email,
        String password
) {}
