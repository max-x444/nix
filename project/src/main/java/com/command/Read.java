package com.command;

import com.model.VehicleType;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.UserInputUtil;

public class Read implements Command {
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final VehicleType value = values[UserInputUtil.getNames("What you want to read:", values)];
        switch (value) {
            case AUTO -> UserInputUtil.printVehicle(AUTO_SERVICE.getAll());
            case MOTORBIKE -> UserInputUtil.printVehicle(MOTORBIKE_SERVICE.getAll());
            case AIRPLANE -> UserInputUtil.printVehicle(AIRPLANE_SERVICE.getAll());
            default -> throw new IllegalArgumentException("Cannot read " + value);
        }
    }
}
