package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Motorbike extends Vehicle {
    private LocalDateTime createdMotorbike;
    private Engine engine;
    private Double leanAngle;
    private String currency;


    public Motorbike(String id, String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count,
                     LocalDateTime createdMotorbike, String currency, Engine engine) {
        super(id, model, manufacturer, price, count, VehicleType.MOTORBIKE);
        this.leanAngle = leanAngle;
        this.createdMotorbike = createdMotorbike;
        this.currency = currency;
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Motorbike{" +
                "createdMotorbike=" + createdMotorbike +
                ", engine=" + engine +
                ", leanAngle=" + leanAngle +
                ", currency='" + currency + '\'' +
                ", id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", type=" + type +
                ", invoiceId='" + invoiceId + '\'' +
                ", details=" + details +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", count=" + count +
                '}';
    }
}