package org.example.nosqllab1.service;


import jakarta.transaction.Transactional;
import org.example.nosqllab1.dto.AuthRequest;
import org.example.nosqllab1.dto.RegisterRequest;
import org.example.nosqllab1.dto.UserResponce;
import org.example.nosqllab1.enums.Role;
import org.example.nosqllab1.model.User;
import org.example.nosqllab1.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EtcdService etcdService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService, EtcdService etcdService, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.etcdService = etcdService;
        this.mailSender = mailSender;
    }

    @Transactional
    public UserResponce registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        String hash = passwordEncoder.encode(registerRequest.password());

        User user = User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .password(hash)
                .role(Role.ROLE_USER.name())
                .build();

        userRepository.save(user);

        return new UserResponce(jwtService.generateToken(user), registerRequest.name(), registerRequest.email(), Role.ROLE_USER.name());
    }

    public UserResponce login(AuthRequest authRequest) {
        String lockKey = "login_attempts:" + authRequest.email();


        etcdService.get(lockKey).ifPresent(attempts -> {
            if (Long.parseLong(attempts) >= 3) {
                throw new SecurityException("The account is blocked for 5 minutes due to frequent input errors.");
            }
        });

        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            try{
                long currentAttempts = etcdService.incrementLoginAttempts(authRequest.email());

                if (currentAttempts >= 3) {
                    throw new SecurityException("The account is blocked for 5 minutes due to frequent input errors.");
                }

                throw new IllegalArgumentException("Wrong password. There are still attempts left: " + (3 - currentAttempts));
            } catch (Exception e){
                throw new SecurityException("Security system error: NoSQL storage is temporarily unavailable. Try again later.");
            }
        }

        etcdService.delete(lockKey);

        return new UserResponce(jwtService.generateToken(user), user.getName(), user.getEmail(), user.getRole());
    }


    public void sendResetCode(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String code = String.format("%04d", new SecureRandom().nextInt(10000));

        etcdService.saveResetCode(email, code);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("myflow.app@yandex.ru");
            message.setTo(email);
            message.setSubject("Reset Password");
            message.setText("Hello! Your one‑time password reset code: " + code + "\nThe code is valid for 5 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("The email was not sent: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void verifyResetCodeAndChangePassword(String email, String code, String newPassword) {
        String savedCode = etcdService.get("reset_code:" + email)
                .orElseThrow(() -> new IllegalArgumentException("The recovery code is expired or was not requested. Try again."));

        if (!savedCode.equals(code)) {
            throw new SecurityException("Invalid confirmation code!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));

        etcdService.delete("reset_code:" + email);
    }
}
