package nix;

import nix.command.Action;
import nix.command.Command;
import nix.service.UniversityService;
import nix.util.UserInputUtil;
import org.flywaydb.core.Flyway;

public class Main {
    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String URL = System.getenv("URL");

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