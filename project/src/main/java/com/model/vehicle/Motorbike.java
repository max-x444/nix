package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class Motorbike extends Vehicle {
    @Column(name = "create_motorbike")
    private LocalDateTime createdMotorbike;
    @OneToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "engine_id")
    private Engine engine;
    @Column(name = "lean_angle")
    private Double leanAngle;
    private String currency;

    public Motorbike(String id, String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count,
                     LocalDateTime createdMotorbike, String currency, Engine engine) {
        super(id, model, manufacturer, price, count, VehicleType.MOTORBIKE);
        this.leanAngle = leanAngle;
        this.createdMotorbike = createdMotorbike;
        this.currency = currency;
        this.engine = engine;
    }
}