package com.model.vehicle;

import com.google.gson.annotations.SerializedName;
import com.interfaces.ContainIdAble;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice implements ContainIdAble {
    @Id
    @Column(name = "invoice_id")
    @SerializedName("_id")
    private String id;
    private LocalDateTime created;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Vehicle> vehicles = new LinkedHashSet<>();
}