package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;

import java.math.BigDecimal;

public class Airplane extends Vehicle {
    private int numberOfPassengerSeats;

    public Airplane(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        super(model, manufacturer, price, count, VehicleType.AIRPLANE);
        this.numberOfPassengerSeats = numberOfPassengerSeats;
    }

    public int getNumberOfPassengerSeats() {
        return numberOfPassengerSeats;
    }

    public void setNumberOfPassengerSeats(int numberOfPassengerSeats) {
        this.numberOfPassengerSeats = numberOfPassengerSeats;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "numberOfPassengerSeats=" + numberOfPassengerSeats +
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