package com;

import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final String JSON_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.json")).getFile();
    private static final String XML_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.xml")).getFile();

    public static void main(String[] args) {
        LOGGER.info("Creating motorbike with using a function: {}",
                MOTORBIKE_SERVICE.function.apply(
                        MOTORBIKE_SERVICE.createMap(
                                MOTORBIKE_SERVICE.readFile(new File(XML_PATH)), "vehicle.xml")));
        LOGGER.info("Creating motorbike with using a function: {}",
                MOTORBIKE_SERVICE.function.apply(
                        MOTORBIKE_SERVICE.createMap(
                                MOTORBIKE_SERVICE.readFile(new File(JSON_PATH)), "vehicle.json")));
    }
}