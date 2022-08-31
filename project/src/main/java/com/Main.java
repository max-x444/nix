package com;

import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
import com.model.vehicle.Detail;
import com.model.vehicle.Engine;
import com.model.vehicle.Invoice;
import com.model.vehicle.Motorbike;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.InvoiceService;
import com.service.MotorbikeService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();
    private static final InvoiceService INVOICE_SERVICE = InvoiceService.getInstance();
    private static final String JSON_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.json")).getFile();
    private static final String XML_PATH =
            Objects.requireNonNull(Main.class.getClassLoader().getResource("vehicle.xml")).getFile();

    @SneakyThrows
    public static void main(String[] args) {
        final Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID().toString());
        invoice.setCreated(LocalDateTime.now());
        final Auto auto = AUTO_SERVICE.create(1).get(0);
        System.out.println("Save first auto: " + AUTO_SERVICE.save(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));
        auto.setPrice(BigDecimal.valueOf(1_000));
        auto.setCount(1);
        auto.setBodyType("coupe");
        System.out.println("Update first auto: " + AUTO_SERVICE.update(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));
        auto.setInvoice(invoice);

        final Invoice secondInvoice = new Invoice();
        final Auto secondAuto = AUTO_SERVICE.create(1).get(0);
        secondAuto.setCount(1);
        secondAuto.setPrice(BigDecimal.valueOf(2_000));
        secondAuto.setInvoice(secondInvoice);

        secondInvoice.setId(UUID.randomUUID().toString());
        secondInvoice.setCreated(LocalDateTime.now());
        secondInvoice.setVehicles(Set.of(secondAuto));
        System.out.println("Save second invoice: " + INVOICE_SERVICE.save(secondInvoice));
        System.out.println("Get second auto: " + AUTO_SERVICE.findById(secondAuto.getId()));
        System.out.println("Get all autos:");
        for (Auto a : AUTO_SERVICE.getAll()) {
            System.out.println(a);
        }

        final Airplane airplane = AIRPLANE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(500),
                5,
                1);
        airplane.setInvoice(invoice);
        final Motorbike motorbike = MOTORBIKE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(400),
                55.0,
                1,
                LocalDateTime.now(),
                "$",
                new Engine(UUID.randomUUID().toString(), 3, "BMW"));
        final Detail firstDetail = new Detail(UUID.randomUUID().toString(), "wheels");
        firstDetail.setVehicle(motorbike);
        final Detail secondDetail = new Detail(UUID.randomUUID().toString(), "spoiler");
        secondDetail.setVehicle(motorbike);

        motorbike.setDetails(List.of(firstDetail, secondDetail));
        motorbike.setInvoice(invoice);

        invoice.setVehicles(Set.of(airplane, auto, motorbike));
        System.out.println("Save first invoice: " + INVOICE_SERVICE.save(invoice));
        invoice.setCreated(LocalDateTime.now());
        System.out.println("Update first invoice: " + INVOICE_SERVICE.update(invoice));
        System.out.println("Get first invoice: " + INVOICE_SERVICE.findById(invoice.getId()));
        System.out.println("Get all invoices:");
        for (Invoice in : INVOICE_SERVICE.getAll()) {
            System.out.println(in);
        }
        System.out.println("Group by invoice use dto:");
        for (Map.Entry<Invoice, BigDecimal> entry : INVOICE_SERVICE.groupByInvoiceDTO().entrySet()) {
            System.out.println("Key: " + entry.getKey().getId() + " Value: " + entry.getValue());
        }
        System.out.println("Group by invoice use criteria:");
        for (Map.Entry<Invoice, BigDecimal> entry : INVOICE_SERVICE.groupByInvoiceCriteria().entrySet()) {
            System.out.println("Key: " + entry.getKey().getId() + " Value: " + entry.getValue());
        }
        System.out.println("Get invoice more expensive than 1000 use dto:");
        for (Invoice i : INVOICE_SERVICE.getInvoiceMoreExpensiveThanAmountDTO(BigDecimal.valueOf(1_000))) {
            System.out.println(i.getId());
        }
        System.out.println("Get total count invoices: " + INVOICE_SERVICE.getTotalCountInvoices());

        System.out.println("Delete first invoice: " + INVOICE_SERVICE.delete(invoice.getId()));
        System.out.println("Delete second invoice: " + INVOICE_SERVICE.delete(secondInvoice.getId()));
        System.out.println("Get all autos:");
        for (Auto a : AUTO_SERVICE.getAll()) {
            System.out.println(a);
        }
        System.out.println("Get all invoices: " + Arrays.toString(INVOICE_SERVICE.getAll().toArray()));
    }
}