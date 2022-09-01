package com.repository.hibernate;

import com.config.HibernateFactoryUtil;
import com.model.vehicle.Vehicle;
import com.repository.CrudRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JPAVehicleRepository implements CrudRepository<Vehicle> {
    private static JPAVehicleRepository instance;
    protected static EntityManager entityManager;

    private JPAVehicleRepository() {
        entityManager = HibernateFactoryUtil.getEntityManager();
    }

    public static JPAVehicleRepository getInstance() {
        if (instance == null) {
            instance = new JPAVehicleRepository();
        }
        return instance;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        final Vehicle vehicle = entityManager.find(Vehicle.class, id);
        return (vehicle != null) ? Optional.of(vehicle) : Optional.empty();
    }

    @Override
    public List<Vehicle> getAll() {
        return entityManager.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
    }

    @Override
    public boolean save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        entityManager.getTransaction().begin();
        entityManager.merge(vehicle);
        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
        return true;
    }

    @Override
    public boolean save(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        vehicles.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        findById(vehicle.getId()).ifPresent(target -> {
            target.setDetails(vehicle.getDetails());
            target.setCount(vehicle.getCount());
            target.setManufacturer(vehicle.getManufacturer());
            target.setModel(vehicle.getModel());
            target.setPrice(vehicle.getPrice());
            target.setType(vehicle.getType());
            save(target);
        });
        return true;
    }

    @Override
    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        findById(id).ifPresent(vehicle -> {
            entityManager.getTransaction().begin();
            entityManager.remove(vehicle);
            entityManager.flush();
            entityManager.clear();
            entityManager.getTransaction().commit();
        });
        return true;
    }

    @Override
    public List<Vehicle> delete(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        delete(vehicle.getId());
        return getAll();
    }
}