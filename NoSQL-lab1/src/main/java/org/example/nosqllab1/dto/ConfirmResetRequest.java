package org.example.nosqllab1.dto;

public record ConfirmResetRequest(
        String email,
        String code,
        String newPassword
) {}
