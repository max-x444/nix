package com.repository.mongo;

import com.model.vehicle.Airplane;

public class JSONAirplaneRepository extends JSONRepository<Airplane> {
    private static JSONAirplaneRepository instance;

    private JSONAirplaneRepository() {
        super(Airplane.class);
    }

    public static JSONAirplaneRepository getInstance() {
        if (instance == null) {
            instance = new JSONAirplaneRepository();
        }
        return instance;
    }
}