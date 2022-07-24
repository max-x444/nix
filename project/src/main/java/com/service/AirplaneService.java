package com.service;

import com.model.Airplane;
import com.model.Manufacturer;
import com.repository.CrudRepository;

import java.math.BigDecimal;

public class AirplaneService extends VehicleService<Airplane> {
    public AirplaneService(CrudRepository<Airplane> crudRepository) {
        super(crudRepository);
    }

    public Airplane create(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        return new Airplane(model, manufacturer, price, numberOfPassengerSeats, count);
    }
}