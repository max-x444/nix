package nix.command;

import nix.service.UniversityService;

import java.util.Map;

public class NumberOfStudentsPerGroup implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();

    @Override
    public void execute() {
        for (Map.Entry<String, Long> entry : UNIVERSITY_SERVICE.numberOfStudentsPerGroup().entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }
}