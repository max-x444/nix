package com.service;

import com.model.annotations.MyAutowired;
import com.model.annotations.MySingleton;
import com.model.constants.Manufacturer;
import com.model.vehicle.Engine;
import com.model.vehicle.Motorbike;
import com.model.vehicle.Vehicle;
import com.repository.CrudRepository;
import com.repository.collection.MotorbikeRepository;
import com.repository.hibernate.JPAMotorbikeRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@MySingleton
public class MotorbikeService extends VehicleService<Motorbike> {
    private static MotorbikeService instance;

    @MyAutowired(MotorbikeRepository.class)
    public MotorbikeService(CrudRepository<Motorbike> crudRepository) {
        super(crudRepository);
    }

    public static MotorbikeService getInstance() {
        if (instance == null) {
            instance = new MotorbikeService(JPAMotorbikeRepository.getInstance());
        }
        return instance;
    }

    public Motorbike create(String id, String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count,
                            LocalDateTime created, String currency, Engine engine) {
        return new Motorbike(id, model, manufacturer, price, leanAngle, count, created, currency, engine);
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

    public Function<Map<String, Object>, Motorbike> function = map -> {
        final String randomId = UUID.randomUUID().toString();
        return new Motorbike(
                randomId,
                String.valueOf(map.getOrDefault("model", "Model")),
                Manufacturer.valueOf(String.valueOf(map.getOrDefault("manufacturer", Manufacturer.BMW))),
                BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.getOrDefault("price", BigDecimal.ZERO)))),
                Double.valueOf(String.valueOf(map.getOrDefault("leanAngle", 0.0))),
                Integer.parseInt(String.valueOf(map.getOrDefault("count", 0))),
                LocalDateTime.parse(String.valueOf(map.getOrDefault("created", LocalDateTime.now())),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
                String.valueOf(map.getOrDefault("currency", "$")),
                new Engine(randomId, Integer.parseInt(String.valueOf(map.getOrDefault("volume", 0))),
                        String.valueOf(map.getOrDefault("brand", "Brand")))
        );
    };

    private Motorbike createDefault() {
        final String randomId = UUID.randomUUID().toString();
        return new Motorbike(
                randomId,
                "Default model",
                Manufacturer.MERCEDES,
                BigDecimal.ZERO,
                0.0,
                0,
                LocalDateTime.now(),
                "$",
                new Engine(randomId, 0, "Brand"));
    }
}