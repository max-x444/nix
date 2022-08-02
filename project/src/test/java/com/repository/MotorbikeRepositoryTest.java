package com.repository;

import com.model.Engine;
import com.model.Manufacturer;
import com.model.Motorbike;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class MotorbikeRepositoryTest {
    private MotorbikeRepository target;
    private Motorbike motorbike;

    @BeforeEach
    void setUp() {
        target = new MotorbikeRepository();
        motorbike = createSimpleMotorbike();
        target.create(motorbike);
    }

    @Test
    void findById_motorbike_found() {
        final Optional<Motorbike> actual = target.findById(motorbike.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(motorbike.getId(), actual.get().getId());
    }

    @Test
    void findById_motorbike_not_found() {
        final Optional<Motorbike> actual = target.findById("111");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void getAll_same_list() {
        final List<Motorbike> actual = target.getAll();
        Assertions.assertEquals(actual, target.getAll());
    }

    @Test
    void getAll_not_same_list() {
        final List<Motorbike> actual = new LinkedList<>();
        Assertions.assertNotEquals(actual, target.getAll());
    }

    @Test
    void create_motorbike_successfully() {
        Assertions.assertTrue(target.create(createSimpleMotorbike()));
    }

    @Test
    void create_motorbike_fail() {
        Assertions.assertFalse(target.create((Motorbike) null));
    }

    @Test
    void create_listMotorbike_successfully() {
        Assertions.assertTrue(target.create(List.of(createSimpleMotorbike())));
    }

    @Test
    void create_listMotorbike_fail() {
        Assertions.assertFalse(target.create(new LinkedList<>()));
    }

    @Test
    void update_has_changed() {
        motorbike.setPrice(BigDecimal.ONE);
        Assertions.assertTrue(target.update(motorbike));
    }

    @Test
    void update_has_not_changed() {
        Assertions.assertFalse(target.update(createSimpleMotorbike()));
    }

    @Test
    void delete_id_successfully() {
        Assertions.assertTrue(target.delete(motorbike.getId()));
    }

    @Test
    void delete_id_fail() {
        Assertions.assertFalse(target.delete("111"));
    }

    @Test
    void delete_motorbike_successfully() {
        Assertions.assertEquals(0, target.delete(motorbike).size());
    }

    @Test
    void delete_motorbike_fail() {
        Assertions.assertEquals(1, target.delete(createSimpleMotorbike()).size());
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