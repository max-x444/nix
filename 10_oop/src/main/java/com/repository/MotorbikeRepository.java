package com.repository;

import com.model.Motorbike;

import java.util.LinkedList;
import java.util.List;


public class MotorbikeRepository implements CrudRepository<Motorbike> {
    private final List<Motorbike> motorbikes;

    public MotorbikeRepository() {
        this.motorbikes = new LinkedList<>();
    }

    @Override
    public Motorbike getById(String id) {
        for (Motorbike motorbike : motorbikes) {
            if (motorbike.getId().equals(id)) {
                return motorbike;
            }
        }
        return null;
    }

    @Override
    public List<Motorbike> getAll() {
        return motorbikes;
    }

    @Override
    public boolean create(Motorbike motorbike) {
        return motorbikes.add(motorbike);
    }

    @Override
    public boolean create(List<Motorbike> motorbikeList) {
        return motorbikes.addAll(motorbikeList);
    }

    @Override
    public boolean update(Motorbike motorbike) {
        Motorbike newVersion = getById(motorbike.getId());
        if (newVersion != null) {
            newVersion.setManufacturer(motorbike.getManufacturer());
            newVersion.setModel(motorbike.getModel());
            newVersion.setPrice(motorbike.getPrice());
            newVersion.setLeanAngle(motorbike.getLeanAngle());
            return true;
        }
        return false;
    }

    @Override
    public List<Motorbike> delete(Motorbike motorbike) {
        motorbikes.remove(motorbike);
        return motorbikes;
    }

    @Override
    public boolean delete(String id) {
        return motorbikes.remove(getById(id));
    }
}
