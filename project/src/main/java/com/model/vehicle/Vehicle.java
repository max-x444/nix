package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public abstract class Vehicle {
    protected final String id;
    protected Manufacturer manufacturer;
    protected VehicleType type;
    protected Invoice invoice;
    protected List<String> details = new ArrayList<>();
    protected BigDecimal price;
    protected String model;
    protected int count;

    protected Vehicle(String id, String model, Manufacturer manufacturer, BigDecimal price, int count, VehicleType type) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.count = count;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return model.equals(vehicle.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }
}