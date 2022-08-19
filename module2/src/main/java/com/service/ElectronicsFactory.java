package com.service;

import com.exception.InvalidStringException;
import com.model.constants.Country;
import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import com.model.electronics.Electronics;
import com.model.electronics.Telephone;
import com.model.electronics.Television;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Map;

public final class ElectronicsFactory {
    private ElectronicsFactory() {
    }

    public static Electronics createElectronic(@NonNull final Map<String, String> map) throws InvalidStringException {
        try {
            switch (map.get("type")) {
                case "Telephone" -> {
                    return createTelephone(map);
                }
                case "Television" -> {
                    return createTelevision(map);
                }
                default -> throw new InvalidStringException("Invalid string: " + map.get("type"));
            }
        } catch (RuntimeException exception) {
            throw new InvalidStringException(exception.getMessage());
        }
    }

    private static Telephone createTelephone(@NonNull final Map<String, String> map) {
        return new Telephone.Builder()
                .setSeries(map.get("series"))
                .setScreenType(ScreenType.valueOf(map.get("screen type").toUpperCase()))
                .setPrice(BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.get("price")))))
                .setModel(Manufacture.valueOf(map.get("model").toUpperCase()))
                .build();
    }

    private static Television createTelevision(@NonNull final Map<String, String> map) {
        return new Television.Builder()
                .setSeries(map.get("series"))
                .setScreenType(ScreenType.valueOf(map.get("screen type").toUpperCase()))
                .setPrice(BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.get("price")))))
                .setDiagonal(Integer.parseInt(map.get("diagonal").toUpperCase()))
                .setCountry(Country.valueOf(map.get("country").toUpperCase()))
                .build();
    }
}