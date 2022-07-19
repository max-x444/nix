package com.utils;

import com.model.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Discount<T extends Vehicle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Discount.class);
    private final T value;

    public Discount(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public T getDiscount() {
        BigDecimal price = value.getPrice();
        LOGGER.info("Vehicle price before discount: {}", price);
        BigDecimal discount = BigDecimal.valueOf(10 + Math.random() * 21).setScale(0, RoundingMode.FLOOR);
        LOGGER.info("Discount percentage: {}%", discount);
        BigDecimal priceAfterDiscount = price.subtract(price.multiply(
                        discount.divide(BigDecimal.valueOf(100), 2, RoundingMode.FLOOR)))
                .setScale(0, RoundingMode.FLOOR);
        LOGGER.info("Vehicle price after discount: {}", priceAfterDiscount);
        value.setPrice(priceAfterDiscount);
        return value;
    }

    public <O extends Number> T increasePrice(O coefficient) {
        value.setPrice(value.getPrice().multiply(BigDecimal.valueOf(coefficient.doubleValue()))
                .setScale(0, RoundingMode.FLOOR));
        LOGGER.info("The cost of the vehicle has increased by {} times", coefficient);
        return value;
    }
}
