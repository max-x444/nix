package com.service;

import com.model.Airplane;
import com.model.Manufacturer;
import com.repository.AirplaneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class AirplaneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirplaneService.class);
    private static final AirplaneRepository AIRPLANE_REPOSITORY = new AirplaneRepository();

    public void print(Airplane airplane) {
        LOGGER.info("Print info about airplane: {}", airplane);
    }

    public void print(List<Airplane> airplaneList) {
        for (Airplane airplane : airplaneList) {
            LOGGER.info("Print info about airplane: {}", airplane);
        }
    }

    public Airplane create(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats) {
        return new Airplane(model, manufacturer, price, numberOfPassengerSeats);
    }

    public void save(Airplane motorbike) {
        AIRPLANE_REPOSITORY.create(motorbike);
    }

    public void update(Airplane airplane) {
        AIRPLANE_REPOSITORY.update(airplane);
    }

    public List<Airplane> delete(Airplane airplane) {
        return AIRPLANE_REPOSITORY.delete(airplane);
    }

    public List<Airplane> delete(String id) {
        return AIRPLANE_REPOSITORY.delete(id);
    }
}
