package com.service;

import com.model.Customer;
import com.model.Electronics;
import com.model.Invoice;
import com.model.Telephone;
import com.model.Television;
import com.model.constants.Country;
import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import com.model.constants.Type;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InvoiceServiceTest<T extends Electronics> {
    private static final Customer CUSTOMER_OLD = new Customer("maksym.frolov@nure.ua", 22);
    private static final Customer CUSTOMER_YOUNG = new Customer("maxfv1999@gmail.com", 17);
    private static final String TELEPHONE = "Telephone";
    private static final String TELEVISION = "Television";
    private static final String INCORRECT_TYPE = "";
    private final List<Invoice<T>> invoiceList = new ArrayList<>();
    private InvoiceService<T> target;

    @BeforeEach
    void setUp() {
        createListInvoices();
        target = new InvoiceService<>(invoiceList);
    }

    @Test
    void numberSoldElectronicsByCategory_six_telephone() {
        final long numberSoldTelephone = 6L;
        Assertions.assertEquals(numberSoldTelephone, target.numberSoldElectronicsByCategory(TELEPHONE));
    }

    @Test
    void numberSoldElectronicsByCategory_four_television() {
        final long numberSoldTelevision = 4L;
        Assertions.assertEquals(numberSoldTelevision, target.numberSoldElectronicsByCategory(TELEVISION));
    }

    @Test
    void numberSoldElectronicsByCategory_incorrect_type() {
        final long numberSoldElectronics = 0L;
        Assertions.assertEquals(numberSoldElectronics, target.numberSoldElectronicsByCategory(INCORRECT_TYPE));
    }

    @Test
    void minSumInvoices_Customer_with_lowest_purchase_price_found() {
        final Map<Customer, BigDecimal> map = new HashMap<>();
        final BigDecimal minSumInvoice = BigDecimal.valueOf(200.0);
        map.put(CUSTOMER_OLD, minSumInvoice);
        Assertions.assertEquals(map, target.minSumInvoice());
    }

    @Test
    void minSumInvoices_Customer_with_lowest_purchase_price_not_found() {
        final Map<Customer, BigDecimal> map = new HashMap<>();
        final BigDecimal incorrectSumInvoice = BigDecimal.valueOf(100.0);
        map.put(CUSTOMER_OLD, incorrectSumInvoice);
        Assertions.assertNotEquals(map, target.minSumInvoice());
    }

    @Test
    void totalSumInvoices_is_correct() {
        final BigDecimal totalSumInvoices = BigDecimal.valueOf(7400.0);
        Assertions.assertEquals(totalSumInvoices, target.totalSumInvoices());
    }

    @Test
    void totalSumInvoices_not_correct() {
        Assertions.assertNotEquals(BigDecimal.ZERO, target.totalSumInvoices());
    }

    @Test
    void numberInvoicesByType_wholesale() {
        final long numberInvoicesWithWholesale = 1L;
        invoiceList.get(0).setType(Type.WHOLESALE);
        Assertions.assertEquals(numberInvoicesWithWholesale, target.numberInvoicesByType(Type.WHOLESALE));
    }

    @Test
    void numberInvoicesByType_retail() {
        final long numberInvoicesWithRetail = 3L;
        invoiceList.get(0).setType(Type.WHOLESALE);
        Assertions.assertEquals(numberInvoicesWithRetail, target.numberInvoicesByType(Type.RETAIL));
    }

    @Test
    void numberInvoicesByType_incorrect_type() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.numberInvoicesByType(Type.valueOf(INCORRECT_TYPE)));
    }

    @Test
    void onlyOneTypeOfElectronic_invoice_with_only_one_type_Telephone_found() {
        final List<Invoice<T>> list = new ArrayList<>();
        list.add(invoiceList.get(0));
        Assertions.assertEquals(list, target.onlyOneTypeOfElectronic(TELEPHONE));
    }

    @Test
    void onlyOneTypeOfElectronic_invoice_with_only_one_type_Television_not_found() {
        Assertions.assertEquals(new ArrayList<>(), target.onlyOneTypeOfElectronic(TELEVISION));
    }

    @Test
    void onlyOneTypeOfElectronic_incorrect_type() {
        Assertions.assertEquals(new ArrayList<>(), target.onlyOneTypeOfElectronic(INCORRECT_TYPE));
    }

    @Test
    void firstTreeInvoices_is_correct() {
        final List<Invoice<T>> firstThreeInvoices = new ArrayList<>();
        final int numberInvoices = 3;
        firstThreeInvoices.add(invoiceList.get(0));
        firstThreeInvoices.add(invoiceList.get(1));
        firstThreeInvoices.add(invoiceList.get(2));
        Assertions.assertEquals(firstThreeInvoices, target.firstInvoices(numberInvoices));
    }

    @Test
    void firstTreeInvoices_not_correct() {
        final int numberInvoices = 2;
        Assertions.assertNotEquals(invoiceList, target.firstInvoices(numberInvoices));
    }

    @Test
    void firstTreeInvoices_incorrect_number_invoices() {
        final int incorrectNumberInvoices = -3;
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.firstInvoices(incorrectNumberInvoices));
    }

    @Test
    void changeTypeByAge_is_correct() {
        final List<Invoice<T>> listWithTypeLowAge = new ArrayList<>();
        final int ageAtWhichTheTypeWillBeChanged = 18;
        invoiceList.get(0).setCustomer(CUSTOMER_YOUNG);
        invoiceList.get(2).setCustomer(CUSTOMER_YOUNG);
        listWithTypeLowAge.add(invoiceList.get(0));
        listWithTypeLowAge.add(invoiceList.get(2));
        listWithTypeLowAge.get(0).setType(Type.LOW_AGE);
        listWithTypeLowAge.get(1).setType(Type.LOW_AGE);
        Assertions.assertEquals(listWithTypeLowAge, target.changeTypeByAge(ageAtWhichTheTypeWillBeChanged));
    }

    @Test
    void changeTypeByAge_not_correct() {
        final int ageAtWhichTheTypeWillBeChanged = 18;
        Assertions.assertNotEquals(invoiceList, target.changeTypeByAge(ageAtWhichTheTypeWillBeChanged));
    }

    @Test
    void changeTypeByAge_incorrect_age() {
        final int incorrectAge = -18;
        Assertions.assertEquals(new ArrayList<>(), target.changeTypeByAge(incorrectAge));
    }

    @Test
    void sort_is_correct() {
        final List<Invoice<T>> sortedList = new ArrayList<>();
        invoiceList.get(1).setCustomer(CUSTOMER_YOUNG);
        invoiceList.get(3).setCustomer(CUSTOMER_YOUNG);
        sortedList.add(invoiceList.get(0));
        sortedList.add(invoiceList.get(2));
        sortedList.add(invoiceList.get(3));
        sortedList.add(invoiceList.get(1));
        Assertions.assertEquals(sortedList, target.sort());
    }

    @Test
    void sort_not_correct() {
        Assertions.assertNotEquals(invoiceList, target.sort());
    }

    private void createListInvoices() {
        final int numberInvoices = 4;
        int i = 0;
        while (i < numberInvoices) {
            invoiceList.add(createInvoice(++i));
        }
    }

    private Invoice<T> createInvoice(@NonNull final int sequenceNumber) {
        return new Invoice<>(createElectronic(sequenceNumber), CUSTOMER_OLD, Type.RETAIL);
    }

    @SuppressWarnings("unchecked")
    private List<T> createElectronic(@NonNull final int sequenceNumber) {
        final List<T> electronicsList = new ArrayList<>();
        switch (sequenceNumber) {
            case 1 ->
                    electronicsList.add((T) new Telephone("S-10", ScreenType.QLED, BigDecimal.valueOf(200.0), Manufacture.SAMSUNG));
            case 2 -> {
                electronicsList.add((T) new Telephone("S-10", ScreenType.QLED, BigDecimal.valueOf(200.0), Manufacture.SAMSUNG));
                electronicsList.add((T) new Television("RTI-14", ScreenType.LED, BigDecimal.valueOf(1_500.0), 25, Country.CHINA));
                electronicsList.add((T) new Telephone("S-7", ScreenType.IPS, BigDecimal.valueOf(350.0), Manufacture.SAMSUNG));
                electronicsList.add((T) new Television("RTI-13", ScreenType.QLED, BigDecimal.valueOf(1_400.0), 27, Country.CHINA));
            }
            case 3 -> {
                electronicsList.add((T) new Telephone("S-10", ScreenType.QLED, BigDecimal.valueOf(200.0), Manufacture.SAMSUNG));
                electronicsList.add((T) new Television("RTI-14", ScreenType.LED, BigDecimal.valueOf(1_500.0), 25, Country.CHINA));
                electronicsList.add((T) new Telephone("S-7", ScreenType.IPS, BigDecimal.valueOf(350.0), Manufacture.SAMSUNG));
            }
            case 4 -> {
                electronicsList.add((T) new Telephone("S-10", ScreenType.QLED, BigDecimal.valueOf(200.0), Manufacture.SAMSUNG));
                electronicsList.add((T) new Television("RTI-14", ScreenType.LED, BigDecimal.valueOf(1_500.0), 25, Country.CHINA));
            }
        }
        return electronicsList;
    }
}