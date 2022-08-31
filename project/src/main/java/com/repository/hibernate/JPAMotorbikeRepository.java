package com.repository.hibernate;

import com.model.vehicle.Motorbike;
import com.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class JPAMotorbikeRepository implements CrudRepository<Motorbike> {
    private static JPAMotorbikeRepository instance;
    private static JPAVehicleRepository jpaVehicleRepository;

    private JPAMotorbikeRepository() {
        jpaVehicleRepository = JPAVehicleRepository.getInstance();
    }

    public static JPAMotorbikeRepository getInstance() {
        if (instance == null) {
            instance = new JPAMotorbikeRepository();
        }
        return instance;
    }

    @Override
    public Optional<Motorbike> findById(String id) {
        return jpaVehicleRepository.findById(id)
                .map(value -> (Motorbike) value);
    }

    @Override
    public List<Motorbike> getAll() {
        return JPAVehicleRepository.entityManager.createQuery("SELECT m FROM Motorbike m", Motorbike.class).getResultList();
    }

    @Override
    public boolean save(Motorbike motorbike) {
        return jpaVehicleRepository.save(motorbike);
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
    public boolean update(Motorbike motorbike) {
        if (jpaVehicleRepository.update(motorbike)) {
            findById(motorbike.getId()).ifPresent(target -> {
                target.setLeanAngle(motorbike.getLeanAngle());
                target.setCurrency(motorbike.getCurrency());
                target.setCreatedMotorbike(motorbike.getCreatedMotorbike());
                target.setEngine(motorbike.getEngine());
                save(target);
            });
        }
        return true;
    }

    @Override
    public boolean delete(String id) {
        return jpaVehicleRepository.delete(id);
    }

    @Override
    public List<Motorbike> delete(Motorbike motorbike) {
        if (motorbike == null) {
            throw new IllegalArgumentException("Motorbike must not be null");
        }
        jpaVehicleRepository.delete(motorbike.getId());
        return getAll();
    }
}