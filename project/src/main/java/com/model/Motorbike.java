package com.model;

import java.math.BigDecimal;

public class Motorbike extends Vehicle {
    private Double leanAngle;

    public Motorbike(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count) {
        super(model, manufacturer, price, count, VehicleType.MOTORBIKE);
        this.leanAngle = leanAngle;
    }

    public Double getLeanAngle() {
        return leanAngle;
    }

    public void setLeanAngle(Double leanAngle) {
        this.leanAngle = leanAngle;
    }

    @Override
    public String toString() {
        return "Motorbike{" +
                "leanAngle=" + leanAngle +
                ", id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", type=" + type +
                ", details=" + details +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", count=" + count +
                '}';
    }
}