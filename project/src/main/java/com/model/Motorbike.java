package com.model;

import java.math.BigDecimal;

public class Motorbike extends Vehicle {
    private Double leanAngle;

    public Motorbike(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle) {
        super(model, manufacturer, price);
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
                ", model='" + model + '\'' +
                ", price=" + price +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
