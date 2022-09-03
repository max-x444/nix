package com.model.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Detail {
    @Id
    @Column(name = "detail_id")
    private String id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public Detail(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", vehicleId='" + vehicle.getId() + '\'' +
                '}';
    }
}