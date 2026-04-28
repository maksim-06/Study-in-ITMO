package org.example.bean;


import org.example.entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class AuthBean {

    @PersistenceContext(name = "labPU")
    EntityManager entityManager;

    @EJB
    PasswordHasherBean passwordHasherBean;

    public User registerUser(String login, String password) {
        if (userExists(login)) {
            return null;
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword_hash(passwordHasherBean.hashPassword(password));

        entityManager.persist(user);
        return user;
    }

    public User login(String login, String password) {
        try {
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();

            if (passwordHasherBean.checkPassword(password, user.getPassword_hash())) {
                return user;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean userExists(String login) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.login = :login",
                        Long.class)
                .setParameter("login", login)
                .getSingleResult();
        return count > 0;
    }

    public User findUserById(Long id) {
        try {
            return entityManager.find(User.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}
