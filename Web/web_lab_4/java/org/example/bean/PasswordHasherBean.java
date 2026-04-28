package org.example.bean;


import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.Singleton;

@Singleton
public class PasswordHasherBean {
    private static final int BCRYPT_ROUNDS = 12;

    public String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));
    }


    public boolean checkPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
