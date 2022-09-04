package com.repository.mongo;

import com.model.vehicle.Airplane;

import java.util.List;
import java.util.Optional;

public class JSONAirplaneRepository extends JSONRepository<Airplane> {
    private static JSONAirplaneRepository instance;

    private JSONAirplaneRepository() {
        super("airplane");
    }

    public static JSONAirplaneRepository getInstance() {
        if (instance == null) {
            instance = new JSONAirplaneRepository();
        }
        return instance;
    }

    @Override
    public boolean save(Airplane item) {
        return super.save(item);
    }

    @Override
    public boolean save(List<Airplane> airplanes) {
        return super.save(airplanes);
    }

    @Override
    public boolean update(Airplane item) {
        return super.update(item, item.getId());
    }

    @Override
    public List<Airplane> delete(Airplane item) {
        super.delete(item.getId());
        return super.getAll(Airplane.class);
    }

    @Override
    public Optional<Airplane> findById(String id) {
        return super.findById(Airplane.class, id);
    }

    @Override
    public List<Airplane> getAll() {
        return super.getAll(Airplane.class);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }
}