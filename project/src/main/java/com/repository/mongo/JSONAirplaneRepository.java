package com.repository.mongo;

import com.model.vehicle.Airplane;
import org.bson.Document;

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
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        collection.insertOne(super.mapFrom(item));
        return true;
    }

    @Override
    public boolean save(List<Airplane> airplanes) {
        if (airplanes.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        airplanes.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Airplane item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        final Document filter = new Document();
        filter.append("id", item.getId());
        final Document updateObject = new Document();
        updateObject.append("$set", super.mapFrom(item));
        collection.updateOne(filter, updateObject);
        return true;
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