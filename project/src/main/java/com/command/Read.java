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

public class Read implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Read.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final VehicleType value = values[UserInputUtil.getNames("What you want to read:", values)];
        switch (value) {
            case AUTO -> printVehicle(AUTO_SERVICE.getAll());
            case MOTORBIKE -> printVehicle(MOTORBIKE_SERVICE.getAll());
            case AIRPLANE -> printVehicle(AIRPLANE_SERVICE.getAll());
            default -> throw new IllegalArgumentException("Cannot read " + value);
        }
    }

    private <T extends Vehicle> void printVehicle(List<T> list) {
        LOGGER.info("List of vehicles: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d) %s%n", i, list.get(i));
        }
    }
}