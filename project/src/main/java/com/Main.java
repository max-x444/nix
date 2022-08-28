package com;

import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
import com.model.vehicle.Engine;
import com.model.vehicle.Invoice;
import com.model.vehicle.Motorbike;
import com.model.vehicle.Vehicle;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.DBTableService;
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
import java.util.UUID;
import java.util.stream.Collectors;

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
        System.out.println("Drop all tables");
        DBTableService.getInstance().dropTables();
        System.out.println("Create all tables");
        DBTableService.getInstance().createTables();
        final Auto auto = AUTO_SERVICE.create(1).get(0);
        final Auto secondAuto = AUTO_SERVICE.create(1).get(0);
        final List<Auto> autoList = List.of(auto, secondAuto);
        final Airplane airplane = AIRPLANE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(6),
                5,
                1);
        final Motorbike motorbike = MOTORBIKE_SERVICE.create(
                UUID.randomUUID().toString(),
                "S 3500 RR",
                Manufacturer.BMW,
                BigDecimal.valueOf(800),
                55.0,
                1,
                LocalDateTime.now(),
                "$",
                new Engine(3, "BMW"));

        System.out.println("Airplane:");
        System.out.println("Save airplane: " + AIRPLANE_SERVICE.save(airplane));
        System.out.println("Print all airplanes: " + Arrays.toString(AIRPLANE_SERVICE.getAll().toArray()));
        airplane.setPrice(BigDecimal.valueOf(1000));
        airplane.setNumberOfPassengerSeats(5);
        System.out.println("Update airplane: " + AIRPLANE_SERVICE.save(airplane));
        System.out.println("Get airplane: " + AIRPLANE_SERVICE.findById(airplane.getId()));
//        System.out.println("Delete airplane: " + AIRPLANE_SERVICE.delete(airplane.getId()));
//        System.out.println("Get airplane: " + AIRPLANE_SERVICE.findById(airplane.getId()) + "\n");

        System.out.println("Motorbike: ");
        System.out.println("Save motorbike: " + MOTORBIKE_SERVICE.save(motorbike));
        System.out.println("Print all motorbikes: " + Arrays.toString(MOTORBIKE_SERVICE.getAll().toArray()));
        motorbike.setCurrency("#");
        motorbike.setLeanAngle(1.0);
        motorbike.setCreatedMotorbike(LocalDateTime.now());
        motorbike.getEngine().setVolume(110);
        motorbike.getEngine().setBrand("AUDI");
        System.out.println("Update motorbike: " + MOTORBIKE_SERVICE.save(motorbike));
        System.out.println("Get motorbike: " + MOTORBIKE_SERVICE.findById(motorbike.getId()));
//        System.out.println("Delete motorbike: " + MOTORBIKE_SERVICE.delete(motorbike.getId()));
//        System.out.println("Get motorbike: " + MOTORBIKE_SERVICE.findById(motorbike.getId()) + "\n");

        System.out.println("Auto:");
        auto.setDetails(List.of("wheels", "spoiler"));
        auto.setBodyType("MODEL");
        System.out.println("Save all autos: " + AUTO_SERVICE.save(autoList));
        System.out.println("Print all autos: " + Arrays.toString(AUTO_SERVICE.getAll().toArray()));
        auto.setCount(2);
        auto.setPrice(BigDecimal.ZERO);
        System.out.println("Update first auto: " + AUTO_SERVICE.update(auto));
        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));
//        System.out.println("Delete first auto: " + AUTO_SERVICE.delete(auto.getId()));
//        System.out.println("Get first auto: " + AUTO_SERVICE.findById(auto.getId()));
//        System.out.println("Print all autos: " + Arrays.toString(AUTO_SERVICE.delete(secondAuto).toArray()));

        System.out.println("Invoice:");
        final Invoice invoice = new Invoice(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                List.of(auto, airplane, motorbike, secondAuto));
        System.out.println("Save first invoice: " + INVOICE_SERVICE.save(invoice));
        final List<Vehicle> fourAuto = AUTO_SERVICE.create(4)
                .stream()
                .map(x -> (Vehicle) x)
                .collect(Collectors.toList());
        final Invoice secondInvoice = new Invoice(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                fourAuto);
        System.out.println("Save second invoice: " + INVOICE_SERVICE.save(secondInvoice));
        System.out.println("First invoice id: " + invoice.getId());
        System.out.println("Second invoice id: " + secondInvoice.getId());
        //Exception when deleting all vehicles
        System.out.println("Get first invoice: " + INVOICE_SERVICE.findById(invoice.getId()));
        invoice.setCreated(LocalDateTime.now());
        System.out.println("Update first invoice: " + INVOICE_SERVICE.update(invoice));
        System.out.println("Print all invoices: " + Arrays.toString(INVOICE_SERVICE.getAll().toArray()));
        System.out.println("Group invoices by amount:");
        for (Map.Entry<Invoice, BigDecimal> entry : INVOICE_SERVICE.groupByTotalPrice().entrySet()) {
            System.out.println("Key: " + entry.getKey().getId() + " Value: " + entry.getValue());
        }
        System.out.println("Get invoices more expensive than 3000 amount:");
        for (Invoice value : INVOICE_SERVICE.getInvoiceMoreExpensiveThanAmount(BigDecimal.valueOf(3_000))) {
            System.out.println(value);
        }
        System.out.println("Get count of invoices: " + INVOICE_SERVICE.getTotalCountInvoices());
        System.out.println("Delete first invoice: " + INVOICE_SERVICE.delete(invoice.getId()));
        System.out.println("Delete second invoice: " + Arrays.toString(INVOICE_SERVICE.delete(secondInvoice).toArray()));
    }
}