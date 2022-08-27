package com;

import com.utils.SingletonUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    //    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
//    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();
//    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final String JSON_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.json")).getFile();
    private static final String XML_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.xml")).getFile();

    @SneakyThrows
    public static void main(String[] args) {
        SingletonUtil.printCache(SingletonUtil.getCache("com"));
    }
}