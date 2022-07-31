package com.utils;

import com.command.Action;
import com.model.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInputUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInputUtil.class);
    private static final Scanner SCANNER = new Scanner(System.in);

    private static int getUserInput(String line, List<String> list) {
        int userInput;
        do {
            LOGGER.info(line);
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d) %s%n", i, list.get(i));
            }
            userInput = SCANNER.nextInt();
        } while (userInput < 0 || userInput >= list.size());
        return userInput;
    }

    public static int getNames(String line, VehicleType[] vehicleTypes) {
        List<String> list = new ArrayList<>();
        for (VehicleType type : vehicleTypes) {
            list.add(type.name());
        }
        return getUserInput(line, list);
    }

    public static int getNames(String line, Action[] actions) {
        List<String> list = new ArrayList<>();
        for (Action action : actions) {
            list.add(action.name());
        }
        return getUserInput(line, list);
    }
}