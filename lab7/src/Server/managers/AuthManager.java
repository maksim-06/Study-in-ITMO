package Server.managers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class AuthManager {
    private final Connection connection;
    private final int SALT_LENGTH = 8;
    private final String pepper;

    public AuthManager(Connection connection, String pepper) {
        this.connection = connection;
        this.pepper = pepper;
    }

    public int registerUser(String login, String password) throws SQLException {
        System.out.println("Регистрация пользователя: " + login);

        String salt = generateSalt();
        String passwordHash = generatePasswordHash(password, salt);
        try (PreparedStatement stmt = connection.prepareStatement(DatabaseCommands.addUser, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, login);
            stmt.setString(2, passwordHash);
            stmt.setString(3, salt);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    System.out.println("Пользователь успешно зарегистрирован с id=" + userId);
                    return userId;
                }
            }
        }

        throw new SQLException("Не удалось зарегистрировать пользователя");
    }

    public int authenticateUser(String login, String password) throws SQLException {
        String selectSQL = "SELECT id, password_digest, salt FROM users WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Пользователь не найден: " + login);
                return -1;
            }

            int userId = rs.getInt("id");
            String expectedHash = rs.getString("password_digest");
            String salt = rs.getString("salt");

            String actualHash = generatePasswordHash(password, salt);
            if (expectedHash.equals(actualHash)) {
                System.out.println("Аутентификация успешна для пользователя: " + login);
                return userId;
            } else {
                System.out.println("Неверный пароль для пользователя: " + login);
                return -1;
            }
        }
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generatePasswordHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            String toHash = password + salt + pepper;
            byte[] hash = md.digest(toHash.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 не поддерживается", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}

