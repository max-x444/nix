package com.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class HibernateFactoryUtil {
    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String URL = System.getenv("URL");
    private static EntityManager entityManager;

    private HibernateFactoryUtil() {
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            final Map<String, Object> configOverriders = new HashMap<>();
            configOverriders.put("javax.persistence.jdbc.url", URL);
            configOverriders.put("javax.persistence.jdbc.user", USER);
            configOverriders.put("javax.persistence.jdbc.password", PASSWORD);
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("persistence", configOverriders);
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
}