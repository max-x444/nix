package com.model;

import java.math.BigDecimal;

public class Airplane extends Vehicle {
    private int numberOfPassengerSeats;

    public Airplane(String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats, int count) {
        super(model, manufacturer, price, count);
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
                ", model='" + model + '\'' +
                ", price=" + price +
                ", manufacturer=" + manufacturer +
                ", count=" + count +
                '}';
    }
}