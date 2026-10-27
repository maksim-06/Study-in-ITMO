package org.example.nosqllab1.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int price;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order() {
    }

    private Order(OrderBuilder builder) {
        this.title = builder.title;
        this.status = builder.status;
        this.price = builder.price;
        this.contactName = builder.contactName;
        this.contactEmail = builder.contactEmail;
        this.user = builder.user;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }

    public static class OrderBuilder {
        private String title;
        private String status;
        private int price;
        private String contactName;
        private String contactEmail; // Обновили
        private User user;

        public OrderBuilder title(String title) {
            this.title = title;
            return this;
        }

        public OrderBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderBuilder price(int price) {
            this.price = price;
            return this;
        }

        public OrderBuilder contactName(String contactName) {
            this.contactName = contactName;
            return this;
        }

        public OrderBuilder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public OrderBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
