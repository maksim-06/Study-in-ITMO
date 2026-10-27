package org.example.nosqllab1.service;


import jakarta.transaction.Transactional;
import org.example.nosqllab1.dto.ApplicationRequest;
import org.example.nosqllab1.enums.Role;
import org.example.nosqllab1.enums.StatusOrder;
import org.example.nosqllab1.model.Order;
import org.example.nosqllab1.model.User;
import org.example.nosqllab1.repository.OrderRepository;
import org.example.nosqllab1.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EtcdService etcdService;
    private final ObjectMapper objectMapper;

    private static final String CACHE_KEY_ALL_ORDERS = "orders:all";

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, EtcdService etcdService, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.etcdService = etcdService;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public void createOrder(ApplicationRequest applicationRequest, String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Order order = Order.builder()
                .contactName(applicationRequest.name())
                .contactEmail(applicationRequest.email())
                .title(applicationRequest.title())
                .price(applicationRequest.price())
                .status(StatusOrder.NEW.name())
                .user(currentUser)
                .build();

        orderRepository.save(order);
        etcdService.delete(CACHE_KEY_ALL_ORDERS);
    }

    @Transactional
    public void changeOrderStatus(Long orderId, StatusOrder status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status.name());

        etcdService.delete(CACHE_KEY_ALL_ORDERS);
    }

    @Transactional
    public List<Order> findUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUser_Id(user.getId());
    }


    public List<Order> findAll(User user) {
        if (!Role.ROLE_MODERATOR.name().equals(user.getRole())) {
            throw new SecurityException("Access denied");
        }

        try {
            Optional<String> cachedData = etcdService.get(CACHE_KEY_ALL_ORDERS);

            if (cachedData.isPresent()) {
                return objectMapper.readValue(
                        cachedData.get(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class)
                );
            }

            List<Order> orders = orderRepository.findAll();

            String jsonValue = objectMapper.writeValueAsString(orders);
            etcdService.saveCatche(CACHE_KEY_ALL_ORDERS, jsonValue);

            return orders;
        } catch (Exception e) {
            return orderRepository.findAll();
        }
    }
}
