package com.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class HibernateFactoryUtil {
    private static EntityManager entityManager;

    private HibernateFactoryUtil() {
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("persistence");
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
}