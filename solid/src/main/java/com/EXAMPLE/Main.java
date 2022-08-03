package com.EXAMPLE;

import com.EXAMPLE.moDEL.NotifiableProduct;
import com.EXAMPLE.moDEL.Product;
import com.EXAMPLE.moDEL.ProductBundle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductUtils utils = new ProductUtils();
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
