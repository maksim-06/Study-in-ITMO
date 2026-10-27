package org.example.nosqllab1.dto;

import org.example.nosqllab1.model.Offer;

import java.util.List;

public record OffersResponse(
        List<Offer> offers,
        long executionTimeMs,
        String dataSource
) {}
