package nix.command;

import nix.model.Teacher;
import nix.service.UniversityService;

import java.util.Scanner;

public class SearchTeacherByNameOrSurname implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Write the name of the teacher:");
        final String name = SCANNER.nextLine();
        System.out.println("Write the surname of the teacher:");
        final String surname = SCANNER.nextLine();
        System.out.println("Found teachers:");
        for (Teacher teacher : UNIVERSITY_SERVICE.searchTeacherByNameOrSurname(name, surname)) {
            System.out.println(teacher);
        }
    }
}