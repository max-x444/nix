package com.repository.hibernate;

import com.model.vehicle.Auto;
import com.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class JPAAutoRepository implements CrudRepository<Auto> {
    private static JPAAutoRepository instance;
    private static JPAVehicleRepository jpaVehicleRepository;

    private JPAAutoRepository() {
        jpaVehicleRepository = JPAVehicleRepository.getInstance();
    }

    public static JPAAutoRepository getInstance() {
        if (instance == null) {
            instance = new JPAAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<Auto> findById(String id) {
        return jpaVehicleRepository.findById(id)
                .map(value -> (Auto) value);
    }

    @Override
    public List<Auto> getAll() {
        return JPAVehicleRepository.entityManager.createQuery("SELECT a FROM Auto a", Auto.class).getResultList();
    }

    @Override
    public boolean save(Auto auto) {
        return jpaVehicleRepository.save(auto);
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
    public boolean update(Auto auto) {
        if (jpaVehicleRepository.update(auto)) {
            findById(auto.getId()).ifPresent(target -> {
                target.setBodyType(auto.getBodyType());
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
    public List<Auto> delete(Auto auto) {
        if (auto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        jpaVehicleRepository.delete(auto.getId());
        return getAll();
    }
}