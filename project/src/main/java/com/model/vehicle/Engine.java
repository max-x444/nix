package com.model.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Engine {
    @Id
    @Column(name = "engine_id")
    private String id;
    private int volume;
    private String brand;

    public Engine(String id, int volume, String brand) {
        this.id = id;
        this.volume = volume;
        this.brand = brand;
    }
}