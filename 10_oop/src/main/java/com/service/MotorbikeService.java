package com.service;

import com.model.Manufacturer;
import com.model.Motorbike;
import com.repository.MotorbikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class MotorbikeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeService.class);
    private static final MotorbikeRepository MOTORBIKE_REPOSITORY = new MotorbikeRepository();

    public void print(Motorbike motorbike) {
        LOGGER.info("Print info about motorbike: {}", motorbike);
    }

    public void print(List<Motorbike> motorbikeList) {
        for (Motorbike motorbike : motorbikeList) {
            LOGGER.info("Print info about motorbike: {}", motorbike);
        }
    }

    public Motorbike create(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle) {
        return new Motorbike(model, manufacturer, price, leanAngle);
    }

    public void save(Motorbike motorbike) {
        MOTORBIKE_REPOSITORY.create(motorbike);
    }

    public void update(Motorbike motorbike) {
        MOTORBIKE_REPOSITORY.update(motorbike);
    }

    public List<Motorbike> delete(Motorbike motorbike) {
        return MOTORBIKE_REPOSITORY.delete(motorbike);
    }

    public List<Motorbike> delete(String id) {
        return MOTORBIKE_REPOSITORY.delete(id);
    }
}
