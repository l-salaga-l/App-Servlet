package org.example.appservlet.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Создание экземпляра SessionFactory из конфигурации Hibernate
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Первоначальное создание SessionFactory завершилось ошибкой.\n" + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
