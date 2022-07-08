package com.service;

import com.model.Airplane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AirplaneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirplaneService.class);

    public void print(Airplane airplane) {
        LOGGER.info(String.valueOf(airplane));
    }

    public void print(List<Airplane> airplaneList) {
        for (Airplane airplane : airplaneList) {
            LOGGER.info(String.valueOf(airplane));
        }
    }
}
