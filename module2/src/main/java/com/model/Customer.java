package com.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
    private String id;
    private String email;
    private int age;

    public Customer(String email, int age) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.age = age;
    }
}