package com;

import com.model.Airplane;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.repository.AirplaneRepository;
import com.repository.AutoRepository;
import com.repository.MotorbikeRepository;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.Parking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = new AutoService(new AutoRepository());
    private static final AirplaneService AIRPLANE_SERVICE = new AirplaneService(new AirplaneRepository());
    private static final MotorbikeService MOTORBIKE_SERVICE = new MotorbikeService(new MotorbikeRepository());

    public static void main(String[] args) {
        Motorbike motorbike1 = MOTORBIKE_SERVICE.create("S 3500 RR", Manufacturer.BMW, BigDecimal.valueOf(100), 55.0, 1);
        Motorbike motorbike2 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(100), 35.0, 3);
        Motorbike motorbike3 = MOTORBIKE_SERVICE.create("S 3900 RR", Manufacturer.BMW, BigDecimal.valueOf(300), 45.0, 2);
        Motorbike motorbike4 = MOTORBIKE_SERVICE.create("S 3600 RR", Manufacturer.BMW, BigDecimal.valueOf(300), 60.0, 5);
        Airplane airplane = AIRPLANE_SERVICE.create("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(1_000_000), 1000, 1);
        List<Auto> autoList = AUTO_SERVICE.create(1);
        List<Vehicle> vehicleList = new LinkedList<>();
        vehicleList.add(motorbike1);
        vehicleList.add(airplane);
        vehicleList.add(motorbike2);
        vehicleList.add(autoList.get(0));
        vehicleList.add(motorbike3);
        vehicleList.add(motorbike4);
        Parking<Vehicle> parking = new Parking<>();
        parking.add(motorbike1);
        parking.add(airplane);
        parking.add(autoList.get(0));
        LOGGER.info(parking.print());
        StringJoiner stringJoiner = new StringJoiner("--");
        for (Vehicle vehicle : parking) {
            stringJoiner.add(vehicle.getClass().getSimpleName());
        }
        LOGGER.info(stringJoiner.toString());
        try {
            MOTORBIKE_SERVICE.print((Motorbike) parking.search(parking.getRestylingNumber(motorbike1)));
        } catch (NoSuchElementException exception) {
            LOGGER.error(exception.getMessage());
        }
        try {
            MOTORBIKE_SERVICE.print((Motorbike) parking.search("0"));
        } catch (NoSuchElementException exception) {
            LOGGER.error(exception.getMessage());
        }
        parking.set(parking.getRestylingNumber(motorbike1), airplane);
        LOGGER.info(parking.print());
        parking.set(parking.getRestylingNumber(airplane), autoList.get(0));
        LOGGER.info(parking.print());
        parking.set(parking.getRestylingNumber(autoList.get(0)), motorbike1);
        LOGGER.info(parking.print());
        parking.remove(parking.getRestylingNumber(motorbike1));
        LOGGER.info(parking.print());
        LOGGER.info("Number of restyling: {}", parking.getSize());
        LOGGER.info("Date of parking of the first vehicle {}", parking.getHeadTime());
        LOGGER.info("Date of parking of the last vehicle {}", parking.getTailTime());

        //Comparator
        StringBuilder stringBuilder = new StringBuilder();
        LOGGER.info("List before sorting: ");
        for (Vehicle vehicle : vehicleList) {
            stringBuilder
                    .append(vehicle.getPrice())
                    .append(" ")
                    .append(vehicle.getClass().getSimpleName())
                    .append(" ")
                    .append(vehicle.getCount())
                    .append(" ");
        }
        LOGGER.info(stringBuilder.toString());


        Comparator<Vehicle> comparator = Comparator.comparing(Vehicle::getPrice)
                .reversed()
                .thenComparing(vehicle -> vehicle.getClass().getSimpleName())
                .thenComparing(Vehicle::getCount);
        vehicleList.sort(comparator);

        LOGGER.info("List after sorting: ");
        stringBuilder = new StringBuilder();
        for (Vehicle vehicle : vehicleList) {
            stringBuilder
                    .append(vehicle.getPrice())
                    .append(" ")
                    .append(vehicle.getClass().getSimpleName())
                    .append(" ")
                    .append(vehicle.getCount())
                    .append(" ");
        }
        LOGGER.info(stringBuilder.toString());
    }
}