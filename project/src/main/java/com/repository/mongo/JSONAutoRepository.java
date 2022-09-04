package com.repository.mongo;

import com.model.vehicle.Auto;

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
        return super.save(item);
    }

    @Override
    public boolean save(List<Auto> autos) {
        return super.save(autos);
    }

    @Override
    public boolean update(Auto item) {
        return super.update(item, item.getId());
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