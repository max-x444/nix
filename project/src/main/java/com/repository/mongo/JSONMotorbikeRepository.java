package com.repository.mongo;

import com.model.vehicle.Motorbike;

public class JSONMotorbikeRepository extends JSONRepository<Motorbike> {
    private static JSONMotorbikeRepository instance;

    private JSONMotorbikeRepository() {
        super(Motorbike.class);
    }

    public static JSONMotorbikeRepository getInstance() {
        if (instance == null) {
            instance = new JSONMotorbikeRepository();
        }
        return instance;
    }
}