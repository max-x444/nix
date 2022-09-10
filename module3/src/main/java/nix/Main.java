package nix;

import nix.command.Action;
import nix.command.Command;
import nix.service.UniversityService;
import nix.util.UserInputUtil;
import org.flywaydb.core.Flyway;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
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

    public static void main(String[] args) {
        final Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .baselineOnMigrate(true)
                .locations("db/migration")
                .load();
        flyway.clean();
        UniversityService.getInstance();
        flyway.migrate();

        final Action[] actions = Action.values();
        Command command;
        do {
            command = actions[UserInputUtil.getUserInput(actions)].execute();
        } while (command != null);
    }
}