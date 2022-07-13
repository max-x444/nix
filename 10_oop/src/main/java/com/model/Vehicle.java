package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public abstract class Vehicle {
    protected final String id;
    protected String model;
    protected BigDecimal price;
    protected Manufacturer manufacturer;

    protected Vehicle(String model, Manufacturer manufacturer, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
    }
}
