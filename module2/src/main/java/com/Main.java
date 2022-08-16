package com;

import com.model.Customer;
import com.model.Electronics;
import com.model.Invoice;
import com.model.constants.Type;
import com.service.InvoiceService;
import com.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String CSV_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("electronics.csv")).getFile();

    public static <T extends Electronics> void main(String[] args) {
        final double limitType = 4_000.0;
        final Predicate<BigDecimal> criteria = i -> i.compareTo(BigDecimal.valueOf(limitType)) >= 0;
        final ShopService<T> shopService = new ShopService<>(criteria, 5, 1);
        final List<Invoice<T>> list = shopService.readFile(new File(CSV_PATH));
        for (Invoice<T> invoice : list) {
            LOGGER.debug("Time: {} User-data: {} Invoice-data: {}",
                    invoice.getCreated(), invoice.getCustomer(), invoice.getElectronics());
        }
        final InvoiceService<T> invoiceService = new InvoiceService<>(list);
        LOGGER.info("Number of items sold by category Telephone: {}", invoiceService.numberSoldElectronicsByCategory("Telephone"));
        LOGGER.info("Number of items sold by category Television: {}", invoiceService.numberSoldElectronicsByCategory("Television"));
        LOGGER.info("The amount of the smallest Invoice and buyer information:");
        for (Map.Entry<Customer, BigDecimal> entry : invoiceService.minSumInvoice().entrySet()) {
            System.out.println(entry.getKey() + " Minimum invoice price: " + entry.getValue());
        }
        LOGGER.info("The sum of all purchases: {}", invoiceService.totalSumInvoices());
        LOGGER.info("Number of receipts with category retail: {}", invoiceService.numberInvoicesByType(Type.RETAIL));
        LOGGER.info("Receipts that contain only one type of Telephone:");
        for (Invoice<T> invoice : invoiceService.onlyOneTypeOfElectronic("Telephone")) {
            System.out.println(invoice);
        }
        LOGGER.info("Receipts that contain only one type of Television:");
        for (Invoice<T> invoice : invoiceService.onlyOneTypeOfElectronic("Television")) {
            System.out.println(invoice);
        }
        LOGGER.info("First 3 Invoices made by buyers:");
        for (Invoice<T> invoice : invoiceService.firstInvoices(3)) {
            System.out.println(invoice);
        }
        LOGGER.info("Information on checks purchased by a user under 18 years old:");
        for (Invoice<T> invoiceFromMinor : invoiceService.changeTypeByAge(18)) {
            System.out.println(invoiceFromMinor);
        }
        LOGGER.info("Sorting:" +
                " 1) First by the age of the buyer from the largest to the smallest" +
                " 2) Next by the number of items purchased" +
                " 3) Next by the total amount of items purchased");
        for (Invoice<T> invoice : invoiceService.sort()) {
            System.out.println(invoice);
        }
    }
}