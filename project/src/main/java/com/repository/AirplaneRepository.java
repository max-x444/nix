package com.repository;

import com.model.Airplane;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AirplaneRepository implements CrudRepository<Airplane> {
    private final List<Airplane> airplanes;

    public AirplaneRepository() {
        this.airplanes = new LinkedList<>();
    }

    @Override
    public Optional<Airplane> findById(String id) {
        for (Airplane airplane : airplanes) {
            if (airplane.getId().equals(id)) {
                return Optional.of(airplane);
            }
        }
        return Optional.empty();
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
        final Optional<Airplane> founded = findById(airplane.getId());
        if (founded.isPresent()) {
            founded.get().setManufacturer(airplane.getManufacturer());
            founded.get().setModel(airplane.getModel());
            founded.get().setPrice(airplane.getPrice());
            founded.get().setNumberOfPassengerSeats(airplane.getNumberOfPassengerSeats());
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
        return findById(id).isPresent() && airplanes.remove(findById(id).get());
    }
}