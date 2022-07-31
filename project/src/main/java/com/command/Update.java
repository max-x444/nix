package com.command;

import com.model.Manufacturer;
import com.model.Vehicle;
import com.model.VehicleType;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Update implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Update.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final VehicleType value = values[UserInputUtil.getNames("What you want to update:", values)];
        switch (value) {
            case AUTO -> updateVehicle(AUTO_SERVICE.getAll());
            case MOTORBIKE -> updateVehicle(MOTORBIKE_SERVICE.getAll());
            case AIRPLANE -> updateVehicle(AIRPLANE_SERVICE.getAll());
            default -> throw new IllegalArgumentException("Cannot update " + value);
        }
    }

    private <T extends Vehicle> void updateVehicle(List<T> list) {
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

    private <T extends Vehicle> void getField(T vehicle) {
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

    private <T extends Vehicle> void updateField(T vehicle, List<String> list, int userInput, String line) {
        switch (list.get(userInput)) {
            case "Model" -> vehicle.setModel(line);
            case "Price" -> vehicle.setPrice(BigDecimal.valueOf(Long.parseLong(line)));
            case "Manufacturer" -> vehicle.setManufacturer(Manufacturer.valueOf(line));
            case "Count" -> vehicle.setCount(Integer.parseInt(line));
        }
    }
}