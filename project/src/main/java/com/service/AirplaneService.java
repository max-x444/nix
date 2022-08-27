package com.service;

import com.model.annotations.MyAutowired;
import com.model.annotations.MySingleton;
import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.repository.CrudRepository;
import com.repository.jdbc.DBAirplaneRepository;
import com.repository.list.AirplaneRepository;

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
            instance = new AirplaneService(DBAirplaneRepository.getInstance());
        }
        return instance;
    }

    public Airplane create(String id, String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        final Airplane airplane = new Airplane(id, model, manufacturer, price, numberOfPassengerSeats, count);
        crudRepository.save(airplane);
        return airplane;
    }
}