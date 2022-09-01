package com.repository.mongo;

import com.model.vehicle.Motorbike;
import com.repository.CrudRepository;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class JSONMotorbikeRepository extends JSONRepository<Motorbike> implements CrudRepository<Motorbike> {
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
        collection.insertOne(mapFrom(item));
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
        final Document filter = new Document();
        filter.append("id", item.getId());
        final Document newData = new Document();
        newData.append("manufacturer", item.getManufacturer().getTitle());
        newData.append("type", item.getType().getTitle());
        newData.append("price", item.getPrice().doubleValue());
        newData.append("model", item.getModel());
        newData.append("count", item.getCount());
        newData.append("details", item.getDetails());
        newData.append("leanAngle", item.getLeanAngle());
        newData.append("currency", item.getCurrency());
        newData.append("createdMotorbike", item.getCreatedMotorbike());
        newData.append("engine", item.getEngine());
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        collection.updateOne(filter, updateObject);
        return true;
    }

    @Override
    public List<Motorbike> delete(Motorbike item) {
        super.delete(item.getId());
        return this.getAll();
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