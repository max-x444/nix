package com.service;

import com.model.Auto;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AutoService extends VehicleService<Auto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoService.class);
    private static final Random RANDOM = new Random();
    private static AutoService instance;

    public AutoService(CrudRepository<Auto> crudRepository) {
        super(crudRepository);
    }

    public static AutoService getInstance() {
        if (instance == null) {
            instance = new AutoService(new AutoRepository());
        }
        return instance;
    }

    public List<Auto> create(int count) {
        List<Auto> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final Auto auto = new Auto(
                    "Model-" + RANDOM.nextInt(1000),
                    getRandomManufacturer(),
                    BigDecimal.valueOf(RANDOM.nextInt(1000)),
                    "Model-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(9) + 1
            );
            result.add(auto);
            crudRepository.create(auto);
            LOGGER.debug("Created auto {}", auto.getId());
        }
        return result;
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }
}