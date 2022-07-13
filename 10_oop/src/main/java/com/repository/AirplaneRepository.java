package com.repository;

import com.model.Airplane;

import java.util.LinkedList;
import java.util.List;

public class AirplaneRepository implements CrudRepository<Airplane> {
    private final List<Airplane> airplanes;

    public AirplaneRepository() {
        this.airplanes = new LinkedList<>();
    }

    @Override
    public Airplane getById(String id) {
        for (Airplane airplane : airplanes) {
            if (airplane.getId().equals(id)) {
                return airplane;
            }
        }
        return null;
    }

    @Override
    public List<Airplane> getAll() {
        return airplanes;
    }


    @Override
    public boolean create(Airplane airplane) {
        return airplanes.add(airplane);
    }

    @Override
    public boolean create(List<Airplane> airplaneList) {
        return airplanes.addAll(airplaneList);
    }

    @Override
    public boolean update(Airplane airplane) {
        Airplane newVersion = getById(airplane.getId());
        if (newVersion != null) {
            newVersion.setManufacturer(airplane.getManufacturer());
            newVersion.setModel(airplane.getModel());
            newVersion.setPrice(airplane.getPrice());
            newVersion.setNumberOfPassengerSeats(airplane.getNumberOfPassengerSeats());
            return true;
        }
        return false;
    }


    @Override
    public List<Airplane> delete(Airplane airplane) {
        airplanes.remove(airplane);
        return airplanes;
    }

    @Override
    public boolean delete(String id) {
        return airplanes.remove(getById(id));
    }
}