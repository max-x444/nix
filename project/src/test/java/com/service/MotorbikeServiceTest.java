package com.service;

import com.model.Manufacturer;
import com.model.Motorbike;
import com.repository.MotorbikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

class MotorbikeServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeServiceTest.class);
    private MotorbikeService target;
    private MotorbikeRepository motorbikeRepository;

    @BeforeEach
    void setUp() {
        motorbikeRepository = Mockito.mock(MotorbikeRepository.class);
        target = new MotorbikeService(motorbikeRepository);
    }

    @Test
    void create_default_motorbike() {
        Motorbike actual = createSimpleMotorbike();
        target.save(actual);
        Mockito.verify(motorbikeRepository).create(argThat((ArgumentMatcher<Motorbike>)
                motorbike -> actual.getModel().equals(motorbike.getModel())));
    }

    @Test
    void save_successfully() {
        Mockito.when(motorbikeRepository.create(any(Motorbike.class))).thenReturn(true);
        Assertions.assertTrue(target.save(createSimpleMotorbike()));
        Mockito.verify(motorbikeRepository).create(any(Motorbike.class));
    }

    @Test
    void save_fail() {
        Mockito.when(motorbikeRepository.create(any(Motorbike.class))).thenReturn(false);
        Assertions.assertFalse(target.save(createSimpleMotorbike()));
        Mockito.verify(motorbikeRepository, Mockito.times(1)).create(any(Motorbike.class));
    }

    @Test
    void update_successfully_if_transfer_motorbike() {
        ArgumentCaptor<Motorbike> argumentCaptor = ArgumentCaptor.forClass(Motorbike.class);
        target.update(createSimpleMotorbike());
        Mockito.verify(motorbikeRepository).update(argumentCaptor.capture());
        Assertions.assertEquals(Motorbike.class, argumentCaptor.getValue().getClass());
    }

    @Test
    void update_fail_if_callRealMethod() {
        Mockito.when(motorbikeRepository.update(any(Motorbike.class))).thenReturn(true);
        Mockito.when(motorbikeRepository.update(any(Motorbike.class))).thenCallRealMethod();
        Assertions.assertFalse(target.update(createSimpleMotorbike()));
    }

    @Test
    void delete_id_successfully() {
        Mockito.when(motorbikeRepository.delete(anyString())).thenReturn(true);
        Assertions.assertTrue(target.delete(anyString()));
    }

    @Test
    void delete_id_fail() {
        Mockito.when(motorbikeRepository.delete(anyString())).thenThrow(
                new IllegalArgumentException("Cannot find motorbike with this id"));
        try {
            target.delete(anyString());
        } catch (IllegalArgumentException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    @Test
    void delete_listMotorbike_successfully() {
        Mockito.when(motorbikeRepository.delete(any(Motorbike.class))).thenReturn(new LinkedList<>());
        Assertions.assertEquals(0, target.delete(any(Motorbike.class)).size());
    }

    @Test
    void delete_listMotorbike_fail() {
        Mockito.when(motorbikeRepository.delete(any(Motorbike.class))).thenReturn(List.of(createSimpleMotorbike()));
        Assertions.assertEquals(1, target.delete(createSimpleMotorbike()).size());
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
        try {
            Assertions.assertFalse(target.findOrException(anyString()));
        } catch (IllegalArgumentException exception) {
            LOGGER.error(exception.getMessage());
        }
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
        return new Motorbike("Model", Manufacturer.BMW, BigDecimal.ZERO, 0.0);
    }
}