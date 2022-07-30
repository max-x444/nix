package com.utils;

import com.command.Action;
import com.model.Manufacturer;
import com.model.Vehicle;
import com.model.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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

    public static <T extends Vehicle> String deleteVehicle(List<T> list) {
        if (list.isEmpty()) {
            LOGGER.info("No vehicles found");
            return null;
        }
        int userInput;
        LOGGER.info("Which vehicle do you want to delete: ");
        do {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d) %s%n", i, list.get(i));
            }
            userInput = SCANNER.nextInt();
        } while (userInput < 0 || userInput >= list.size());
        LOGGER.info("Vehicle number {} removed", userInput);
        return list.get(userInput).getId();
    }

    public static <T extends Vehicle> void updateVehicle(List<T> list) {
        if (list.isEmpty()) {
            LOGGER.info("No vehicles found");
            return;
        }
        int userInput;
        LOGGER.info("Which vehicle do you want to update: ");
        do {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d) %s%n", i, list.get(i));
            }
            userInput = SCANNER.nextInt();
        } while (userInput < 0 || userInput >= list.size());

        getField(list.get(userInput));
    }

    private static <T extends Vehicle> void getField(T vehicle) {
        Field[] fields;
        List<String> listFields = new ArrayList<>();
        String str;
        int userInput;
        int j;
        do {
            LOGGER.info("What do you want to change: ");
            j = 0;
            for (Class<?> cls = vehicle.getClass().getSuperclass(); cls != null; cls = cls.getSuperclass()) {
                fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    str = field.getName();
                    if (!str.equals("id")) {
                        str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                        listFields.add(str);
                        System.out.printf("%d) %s%n", j++, str);
                    }
                }
            }
            System.out.printf("%d) %s%n", j++, "Exit");
            userInput = SCANNER.nextInt();

            if (0 <= userInput && userInput < --j) {
                LOGGER.info("Write a new value for the {} field: ", listFields.get(userInput));
                updateField(vehicle, listFields, userInput, SCANNER.next());
            }
        } while (userInput != j);
    }

    private static <T extends Vehicle> void updateField(T vehicle, List<String> list, int userInput, String line) {
        switch (list.get(userInput)) {
            case "Model" -> vehicle.setModel(line);
            case "Price" -> vehicle.setPrice(BigDecimal.valueOf(Long.parseLong(line)));
            case "Manufacturer" -> vehicle.setManufacturer(Manufacturer.valueOf(line));
            case "Count" -> vehicle.setCount(Integer.parseInt(line));
        }
    }

    public static <T extends Vehicle> void printVehicle(List<T> list) {
        LOGGER.info("List of vehicles: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d) %s%n", i, list.get(i));
        }
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