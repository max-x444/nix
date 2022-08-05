package com.service;

import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.repository.CrudRepository;
import com.repository.MotorbikeRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class MotorbikeService extends VehicleService<Motorbike> {
    private static MotorbikeService instance;

    public MotorbikeService(CrudRepository<Motorbike> crudRepository) {
        super(crudRepository);
    }

    public static MotorbikeService getInstance() {
        if (instance == null) {
            instance = new MotorbikeService(new MotorbikeRepository());
        }
        return instance;
    }

    public Motorbike create(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count) {
        Motorbike motorbike = new Motorbike(model, manufacturer, price, leanAngle, count);
        crudRepository.create(motorbike);
        return motorbike;
    }

    public Motorbike findOrCreateDefault(String id) {
        Optional<Motorbike> optionalMotorbike = crudRepository.findById(id).or(() -> Optional.of(createDefault()));
        optionalMotorbike.ifPresent(motorbike -> crudRepository.delete(motorbike));
        return optionalMotorbike.orElseGet(this::createDefault);
    }

    public boolean findOrException(String id) {
        crudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find motorbike with id " + id));
        return true;
    }

    public boolean checkManufacturerById(String id, Manufacturer searchManufacturer) {
        AtomicBoolean temp = new AtomicBoolean(false);
        crudRepository.findById(id)
                .map(Vehicle::getManufacturer)
                .filter(m -> m.equals(searchManufacturer))
                .ifPresentOrElse(
                        manufacturer -> temp.set(true),
                        () -> temp.set(false)
                );
        return temp.get();
    }

    public Function<Map<String, Object>, Motorbike> function = map -> new Motorbike(
            (String) map.getOrDefault("model", "Model"),
            (Manufacturer) map.getOrDefault("manufacturer", Manufacturer.BMW),
            (BigDecimal) map.getOrDefault("price", BigDecimal.ZERO),
            (Double) map.getOrDefault("leanAngle", 0.0),
            (int) map.getOrDefault("count", 0)
    );

    private Motorbike createDefault() {
        return new Motorbike("Default model", Manufacturer.MERCEDES, BigDecimal.ZERO, 0.0, 0);
    }
}