package com.service;

import com.model.Airplane;
import com.model.Manufacturer;
import com.repository.AirplaneRepository;
import com.repository.CrudRepository;

import java.math.BigDecimal;

public class AirplaneService extends VehicleService<Airplane> {
    private static AirplaneService instance;

    public AirplaneService(CrudRepository<Airplane> crudRepository) {
        super(crudRepository);
    }

    public static AirplaneService getInstance() {
        if (instance == null) {
            instance = new AirplaneService(new AirplaneRepository());
        }
        return instance;
    }

    public Airplane create(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        Airplane airplane = new Airplane(model, manufacturer, price, numberOfPassengerSeats, count);
        crudRepository.create(airplane);
        return airplane;
    }
}