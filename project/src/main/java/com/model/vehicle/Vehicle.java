package com.model.vehicle;

import com.model.constants.Manufacturer;
import com.model.constants.VehicleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    protected String id;
    @Enumerated(value = EnumType.STRING)
    protected Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    protected Invoice invoice;
    @Enumerated(value = EnumType.STRING)
    protected VehicleType type;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<Detail> details = new ArrayList<>();
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
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", type=" + type +
                ", details=" + details +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", count=" + count +
                '}';
    }
}