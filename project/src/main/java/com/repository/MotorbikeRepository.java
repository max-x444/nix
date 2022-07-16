package com.repository;

import com.model.Motorbike;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class MotorbikeRepository implements CrudRepository<Motorbike> {
    private final List<Motorbike> motorbikes;

    public MotorbikeRepository() {
        this.motorbikes = new LinkedList<>();
    }

    @Override
    public Optional<Motorbike> findById(String id) {
        for (Motorbike motorbike : motorbikes) {
            if (motorbike.getId().equals(id)) {
                return Optional.of(motorbike);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Motorbike> getAll() {
        return motorbikes;
    }

    @Override
    public boolean create(Motorbike motorbike) {
        if (motorbike == null) {
            return false;
        }
        return motorbikes.add(motorbike);
    }

    @Override
    public boolean create(List<Motorbike> motorbikeList) {
        if (motorbikeList.isEmpty()) {
            return false;
        }
        return motorbikes.addAll(motorbikeList);
    }

    @Override
    public boolean update(Motorbike motorbike) {
        final Optional<Motorbike> founded = findById(motorbike.getId());
        if (founded.isPresent()) {
            Motorbike foundedMotorbike = founded.get();
            foundedMotorbike.setManufacturer(motorbike.getManufacturer());
            foundedMotorbike.setModel(motorbike.getModel());
            foundedMotorbike.setPrice(motorbike.getPrice());
            foundedMotorbike.setLeanAngle(motorbike.getLeanAngle());
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
        return findById(id).isPresent() && motorbikes.remove(findById(id).get());
    }
}
