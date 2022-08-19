package com.service;

import com.exception.InvalidStringException;
import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import com.model.electronics.Telephone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ElectronicsFactoryTest {
    private Map<String, String> map;
    private Telephone telephone;

    @BeforeEach
    void setUp() {
        createElectronicsMap();
        telephone = createTelephone();
    }

    @Test
    void createElectronic_is_correct() throws InvalidStringException {
        try (final MockedStatic<UUID> mockUUID = Mockito.mockStatic(UUID.class)) {
            final UUID uuid = Mockito.mock(UUID.class);
            BDDMockito.given(UUID.randomUUID()).willReturn(uuid);
            BDDMockito.given(uuid.toString()).willReturn(String.valueOf(telephone.getId()));
            Assertions.assertEquals(telephone, ElectronicsFactory.createElectronic(map));
        }
    }

    @Test
    void createElectronic_incorrect_model() {
        map.put("model", " ");
        Assertions.assertThrows(InvalidStringException.class, () -> ElectronicsFactory.createElectronic(map));
    }

    private void createElectronicsMap() {
        map = new HashMap<>();
        map.put("type", "Telephone");
        map.put("series", "S-10");
        map.put("model", "Samsung");
        map.put("diagonal", "none");
        map.put("screen type", "QLED");
        map.put("country", "none");
        map.put("price", "200");
    }

    private Telephone createTelephone() {
        return new Telephone.Builder()
                .setSeries("S-10")
                .setScreenType(ScreenType.QLED)
                .setPrice(BigDecimal.valueOf(200.0))
                .setModel(Manufacture.SAMSUNG)
                .build();
    }
}