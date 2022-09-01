package com.repository;

import com.model.constants.Manufacturer;
import com.model.vehicle.Engine;
import com.model.vehicle.Motorbike;
import com.repository.collection.MotorbikeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class MotorbikeRepositoryTest {
    private static final MotorbikeRepository TARGET = MotorbikeRepository.getInstance();
    private Motorbike motorbike;

    @BeforeEach
    void setUp() {
        motorbike = createSimpleMotorbike();
        TARGET.save(motorbike);
    }

    @Test
    void findById_motorbike_found() {
        final Optional<Motorbike> actual = TARGET.findById(motorbike.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(motorbike.getId(), actual.get().getId());
    }

    @Test
    void findById_motorbike_not_found() {
        final Optional<Motorbike> actual = TARGET.findById("111");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void getAll_same_list() {
        final List<Motorbike> actual = TARGET.getAll();
        Assertions.assertEquals(actual, TARGET.getAll());
    }

    @Test
    void getAll_not_same_list() {
        final List<Motorbike> actual = new LinkedList<>();
        Assertions.assertNotEquals(actual, TARGET.getAll());
    }

    @Test
    void create_motorbike_successfully() {
        Assertions.assertTrue(TARGET.save(createSimpleMotorbike()));
    }

    @Test
    void create_motorbike_fail() {
        Assertions.assertFalse(TARGET.save((Motorbike) null));
    }

    @Test
    void create_listMotorbike_successfully() {
        Assertions.assertTrue(TARGET.save(List.of(createSimpleMotorbike())));
    }

    @Test
    void create_listMotorbike_fail() {
        Assertions.assertFalse(TARGET.save(new LinkedList<>()));
    }

    @Test
    void update_has_changed() {
        motorbike.setPrice(BigDecimal.ONE);
        Assertions.assertTrue(TARGET.update(motorbike));
    }

    @Test
    void update_has_not_changed() {
        Assertions.assertFalse(TARGET.update(createSimpleMotorbike()));
    }

    @Test
    void delete_id_successfully() {
        Assertions.assertTrue(TARGET.delete(motorbike.getId()));
    }

    @Test
    void delete_id_fail() {
        Assertions.assertFalse(TARGET.delete("111"));
    }

    @Test
    void delete_motorbike_successfully() {
        Assertions.assertEquals(TARGET.getAll().size() - 1, TARGET.delete(motorbike).size());
    }

    @Test
    void delete_motorbike_fail() {
        Assertions.assertNotEquals(TARGET.getAll().size(), TARGET.delete(motorbike).size());
    }

    private Motorbike createSimpleMotorbike() {
        return new Motorbike(
                UUID.randomUUID().toString(),
                "Model",
                Manufacturer.BMW,
                BigDecimal.ZERO,
                0.0,
                0,
                LocalDateTime.now(),
                "$",
                new Engine(UUID.randomUUID().toString(), 0, "Brand"));
    }
}