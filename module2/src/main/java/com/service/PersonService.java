package com.service;

import com.model.Customer;

import java.util.Random;

public final class PersonService {
    private static final Random RANDOM = new Random();

    private PersonService() {
    }

    public static Customer create() {
        final int maxAge = 88;
        final int minAge = 12;
        return new Customer(getRandomEmail(), RANDOM.nextInt(maxAge) + minAge);
    }

    private static String getRandomEmail() {
        final StringBuilder stringBuilder = new StringBuilder();
        final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final int lengthChars = chars.length();
        final int maxLengthChars = 10;
        while (stringBuilder.length() < maxLengthChars) {
            stringBuilder.append(chars.charAt(RANDOM.nextInt(lengthChars)));
        }
        return stringBuilder.append("@gmail.com").toString();
    }
}
