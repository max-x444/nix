package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Engine {
    private int volume;
    private String brand;

    public Engine(int volume, String brand) {
        this.volume = volume;
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "volume=" + volume +
                ", brand='" + brand + '\'' +
                '}';
    }
}
