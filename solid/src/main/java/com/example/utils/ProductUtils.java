package com.example.utils;

import com.example.model.NotifiableProduct;
import com.example.model.Product;
import com.example.model.ProductBundle;
import com.example.repository.ProductRepository;

import java.util.List;
import java.util.Random;

public class ProductUtils {
    private final ProductRepository repository;

    public ProductUtils(ProductRepository repository) {
        this.repository = repository;
    }

    public void saveNotifiableProduct(NotifiableProduct product) {
        repository.save(product);
    }

    public void saveProductBundle(ProductBundle product) {
        repository.save(product);
    }

    public int filterNotifiableProductsAndSendNotifications() {
        int notifications = 0;
        for (Product product : getAll()) {
            if (product.getClass().getSuperclass().getSimpleName().equals("Product")) {
                notifications++;
            }
        }
        return notifications;
    }

    public List<Product> getAll() {
        return repository.getAll();
    }

    public Product generateRandomProduct() {
        final Random random = new Random();
        if (random.nextBoolean()) {
            try {
                return new ProductBundle.Builder()
                        .setId(random.nextLong())
                        .setAvailable(random.nextBoolean())
                        .setTitle(random.nextFloat() + "" + random.nextDouble())
                        .setPrice(random.nextDouble())
                        .setChannel(random.nextBoolean() + "" + random.nextDouble())
                        .setAmount(random.nextInt(15))
                        .build();
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                return null;
            }
        } else {
            NotifiableProduct product = new NotifiableProduct();
            product.setId(random.nextLong());
            product.setTitle(random.nextFloat() + "" + random.nextDouble());
            product.setAvailable(random.nextBoolean());
            product.setChannel(random.nextBoolean() + "" + random.nextDouble());
            product.setPrice(random.nextDouble());
            return product;
        }
    }
}
