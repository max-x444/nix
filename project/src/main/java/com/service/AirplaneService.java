package com.service;

import com.model.annotations.MyAutowired;
import com.model.annotations.MySingleton;
import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.repository.AirplaneRepository;
import com.repository.CrudRepository;

import java.math.BigDecimal;

@MySingleton
public class AirplaneService extends VehicleService<Airplane> {
    private static AirplaneService instance;

    @MyAutowired(AirplaneRepository.class)
    public AirplaneService(CrudRepository<Airplane> crudRepository) {
        super(crudRepository);
    }

    public static AirplaneService getInstance() {
        if (instance == null) {
            instance = new AirplaneService(AirplaneRepository.getInstance());
        }
        return instance;
    }

    public Airplane create(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        Airplane airplane = new Airplane(model, manufacturer, price, numberOfPassengerSeats, count);
        crudRepository.create(airplane);
        return airplane;
    }
}