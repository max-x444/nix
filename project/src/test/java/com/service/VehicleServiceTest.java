package com.service;

import com.model.constants.Manufacturer;
import com.model.vehicle.Engine;
import com.model.vehicle.Motorbike;
import com.repository.collection.MotorbikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;


class VehicleServiceTest {
    private VehicleService<Motorbike> target;
    private MotorbikeRepository motorbikeRepository;

    @BeforeEach
    void setUp() {
        motorbikeRepository = Mockito.mock(MotorbikeRepository.class);
        target = new VehicleService<>(motorbikeRepository) {
        };
    }

    @Test
    void create_default_motorbike() {
        Motorbike actual = createSimpleMotorbike();
        target.save(actual);
        Mockito.verify(motorbikeRepository).save(argThat((ArgumentMatcher<Motorbike>)
                motorbike -> actual.getModel().equals(motorbike.getModel())));
    }

    @Test
    void save_successfully() {
        Mockito.when(motorbikeRepository.save(any(Motorbike.class))).thenReturn(true);
        Assertions.assertTrue(target.save(createSimpleMotorbike()));
        Mockito.verify(motorbikeRepository).save(any(Motorbike.class));
    }

    @Test
    void save_fail() {
        Mockito.when(motorbikeRepository.save(any(Motorbike.class))).thenReturn(false);
        Assertions.assertFalse(target.save(createSimpleMotorbike()));
        Mockito.verify(motorbikeRepository, Mockito.times(1)).save(any(Motorbike.class));
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.delete(anyString()));
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

    private Motorbike createSimpleMotorbike() {
        final String randomId = UUID.randomUUID().toString();
        return new Motorbike(
                randomId,
                "Model",
                Manufacturer.BMW,
                BigDecimal.ZERO,
                0.0,
                0,
                LocalDateTime.now(),
                "$",
                new Engine(randomId, 0, "Brand"));
    }
}