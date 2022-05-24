package com.modsen.beershop.repository;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.model.User;
import com.modsen.beershop.service.exception.UserNotFoundException;
import com.modsen.beershop.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


public enum UserRepository {
    INSTANCE;

    private static final String SELECT_USER_BY_LOGIN_OR_EMAIL = "select * from users where login = ? or email = ?";
    private static final String SELECT_USERS_BY_LOGIN_AND_PASSWORD = "select * from users where login = ? and pass = ?";

    public boolean isExistUserByLoginOrEmail(String login, String email) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final User user = session.createNativeQuery(SELECT_USER_BY_LOGIN_OR_EMAIL, User.class)
                    .setParameter(1, login)
                    .setParameter(2, email)
                    .getSingleResult();
            return user != null;
        }
    }

    public void createUser(String login, String pass, String email, String role) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.save(User.builder()
                    .login(login)
                    .pass(pass)
                    .email(email)
                    .role(role)
                    .build());
            transaction.commit();
        }
    }

    public User readUserByLoginAndPassword(String login, String password) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final User user = session.createNativeQuery(SELECT_USERS_BY_LOGIN_AND_PASSWORD, User.class)
                    .setParameter(1, login)
                    .setParameter(2, password)
                    .getSingleResult();
            if (user == null) {
                throw new UserNotFoundException(Messages.MESSAGE.wrongLoginOrPassword());
            }
            return user;
        }
    }

}
