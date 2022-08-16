package com.model;

import com.model.constants.Type;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "Invoice{" +
                "electronics=" + electronics +
                ", customer=" + customer +
                ", type=" + type +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice<?> invoice = (Invoice<?>) o;
        return Objects.equals(electronics, invoice.electronics) && Objects.equals(customer, invoice.customer)
                && type == invoice.type && Objects.equals(created, invoice.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electronics, customer, type, created);
    }
}
