package com.repository.mongo;

import com.model.vehicle.Motorbike;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class JSONMotorbikeRepository extends JSONRepository<Motorbike> {
    private static JSONMotorbikeRepository instance;

    private JSONMotorbikeRepository() {
        super("motorbike");
    }

    public static JSONMotorbikeRepository getInstance() {
        if (instance == null) {
            instance = new JSONMotorbikeRepository();
        }
        return instance;
    }

    @Override
    public boolean save(Motorbike item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        collection.insertOne(super.mapFrom(item));
        return true;
    }

    @Override
    public boolean save(List<Motorbike> motorbikes) {
        if (motorbikes.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        motorbikes.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Motorbike item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        final Document updateObject = new Document();
        updateObject.append("$set", super.mapFrom(item));
        collection.updateOne(eq("id", item.getId()), updateObject);
        return true;
    }

    @Override
    public List<Motorbike> delete(Motorbike item) {
        super.delete(item.getId());
        return super.getAll(Motorbike.class);
    }

    @Override
    public Optional<Motorbike> findById(String id) {
        return super.findById(Motorbike.class, id);
    }

    @Override
    public List<Motorbike> getAll() {
        return super.getAll(Motorbike.class);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }
}