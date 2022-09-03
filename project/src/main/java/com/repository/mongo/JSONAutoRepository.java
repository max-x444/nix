package com.repository.mongo;

import com.model.vehicle.Auto;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class JSONAutoRepository extends JSONRepository<Auto> {
    private static JSONAutoRepository instance;

    private JSONAutoRepository() {
        super("auto");
    }

    public static JSONAutoRepository getInstance() {
        if (instance == null) {
            instance = new JSONAutoRepository();
        }
        return instance;
    }

    @Override
    public boolean save(Auto item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        collection.insertOne(super.mapFrom(item));
        return true;
    }

    @Override
    public boolean save(List<Auto> autos) {
        if (autos.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        autos.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Auto item) {
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
    public List<Auto> delete(Auto item) {
        super.delete(item.getId());
        return super.getAll(Auto.class);
    }

    @Override
    public Optional<Auto> findById(String id) {
        return super.findById(Auto.class, id);
    }

    @Override
    public List<Auto> getAll() {
        return super.getAll(Auto.class);
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }
}