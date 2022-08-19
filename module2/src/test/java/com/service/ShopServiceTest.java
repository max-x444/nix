package com.service;

import com.model.Customer;
import com.model.Invoice;
import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import com.model.constants.Type;
import com.model.electronics.Electronics;
import com.model.electronics.Telephone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;


class ShopServiceTest<T extends Electronics> {
    private static final Customer CUSTOMER = new Customer("maksym.frolov@nure.ua", 22);
    private final List<Invoice<T>> invoiceList = new ArrayList<>();
    private List<String> stringList;
    private ShopService<T> target;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        final double limitType = 4_000.0;
        final Predicate<BigDecimal> criteria = i -> i.compareTo(BigDecimal.valueOf(limitType)) >= 0;
        final int maxListSize = 5;
        final int minListSize = 1;
        target = new ShopService<>(criteria, maxListSize, minListSize);
        invoiceList.add(new Invoice<>(
                List.of((T) new Telephone("S-10", ScreenType.QLED, BigDecimal.valueOf(200.0), Manufacture.SAMSUNG)),
                CUSTOMER,
                Type.RETAIL));
        stringList = List.of(
                "type,series,model,diagonal,screen type,country,price",
                "Telephone,S-10,Samsung,none,QLED,none,200");
    }

    @Test
    void readFile() {
        try (final MockedConstruction<FileReader> mockFileReader = Mockito.mockConstruction(FileReader.class);
             final MockedConstruction<BufferedReader> mockBufferedReader = Mockito.mockConstruction(BufferedReader.class,
                     (mock, context) -> Mockito.when(mock.readLine())
                             .thenReturn(stringList.get(0))
                             .thenReturn(stringList.get(1))
                             .thenReturn(null));
             final MockedStatic<PersonService> mockPersonService = Mockito.mockStatic(PersonService.class);
             final MockedStatic<LocalDateTime> mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
             final MockedStatic<UUID> mockUUID = Mockito.mockStatic(UUID.class)
        ) {
            final File file = Mockito.mock(File.class);
            Mockito.when(file.exists()).thenReturn(true);

            final UUID uuid = Mockito.mock(UUID.class);
            BDDMockito.given(UUID.randomUUID()).willReturn(uuid);
            BDDMockito.given(uuid.toString()).willReturn(String.valueOf(invoiceList.get(0).getElectronics().get(0).getId()));

            mockPersonService.when(PersonService::create).thenReturn(CUSTOMER);

            mockLocalDateTime.when(LocalDateTime::now).thenReturn(invoiceList.get(0).getCreated());

            Assertions.assertEquals(invoiceList, target.readFile(file));
        }
    }
}