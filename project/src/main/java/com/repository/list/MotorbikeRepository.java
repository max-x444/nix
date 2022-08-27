package com.repository.list;

import com.model.annotations.MySingleton;
import com.model.vehicle.Motorbike;
import com.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@MySingleton
public class MotorbikeRepository implements CrudRepository<Motorbike> {
    private static MotorbikeRepository instance;
    private final List<Motorbike> motorbikes;

    private MotorbikeRepository() {
        this.motorbikes = new LinkedList<>();
    }

    public static MotorbikeRepository getInstance() {
        if (instance == null) {
            instance = new MotorbikeRepository();
        }
        return instance;
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
    public boolean save(Motorbike motorbike) {
        if (motorbike == null) {
            return false;
        }
        return motorbikes.add(motorbike);
    }

    @Override
    public boolean save(List<Motorbike> motorbikeList) {
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
            foundedMotorbike.setCreated(motorbike.getCreated());
            foundedMotorbike.setCurrency(motorbike.getCurrency());
            foundedMotorbike.setEngine(motorbike.getEngine());
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
