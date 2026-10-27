package org.example.nosqllab1.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private int price;

    public Offer() {
    }

    private Offer(OfferBuilder builder) {
        this.title = builder.title;
        this.price = builder.price;
    }

    public static OfferBuilder builder() {
        return new OfferBuilder();
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(title, offer.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }


    public static class OfferBuilder {
        private String title;
        private int price;

        public OfferBuilder title(String title) {
            this.title = title;
            return this;
        }

        public OfferBuilder price(int price) {
            this.price = price;
            return this;
        }

        public Offer build() {
            return new Offer(this);
        }
    }
}
