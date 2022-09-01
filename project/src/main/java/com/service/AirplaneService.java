package com.service;

import com.model.annotations.MyAutowired;
import com.model.annotations.MySingleton;
import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.repository.CrudRepository;
import com.repository.collection.AirplaneRepository;
import com.repository.mongo.JSONAirplaneRepository;

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
            instance = new AirplaneService(JSONAirplaneRepository.getInstance());
        }
        return instance;
    }

    public Airplane create(String id, String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        return new Airplane(id, model, manufacturer, price, numberOfPassengerSeats, count);
    }
}