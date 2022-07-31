package com.command;

import com.model.Vehicle;
import com.model.VehicleType;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Delete implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Delete.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final VehicleType value = values[UserInputUtil.getNames("What you want to delete:", values)];
        switch (value) {
            case AUTO -> AUTO_SERVICE.delete(deleteVehicle(AUTO_SERVICE.getAll()));
            case MOTORBIKE -> MOTORBIKE_SERVICE.delete(deleteVehicle(MOTORBIKE_SERVICE.getAll()));
            case AIRPLANE -> AIRPLANE_SERVICE.delete(deleteVehicle(AIRPLANE_SERVICE.getAll()));
            default -> throw new IllegalArgumentException("Cannot delete " + value);
        }
    }

    private <T extends Vehicle> String deleteVehicle(List<T> list) {
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
        System.out.println(list.get(userInput).getId());
        return list.get(userInput).getId();
    }
}