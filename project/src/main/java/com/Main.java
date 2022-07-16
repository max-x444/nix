package com;

import com.model.Manufacturer;
import com.model.Motorbike;
import com.repository.AirplaneRepository;
import com.repository.AutoRepository;
import com.repository.MotorbikeRepository;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.service.MyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = new AutoService(new AutoRepository());
    private static final AirplaneService AIRPLANE_SERVICE = new AirplaneService(new AirplaneRepository());
    private static final MotorbikeService MOTORBIKE_SERVICE = new MotorbikeService(new MotorbikeRepository());

    public static void main(String[] args) {
        Motorbike motorbike = MOTORBIKE_SERVICE.create("S 3500 RR", Manufacturer.BMW, BigDecimal.valueOf(100), 55.0);
        MyContainer<Motorbike> myContainer = new MyContainer<>(motorbike);
        MOTORBIKE_SERVICE.print(myContainer.getValue());
        MOTORBIKE_SERVICE.print(myContainer.getDiscount());
        MOTORBIKE_SERVICE.print(myContainer.increasePrice(2));
    }
}