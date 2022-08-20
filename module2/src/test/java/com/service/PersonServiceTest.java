package com.service;

import com.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PersonServiceTest {
    @Test
    void create_unique_Customer() {
        final Customer firstCustomer = PersonService.create();
        final Customer secondCustomer = PersonService.create();
        Assertions.assertNotEquals(firstCustomer, secondCustomer);
    }
}