package com;

import com.model.Auto;
import com.service.AutoService;

import java.util.List;

public class Main {
    private static final AutoService AUTO_SERVICE = new AutoService();

    public static void main(String[] args) {
        final List<Auto> autos = AUTO_SERVICE.createAutos(10);
        AUTO_SERVICE.saveAutos(autos);
        AUTO_SERVICE.printAll();
        // TODO: 03/07/22 add test
    }
}