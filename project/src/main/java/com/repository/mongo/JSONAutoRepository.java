package com.repository.mongo;

import com.model.vehicle.Auto;

public class JSONAutoRepository extends JSONRepository<Auto> {
    private static JSONAutoRepository instance;

    private JSONAutoRepository() {
        super(Auto.class);
    }

    public static JSONAutoRepository getInstance() {
        if (instance == null) {
            instance = new JSONAutoRepository();
        }
        return instance;
    }
}