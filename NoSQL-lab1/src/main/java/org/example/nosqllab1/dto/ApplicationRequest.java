package org.example.nosqllab1.dto;

public record ApplicationRequest (
        String name,
        String email,
        String title,
        int price
) {}
