package com.example;

import com.example.model.NotifiableProduct;
import com.example.model.Product;
import com.example.model.ProductBundle;
import com.example.repository.ProductRepository;
import com.example.utils.ProductUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductUtils utils = new ProductUtils(new ProductRepository());
        List<Product> products = new ArrayList<>();
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.add(utils.generateRandomProduct());
        products.forEach(it -> {
            if (it instanceof ProductBundle) {
                utils.saveProductBundle((ProductBundle) it);
            } else if (it instanceof NotifiableProduct) {
                utils.saveNotifiableProduct((NotifiableProduct) it);
            }
        });

        System.out.println(utils.getAll());
        System.out.println("notifications sent: " + utils.filterNotifiableProductsAndSendNotifications());
    }
}
