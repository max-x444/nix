package com.service;

import com.model.Engine;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.repository.MotorbikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class MotorbikeServiceTest {
    private MotorbikeService target;
    private MotorbikeRepository motorbikeRepository;

    @BeforeEach
    void setUp() {
        motorbikeRepository = Mockito.mock(MotorbikeRepository.class);
        target = new MotorbikeService(motorbikeRepository);
    }

    @Test
    void findOrCreateDefault_find() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.of(createSimpleMotorbike()));
        Assertions.assertEquals(Motorbike.class, target.findOrCreateDefault(anyString()).getClass());
    }

    @Test
    void findOrCreateDefault_create_default() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.empty());
        Mockito.when(motorbikeRepository.delete(any(Motorbike.class))).thenReturn(new LinkedList<>());
        Assertions.assertEquals(Motorbike.class, target.findOrCreateDefault(createSimpleMotorbike().getId()).getClass());
    }

    @Test
    void findOrException_found() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.of(createSimpleMotorbike()));
        Assertions.assertTrue(target.findOrException(anyString()));
    }

    @Test
    void findOrException_not_found() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.findOrException(anyString()));
    }

    @Test
    void checkManufacturerById_match_found() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.of(createSimpleMotorbike()));
        Assertions.assertTrue(target.checkManufacturerById(anyString(), Manufacturer.BMW));
    }

    @Test
    void checkManufacturerById_no_match_found() {
        Mockito.when(motorbikeRepository.findById(anyString())).thenReturn(Optional.of(createSimpleMotorbike()));
        Assertions.assertFalse(target.checkManufacturerById(anyString(), Manufacturer.MERCEDES));
    }

    private Motorbike createSimpleMotorbike() {
        return new Motorbike(
                "Model",
                Manufacturer.BMW,
                BigDecimal.ZERO,
                0.0,
                0,
                LocalDateTime.now(),
                "$",
                new Engine(0, "Brand"));
    }
}