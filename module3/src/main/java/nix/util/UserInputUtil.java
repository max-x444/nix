package nix.util;

import nix.command.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public final class UserInputUtil {
    private static final Scanner SCANNER = new Scanner(System.in);

    private UserInputUtil() {
    }

    public static int getUserInput(Action[] actions) {
        final List<String> list = Arrays.stream(actions)
                .map(Action::getName)
                .toList();
        int userInput;
        do {
            System.out.println("What you want:");
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d) %s%n", i, list.get(i));
            }
            userInput = SCANNER.nextInt();
        } while (userInput < 0 || userInput >= list.size());
        return userInput;
    }
}