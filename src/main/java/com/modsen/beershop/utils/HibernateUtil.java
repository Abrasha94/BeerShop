package com.modsen.beershop.utils;

import com.modsen.beershop.service.exception.SessionFactoryCreateException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (HibernateException e) {
                throw new SessionFactoryCreateException(e.getMessage());
            }
        }
        return sessionFactory;
    }
}
