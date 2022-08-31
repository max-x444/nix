package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class Airplane extends Vehicle {
    @Column(name = "number_of_passenger_seats")
    private int numberOfPassengerSeats;

    public Airplane(String id, String model, Manufacturer manufacturer, BigDecimal price, int numberOfPassengerSeats,
                    int count) {
        super(id, model, manufacturer, price, count, VehicleType.AIRPLANE);
        this.numberOfPassengerSeats = numberOfPassengerSeats;
    }
}