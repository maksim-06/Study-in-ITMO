package org.example.nosqllab1.controller;


import org.example.nosqllab1.dto.ApplicationRequest;
import org.example.nosqllab1.enums.Role;
import org.example.nosqllab1.enums.StatusOrder;
import org.example.nosqllab1.model.Order;
import org.example.nosqllab1.model.User;
import org.example.nosqllab1.repository.UserRepository;
import org.example.nosqllab1.service.JwtService;
import org.example.nosqllab1.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OrderController(OrderService orderService, UserRepository userRepository, JwtService jwtService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    private String getEmailFromToken(String token) {
        String pureToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return jwtService.extractEmail(pureToken);
    }

    @PostMapping("/create")
    public void createOrder(@RequestBody ApplicationRequest applicationRequest,
                            @RequestHeader("Authorization") String token) {

        String currentUserEmail = getEmailFromToken(token);

        orderService.createOrder(applicationRequest, currentUserEmail);
    }

    @PatchMapping("/change/{orderId}")
    public void changeOrderStatus(@PathVariable Long orderId, @RequestParam String status,
                                  @RequestHeader("Authorization") String token) {

        String email = getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!Role.ROLE_MODERATOR.name().equals(user.getRole())) {
            throw new SecurityException("Access denied. Only moderators can change order status.");
        }

        StatusOrder statusEnum = StatusOrder.valueOf(status.toUpperCase());
        orderService.changeOrderStatus(orderId, statusEnum);
    }

    @GetMapping("/user")
    public List<Order> findUserOrders(@RequestHeader("Authorization") String token) {
        String email = getEmailFromToken(token);
        return orderService.findUserOrders(email);
    }

    @GetMapping("/all")
    public List<Order> findAllOrders(@RequestHeader("Authorization") String token) {
        String email = getEmailFromToken(token);

        User moderator = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Moderator not found with email: " + email));

        return orderService.findAll(moderator);
    }
}
