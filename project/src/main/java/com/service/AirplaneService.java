package com.service;

import com.model.Airplane;
import com.model.Manufacturer;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class AirplaneService extends VehicleService<Airplane> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirplaneService.class);

    public AirplaneService(CrudRepository<Airplane> crudRepository) {
        super(crudRepository);
    }

    public Airplane create(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats) {
        return new Airplane(model, manufacturer, price, numberOfPassengerSeats);
    }

    public void print(List<Airplane> airplaneList) {
        for (Airplane airplane : airplaneList) {
            LOGGER.info("Print info about airplane: {}", airplane);
        }
    }
}
