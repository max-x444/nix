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
import java.util.concurrent.atomic.AtomicBoolean;

public class MotorbikeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeService.class);
    private MotorbikeRepository motorbikeRepository = new MotorbikeRepository();

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

    public Motorbike findOrCreateDefault(String id) {
        Optional<Motorbike> optionalMotorbike = motorbikeRepository.findById(id).or(() -> Optional.of(createDefault()));
        optionalMotorbike.ifPresent(motorbike -> motorbikeRepository.delete(motorbike));
        return optionalMotorbike.orElseGet(this::createDefault);
    }

    public boolean findOrException(String id) {
        motorbikeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find motorbike with id " + id));
        return true;
    }

    public boolean checkManufacturerById(String id, Manufacturer searchManufacturer) {
        AtomicBoolean temp = new AtomicBoolean(false);
        motorbikeRepository.findById(id)
                .map(Vehicle::getManufacturer)
                .filter(m -> m.equals(searchManufacturer))
                .ifPresentOrElse(
                        manufacturer -> temp.set(true),
                        () -> temp.set(false)
                );
        return temp.get();
    }

    private Motorbike createDefault() {
        return new Motorbike("Default model", Manufacturer.MERCEDES, BigDecimal.ZERO, 0.0);
    }
}
