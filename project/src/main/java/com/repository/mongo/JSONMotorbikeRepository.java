package com.repository.mongo;

import com.model.vehicle.Motorbike;

import java.util.List;
import java.util.Optional;

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
        return super.save(item);
    }

    @Override
    public boolean save(List<Motorbike> motorbikes) {
        return super.save(motorbikes);
    }

    @Override
    public boolean update(Motorbike item) {
        return super.update(item, item.getId());
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