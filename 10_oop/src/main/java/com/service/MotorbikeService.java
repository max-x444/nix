package com.service;

import com.model.Motorbike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MotorbikeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeService.class);

    public void print(Motorbike motorbike) {
        LOGGER.info(String.valueOf(motorbike));
    }

    public void print(List<Motorbike> motorbikeList) {
        for (Motorbike motorbike : motorbikeList) {
            LOGGER.info(String.valueOf(motorbike));
        }
    }
}
