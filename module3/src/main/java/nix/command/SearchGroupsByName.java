package nix.command;

import nix.service.UniversityService;

import java.util.Scanner;

public class SearchGroupsByName implements Command {
    private static final UniversityService UNIVERSITY_SERVICE = UniversityService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Write the name of the group:");
        final String name = SCANNER.nextLine();
        System.out.println("Found groups:");
        for (String s : UNIVERSITY_SERVICE.searchGroupsByName(name)) {
            System.out.println(s);
        }
    }
}