package nix.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public final class HibernateFactoryUtil {
    private static final URI DATABASE_URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String URL;

    static {
        try {
            DATABASE_URL = new URI(System.getenv("DATABASE_URL"));
            URL = String.format("jdbc:postgresql://%s:%d%s", DATABASE_URL.getHost(), DATABASE_URL.getPort(), DATABASE_URL.getPath());
            USER = DATABASE_URL.getUserInfo().split(":")[0];
            PASSWORD = DATABASE_URL.getUserInfo().split(":")[1];
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

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