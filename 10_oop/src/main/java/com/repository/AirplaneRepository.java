package com.repository;

import com.model.Airplane;

import java.util.LinkedList;
import java.util.List;

public class AirplaneRepository {
    private final List<Airplane> airplanes;

    public AirplaneRepository() {
        this.airplanes = new LinkedList<>();
    }

    public Airplane getMotorbikeById(String id) {
        for (Airplane airplane : airplanes) {
            if (airplane.getId().equals(id)) {
                return airplane;
            }
        }
        return null;
    }

    public List<Airplane> getAll() {
        return airplanes;
    }

    public Airplane update(Airplane airplane) {
        Airplane newVersion = getMotorbikeById(airplane.getId());
        if (newVersion != null) {
            newVersion.setManufacturer(airplane.getManufacturer());
            newVersion.setModel(airplane.getModel());
            newVersion.setPrice(airplane.getPrice());
            newVersion.setNumberOfPassengerSeats(airplane.getNumberOfPassengerSeats());
            return newVersion;
        }
        return null;
    }

    public List<Airplane> create(Airplane airplane) {
        airplanes.add(airplane);
        return airplanes;
    }

    public List<Airplane> delete(Airplane airplane) {
        airplanes.remove(airplane);
        return airplanes;
    }

    public List<Airplane> delete(String id) {
        airplanes.remove(getMotorbikeById(id));
        return airplanes;
    }
}
