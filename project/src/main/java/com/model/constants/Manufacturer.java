package com.model.constants;

public enum Manufacturer {
    KIA("KIA"),
    BMW("BMW"),
    AUDI("AUDI"),
    MERCEDES("MERCEDES");

    private final String title;

    Manufacturer(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}