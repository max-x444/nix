package com;

import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.IOUtil;
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
        IOUtil ioUtil = new IOUtil();
        LOGGER.info("Creating motorbike with using a function: {}",
                MOTORBIKE_SERVICE.function.apply(ioUtil.readFile(new File(XML_PATH))));
        LOGGER.info("Creating motorbike with using a function: {}",
                MOTORBIKE_SERVICE.function.apply(ioUtil.readFile(new File(JSON_PATH))));
    }
}