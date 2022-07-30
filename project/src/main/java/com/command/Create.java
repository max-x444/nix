package com.command;

import com.model.Manufacturer;
import com.model.VehicleType;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Create implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(Create.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final VehicleType value = values[UserInputUtil.getNames("What you want to create:", values)];
        switch (value) {
            case AUTO -> AUTO_SERVICE.create(1);
            case MOTORBIKE -> MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(7), 35.0, 3);
            case AIRPLANE -> AIRPLANE_SERVICE.create("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(11), 1000, 1);
            default -> throw new IllegalArgumentException("Cannot build " + value);
        }

        LOGGER.info("{} is created",
                value.toString().substring(0, 1).toUpperCase()
                        + value.toString().substring(1).toLowerCase());
    }
}