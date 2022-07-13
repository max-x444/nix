package com.service;

import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.repository.MotorbikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MotorbikeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeService.class);
    private static final MotorbikeRepository MOTORBIKE_REPOSITORY = new MotorbikeRepository();
    private Motorbike motorbike;

    public void print(Motorbike motorbike) {
        LOGGER.info("Print info about motorbike: {}", motorbike);
    }

    public Motorbike create(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle) {
        return new Motorbike(model, manufacturer, price, leanAngle);
    }

    public boolean save(Motorbike motorbike) {
        return MOTORBIKE_REPOSITORY.create(motorbike);
    }

    public boolean update(Motorbike motorbike) {
        return MOTORBIKE_REPOSITORY.update(motorbike);
    }

    public List<Motorbike> delete(Motorbike motorbike) {
        return MOTORBIKE_REPOSITORY.delete(motorbike);
    }

    public boolean delete(String id) {
        return MOTORBIKE_REPOSITORY.delete(id);
    }

    public void orElse(String id) {
        motorbike = MOTORBIKE_REPOSITORY.findById(id).orElse(createDefault());
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void orElseThrow(String id) {
        motorbike = MOTORBIKE_REPOSITORY.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find motorbike with id " + id));
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void or(String id) {
        Optional<Motorbike> optionalMotorbike = MOTORBIKE_REPOSITORY.findById(id).or(() -> Optional.of(createDefault()));
        optionalMotorbike.ifPresent(motorbike -> LOGGER.info("Model motorbike: {}", motorbike.getModel()));
    }

    public void orElseGet(String id) {
        motorbike = MOTORBIKE_REPOSITORY.findById(id).orElseGet(this::createDefault);
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void filter(String id) {
        MOTORBIKE_REPOSITORY.findById(id)
                .filter(motorbike -> motorbike.getManufacturer().equals(Manufacturer.BMW))
                .ifPresent(motorbike -> LOGGER.info("Manufacturer match found: {}", motorbike.getManufacturer()));
    }

    public void map(String id) {
        MOTORBIKE_REPOSITORY.findById(id)
                .map(Vehicle::getModel)
                .ifPresent(model -> LOGGER.info("Model found: {}", model));
    }

    public void ifPresentOrElse(String id) {
        MOTORBIKE_REPOSITORY.findById(id)
                .ifPresentOrElse(
                        motorbike -> LOGGER.info("Model motorbike: {}", motorbike.getModel()),
                        () -> LOGGER.info("Cannot find motorbike with id: {}", id)
                );
    }

    private Motorbike createDefault() {
        return new Motorbike("Default model", Manufacturer.MERCEDES, BigDecimal.ZERO, 0.0);
    }
}
