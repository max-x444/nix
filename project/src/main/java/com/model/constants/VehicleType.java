package com.model.constants;

public enum VehicleType {
    AUTO("AUTO"),
    MOTORBIKE("MOTORBIKE"),
    AIRPLANE("AIRPLANE");

    private final String title;

    VehicleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}