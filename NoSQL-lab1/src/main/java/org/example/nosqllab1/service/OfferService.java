package org.example.nosqllab1.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.nosqllab1.dto.OffersResponse;
import org.example.nosqllab1.model.Offer;
import org.example.nosqllab1.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final EtcdService etcdService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CACHE_KEY = "offers:all";

    public OfferService(OfferRepository offerRepository, EtcdService etcdService) {
        this.offerRepository = offerRepository;
        this.etcdService = etcdService;
    }

    @Transactional
    public OffersResponse findAllOffers() {
        long startTime = System.currentTimeMillis();

        try {
            Optional<String> catchOffer = etcdService.get(CACHE_KEY);
            if (catchOffer.isPresent()) {
                List<Offer> offers = objectMapper.readValue(
                        catchOffer.get(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Offer.class));
                long duration = System.currentTimeMillis() - startTime;
                return new OffersResponse(offers, duration, "NoSQL Etcd Cache");

            }

            List<Offer> offers = offerRepository.findAll();

            if (offers.isEmpty()) {
                offers = List.of(
                        Offer.builder().title("Monthly Gym Pass").price(3000).build(),
                        Offer.builder().title("6-Month Membership").price(15000).build(),
                        Offer.builder().title("Annual Gold Pass").price(25000).build(),
                        Offer.builder().title("Personal Training (x10)").price(12000).build()
                );
                offerRepository.saveAll(offers);
            }

            etcdService.saveCatche(CACHE_KEY, objectMapper.writeValueAsString(offers));

            long duration = System.currentTimeMillis() - startTime;
            return new OffersResponse(offers, duration, "PostgreSQL Database");
        } catch (Exception e) {
            List<Offer> offers = offerRepository.findAll();

            long duration = System.currentTimeMillis() - startTime;
            return new OffersResponse(offers, duration, "PostgreSQL Database (NoSQL Down)");
        }
    }
}