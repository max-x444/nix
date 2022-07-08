package com;

import com.model.Airplane;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.repository.AirplaneRepository;
import com.repository.MotorbikeRepository;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = new AutoService();
    private static final MotorbikeService MOTORBIKE_SERVICE = new MotorbikeService();
    private static final AirplaneService AIRPLANE_SERVICE = new AirplaneService();

    public static void main(String[] args) {
        final List<Auto> autos = AUTO_SERVICE.createAutos(10);
        AUTO_SERVICE.saveAutos(autos);
        AUTO_SERVICE.printAll();
        LOGGER.info("Create airplane");
        AirplaneRepository airplaneRepository = new AirplaneRepository();
        Airplane airplane = new Airplane("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(1_000_000), 1000);
        airplaneRepository.create(airplane);
        AIRPLANE_SERVICE.print(airplane);
        MotorbikeRepository motorbikeRepository = new MotorbikeRepository();
        Motorbike motorbike1 = new Motorbike("S 1000 RR", Manufacturer.BMW, BigDecimal.valueOf(30_000), 50.0);
        motorbikeRepository.create(motorbike1);
        Motorbike motorbike2 = new Motorbike("R 1250 RS", Manufacturer.BMW, BigDecimal.valueOf(35_000), 45.0);
        motorbikeRepository.create(motorbike2);
        LOGGER.info("Create motorbikes");
        MOTORBIKE_SERVICE.print(motorbikeRepository.getAll());
        motorbike2.setLeanAngle(60.0);
        motorbikeRepository.update(motorbike2);
        LOGGER.info("Update second motorbike");
        MOTORBIKE_SERVICE.print(motorbikeRepository.getAll());
        motorbikeRepository.delete(motorbike2);
        motorbikeRepository.delete(motorbike1.getId());
        LOGGER.info("Delete all motorbikes");
        MOTORBIKE_SERVICE.print(motorbikeRepository.getAll());
    }
}