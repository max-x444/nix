package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Motorbike extends Vehicle {
    private LocalDateTime created;
    private Engine engine;
    private Double leanAngle;
    private String currency;


    public Motorbike(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count,
                     LocalDateTime created, String currency, Engine engine) {
        super(model, manufacturer, price, count, VehicleType.MOTORBIKE);
        this.leanAngle = leanAngle;
        this.created = created;
        this.currency = currency;
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Motorbike{" +
                "created=" + created +
                ", engine=" + engine +
                ", leanAngle=" + leanAngle +
                ", currency='" + currency + '\'' +
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