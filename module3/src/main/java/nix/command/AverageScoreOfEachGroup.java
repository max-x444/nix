package nix.command;

import nix.service.UniversityService;

import java.util.Map;

public class AverageScoreOfEachGroup implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();

    @Override
    public void execute() {
        for (Map.Entry<String, Double> entry : UNIVERSITY_SERVICE.averageScoreOfEachGroup().entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }
}