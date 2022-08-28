package com.service;

import com.model.vehicle.Vehicle;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class VehicleService<T extends Vehicle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);
    protected CrudRepository<T> crudRepository;

    protected VehicleService(CrudRepository<T> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<T> findById(String id) {
        return crudRepository.findById(id);
    }

    public boolean save(T vehicle) {
        return crudRepository.save(vehicle);
    }

    public boolean save(List<T> vehicleList) {
        return crudRepository.save(vehicleList);
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

    public void print() {
        for (T vehicle : crudRepository.getAll()) {
            LOGGER.info("Print info about vehicle: {}", vehicle);
        }
    }

    public List<T> getAll() {
        return crudRepository.getAll();
    }

    public void print(T vehicle) {
        LOGGER.info("Print info about vehicle: {}", vehicle);
    }
}