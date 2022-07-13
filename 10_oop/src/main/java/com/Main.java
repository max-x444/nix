package com;

import com.model.Airplane;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Motorbike;
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
        LOGGER.info("Create auto");
        final List<Auto> autos = AUTO_SERVICE.createAutos(10);
        AUTO_SERVICE.saveAutos(autos);
        AUTO_SERVICE.printAll();

        LOGGER.info("Create airplane");
        Airplane airplane = AIRPLANE_SERVICE.create("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(1_000_000), 1000);
        AIRPLANE_SERVICE.print(airplane);

        LOGGER.info("Create motorbikes");
        Motorbike motorbike1 = MOTORBIKE_SERVICE.create("S 1000 RR", Manufacturer.BMW, BigDecimal.valueOf(30_000), 50.0);
        MOTORBIKE_SERVICE.save(motorbike1);
        MOTORBIKE_SERVICE.print(motorbike1);
        Motorbike motorbike2 = MOTORBIKE_SERVICE.create("R 1250 RS", Manufacturer.BMW, BigDecimal.valueOf(35_000), 45.0);
        MOTORBIKE_SERVICE.save(motorbike2);
        MOTORBIKE_SERVICE.print(motorbike2);
        LOGGER.info("Update second motorbike2");
        motorbike2.setLeanAngle(60.0);
        MOTORBIKE_SERVICE.update(motorbike2);
        MOTORBIKE_SERVICE.print(motorbike2);
        MOTORBIKE_SERVICE.delete(motorbike2);
        if (MOTORBIKE_SERVICE.delete(motorbike1.getId())) {
            LOGGER.info("Delete all motorbikes");
        }
    }
}