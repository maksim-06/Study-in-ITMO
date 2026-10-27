package org.example.nosqllab1.controller;


import org.example.nosqllab1.dto.OffersResponse;
import org.example.nosqllab1.service.OfferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;

    public  OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public OffersResponse getOffers() {
        return offerService.findAllOffers();
    }
}
