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
    private MotorbikeRepository motorbikeRepository = new MotorbikeRepository();
    private Motorbike motorbike;

    public MotorbikeService() {

    }

    public MotorbikeService(MotorbikeRepository motorbikeRepository) {
        this.motorbikeRepository = motorbikeRepository;
    }

    public void print(Motorbike motorbike) {
        LOGGER.info("Print info about motorbike: {}", motorbike);
    }

    public Motorbike create(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle) {
        return new Motorbike(model, manufacturer, price, leanAngle);
    }

    public boolean save(Motorbike motorbike) {
        return motorbikeRepository.create(motorbike);
    }

    public boolean update(Motorbike motorbike) {
        return motorbikeRepository.update(motorbike);
    }

    public List<Motorbike> delete(Motorbike motorbike) {
        return motorbikeRepository.delete(motorbike);
    }

    public boolean delete(String id) {
        return motorbikeRepository.delete(id);
    }

    public void orElse(String id) {
        motorbike = motorbikeRepository.findById(id).orElse(createDefault());
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void orElseThrow(String id) {
        motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find motorbike with id " + id));
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void or(String id) {
        Optional<Motorbike> optionalMotorbike = motorbikeRepository.findById(id).or(() -> Optional.of(createDefault()));
        optionalMotorbike.ifPresent(motorbike -> LOGGER.info("Model motorbike: {}", motorbike.getModel()));
    }

    public void orElseGet(String id) {
        motorbike = motorbikeRepository.findById(id).orElseGet(this::createDefault);
        LOGGER.info("Model motorbike: {}", motorbike.getModel());
    }

    public void filter(String id) {
        motorbikeRepository.findById(id)
                .filter(motorbike -> motorbike.getManufacturer().equals(Manufacturer.BMW))
                .ifPresent(motorbike -> LOGGER.info("Manufacturer match found: {}", motorbike.getManufacturer()));
    }

    public void map(String id) {
        motorbikeRepository.findById(id)
                .map(Vehicle::getModel)
                .ifPresent(model -> LOGGER.info("Model found: {}", model));
    }

    public void ifPresentOrElse(String id) {
        motorbikeRepository.findById(id)
                .ifPresentOrElse(
                        motorbike -> LOGGER.info("Model motorbike: {}", motorbike.getModel()),
                        () -> LOGGER.info("Cannot find motorbike with id: {}", id)
                );
    }

    private Motorbike createDefault() {
        return new Motorbike("Default model", Manufacturer.MERCEDES, BigDecimal.ZERO, 0.0);
    }
}
