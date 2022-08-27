package com.model.vehicle;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Invoice {
    private String id;
    private LocalDateTime created;
    private List<Vehicle> vehicles;

    public Invoice(String id, LocalDateTime localDateTime, List<Vehicle> vehicles) {
        this.id = id;
        this.created = localDateTime;
        this.vehicles = vehicles;
    }
}