package com.repository;

import com.model.Motorbike;

import java.util.LinkedList;
import java.util.List;


public class MotorbikeRepository {
    private final List<Motorbike> motorbikes;

    public MotorbikeRepository() {
        this.motorbikes = new LinkedList<>();
    }

    public Motorbike getMotorbikeById(String id) {
        for (Motorbike motorbike : motorbikes) {
            if (motorbike.getId().equals(id)) {
                return motorbike;
            }
        }
        return null;
    }

    public List<Motorbike> getAll() {
        return motorbikes;
    }

    public Motorbike update(Motorbike motorbike) {
        Motorbike newVersion = getMotorbikeById(motorbike.getId());
        if (newVersion != null) {
            newVersion.setManufacturer(motorbike.getManufacturer());
            newVersion.setModel(motorbike.getModel());
            newVersion.setPrice(motorbike.getPrice());
            newVersion.setLeanAngle(motorbike.getLeanAngle());
            return newVersion;
        }
        return null;
    }

    public List<Motorbike> create(Motorbike motorbike) {
        motorbikes.add(motorbike);
        return motorbikes;
    }

    public List<Motorbike> delete(Motorbike motorbike) {
        motorbikes.remove(motorbike);
        return motorbikes;
    }

    public List<Motorbike> delete(String id) {
        motorbikes.remove(getMotorbikeById(id));
        return motorbikes;
    }
}
