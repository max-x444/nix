package com.service;

import com.model.Vehicle;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class VehicleService<T extends Vehicle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);
    protected CrudRepository<T> crudRepository;

    public VehicleService(CrudRepository<T> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public boolean save(T vehicle) {
        return crudRepository.create(vehicle);
    }

    public boolean update(T vehicle) {
        return crudRepository.update(vehicle);
    }

    public List<T> delete(T vehicle) {
        return crudRepository.delete(vehicle);
    }

    public boolean delete(String id) {
        return crudRepository.delete(id);
    }

    public void print(T vehicle) {
        LOGGER.info("Print info about vehicle: {}", vehicle);
    }
}