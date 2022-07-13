package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Auto extends Vehicle {
    private String bodyType;

    public Auto(String model, Manufacturer manufacturer, BigDecimal price, String bodyType) {
        super(model, manufacturer, price);
        this.bodyType = bodyType;
    }

    @Override
    public String toString() {
        return "Auto{" +
                "bodyType='" + bodyType + '\'' +
                ", id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
