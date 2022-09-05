package com;

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
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        System.out.println(AUTO_SERVICE.save(auto));
        System.out.println("Save auto: " + AUTO_SERVICE.save(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));
        auto.setPrice(BigDecimal.valueOf(1_000.0));
        auto.setCount(1);
        auto.setBodyType("coupe");
        auto.setInvoice(invoice);
        System.out.println("Update auto: " + AUTO_SERVICE.update(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));

        final Invoice secondInvoice = new Invoice();
        secondInvoice.setId(UUID.randomUUID().toString());
        secondInvoice.setCreated(LocalDateTime.now());
        final Auto secondAuto = AUTO_SERVICE.create(1).get(0);
        secondAuto.setCount(1);
        secondAuto.setPrice(BigDecimal.valueOf(2_000.0));
        System.out.println("Save second auto: " + AUTO_SERVICE.save(secondAuto));
        secondAuto.setInvoice(secondInvoice);
        System.out.println("Get all autos: " + Arrays.toString(AUTO_SERVICE.getAll().toArray()));
        secondInvoice.setVehicles(Set.of(secondAuto));
        System.out.println("Save second invoice: " + INVOICE_SERVICE.save(secondInvoice));

        final Airplane airplane = AIRPLANE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(500.0),
                5,
                1);
        System.out.println("Save airplane: " + AIRPLANE_SERVICE.save(airplane));
        airplane.setInvoice(invoice);

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
        System.out.println("Save motorbike: " + MOTORBIKE_SERVICE.save(motorbike));
        motorbike.setInvoice(invoice);
        System.out.println("Get motorbike: " + MOTORBIKE_SERVICE.findById(motorbike.getId()));

        invoice.setVehicles(Set.of(auto, airplane, motorbike));
        System.out.println("Save first invoice: " + INVOICE_SERVICE.save(invoice));
        System.out.println("Get all invoices:");
        for (Invoice i : INVOICE_SERVICE.getAll()) {
            System.out.println(i);
        }
        System.out.println(INVOICE_SERVICE.delete(invoice.getId()));
        System.out.println(INVOICE_SERVICE.delete(secondInvoice.getId()));

        final Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://ec2-3-224-125-117.compute-1.amazonaws.com:5432/dee4145r03oc3c",
                        "wfizzbiikzjijl",
                        "ae10f479cfea1f6ef1861b7e2e26f0d4876b759ac470aa46d203bd4ded7a5be7")
                .baselineOnMigrate(true)
                .locations("db/migration")
                .load();
        flyway.migrate();
    }
}