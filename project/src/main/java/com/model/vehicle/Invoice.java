package com.model.vehicle;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Invoice {
    private String id;
    private LocalDateTime created;
    private List<Vehicle> vehicles; // TODO: 21/08/22 For simple implementation can be replaced to Auto

    public Invoice(String id, LocalDateTime localDateTime, List<Vehicle> vehicles) {
        this.id = id;
        this.created = localDateTime;
        this.vehicles = vehicles;
    }
}