package com.repository.hibernate;

import com.model.vehicle.Airplane;
import com.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public class JPAAirplaneRepository implements CrudRepository<Airplane> {
    private static JPAAirplaneRepository instance;
    private static JPAVehicleRepository jpaVehicleRepository;

    private JPAAirplaneRepository() {
        jpaVehicleRepository = JPAVehicleRepository.getInstance();
    }

    public static JPAAirplaneRepository getInstance() {
        if (instance == null) {
            instance = new JPAAirplaneRepository();
        }
        return instance;
    }

    @Override
    public Optional<Airplane> findById(String id) {
        return jpaVehicleRepository.findById(id)
                .map(value -> (Airplane) value);
    }

    @Override
    public List<Airplane> getAll() {
        return JPAVehicleRepository.entityManager.createQuery("SELECT a FROM Airplane a", Airplane.class).getResultList();
    }

    @Override
    public boolean save(Airplane airplane) {
        return jpaVehicleRepository.save(airplane);
    }

    @Override
    public boolean save(List<Airplane> airplanes) {
        if (airplanes.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        airplanes.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Airplane airplane) {
        if (jpaVehicleRepository.update(airplane)) {
            findById(airplane.getId()).ifPresent(target -> {
                target.setNumberOfPassengerSeats(airplane.getNumberOfPassengerSeats());
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
    public List<Airplane> delete(Airplane airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("Airplane must not be null");
        }
        jpaVehicleRepository.delete(airplane.getId());
        return getAll();
    }
}