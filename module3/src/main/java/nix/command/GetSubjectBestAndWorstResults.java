package nix.command;

import nix.service.UniversityService;

import java.math.BigDecimal;
import java.util.Map;

public class GetSubjectBestAndWorstResults implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();

    @Override
    public void execute() {
        for (Map.Entry<String, BigDecimal> entry : UNIVERSITY_SERVICE.getSubjectBestAndWorstResults().entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }
}