package com;

import com.config.MongoFactoryUtil;
import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
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
        MongoFactoryUtil.connect("nix").drop();
        final Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID().toString());
        invoice.setCreated(LocalDateTime.now());
        final Auto auto = AUTO_SERVICE.create(1).get(0);
        System.out.println("Save auto: " + AUTO_SERVICE.save(auto));
        auto.setPrice(BigDecimal.valueOf(1_000.0));
        auto.setCount(1);
        auto.setBodyType("coupe");
        System.out.println("Update auto: " + AUTO_SERVICE.update(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));

        final Invoice secondInvoice = new Invoice();
        final Auto secondAuto = AUTO_SERVICE.create(1).get(0);
        secondAuto.setCount(1);
        secondAuto.setPrice(BigDecimal.valueOf(2_000.0));
        secondAuto.setInvoice(secondInvoice);
        System.out.println("Save second auto: " + AUTO_SERVICE.save(secondAuto));
//        System.out.println("Delete first auto: " + AUTO_SERVICE.delete(auto.getId()));
//        System.out.println("Delete second auto: " + AUTO_SERVICE.delete(secondAuto));
        System.out.println("Get all autos: " + Arrays.toString(AUTO_SERVICE.getAll().toArray()));

        secondInvoice.setId(UUID.randomUUID().toString());
        secondInvoice.setCreated(LocalDateTime.now());
        secondInvoice.setVehicles(Set.of(secondAuto));

        System.out.println("Save second invoice: " + INVOICE_SERVICE.save(secondInvoice));

        final Airplane airplane = AIRPLANE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(500.0),
                5,
                1);
        airplane.setInvoice(invoice);
        System.out.println("Save airplane: " + AIRPLANE_SERVICE.save(airplane));

        final Motorbike motorbike = MOTORBIKE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(500.0),
                55.0,
                1,
                LocalDateTime.now(),
                "$",
                new Engine(UUID.randomUUID().toString(), 3, "BMW"));
        motorbike.setDetails(List.of("wheels", "spoiler"));
        motorbike.setInvoice(invoice);
        System.out.println("Save motorbike: " + MOTORBIKE_SERVICE.save(motorbike));
        System.out.println("Get motorbike: " + MOTORBIKE_SERVICE.findById(motorbike.getId()));

        invoice.setVehicles(Set.of(auto, airplane, motorbike));
        System.out.println("Save first invoice: " + INVOICE_SERVICE.save(invoice));
        System.out.println("Get all invoices:");
        for (Invoice i : INVOICE_SERVICE.getAll()) {
            System.out.println(i);
        }
        System.out.println("Get total count invoices: " + INVOICE_SERVICE.getTotalCountInvoices());
        System.out.println("Group by total price:");
        for (Map.Entry<BigDecimal, Long> entry : INVOICE_SERVICE.groupByTotalPrice().entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
        System.out.println("Get invoice more expensive than 1000:");
        for (Invoice in : INVOICE_SERVICE.getInvoiceMoreExpensiveThanAmount(BigDecimal.valueOf(1000))) {
            System.out.println(in);
        }
    }
}