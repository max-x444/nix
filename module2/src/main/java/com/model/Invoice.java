package com.model;

import com.model.constants.Type;
import com.model.electronics.Electronics;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Invoice<T extends Electronics> {
    private List<T> electronics;
    private Customer customer;
    private Type type;
    private LocalDateTime created;

    public Invoice(List<T> electronics, Customer customer, Type type) {
        this.electronics = electronics;
        this.customer = customer;
        this.type = type;
        this.created = LocalDateTime.now();
    }
}