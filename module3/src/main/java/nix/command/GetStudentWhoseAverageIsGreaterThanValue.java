package nix.command;

import nix.model.human.Student;
import nix.service.UniversityService;

import java.util.Scanner;

public class GetStudentWhoseAverageIsGreaterThanValue implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Write your grade for student search:");
        final Double value = SCANNER.nextDouble();
        for (Student student : UNIVERSITY_SERVICE.getStudentWhoseAverageIsGreaterThanValue(value)) {
            System.out.println(student);
        }
    }
}