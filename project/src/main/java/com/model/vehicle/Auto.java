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
public class Auto extends Vehicle {
    @Column(name = "body_type")
    private String bodyType;

    public Auto(String id, String model, Manufacturer manufacturer, BigDecimal price, String bodyType, int count) {
        super(id, model, manufacturer, price, count, VehicleType.AUTO);
        this.bodyType = bodyType;
    }
}