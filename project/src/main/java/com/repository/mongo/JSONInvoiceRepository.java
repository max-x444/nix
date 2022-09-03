package com.repository.mongo;

import com.config.MongoFactoryUtil;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
import com.model.vehicle.Invoice;
import com.model.vehicle.Motorbike;
import com.model.vehicle.Vehicle;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.NonNull;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JSONInvoiceRepository extends JSONRepository<Invoice> {
    private static final MongoCollection<Document> COLLECTION_AUTO =
            MongoFactoryUtil.connect("nix").getCollection("auto");
    private static final MongoCollection<Document> COLLECTION_AIRPLANE =
            MongoFactoryUtil.connect("nix").getCollection("airplane");
    private static final MongoCollection<Document> COLLECTION_MOTORBIKE =
            MongoFactoryUtil.connect("nix").getCollection("motorbike");
    private static JSONInvoiceRepository instance;

    private JSONInvoiceRepository() {
        super("invoice");
    }

    public static JSONInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new JSONInvoiceRepository();
        }
        return instance;
    }

    @Override
    public boolean save(Invoice item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        collection.insertOne(super.mapFrom(item).append("vehicles", item.getVehicles()
                .stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList())));
        return true;
    }

    @Override
    public boolean save(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        invoices.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(Invoice item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        final Document filter = new Document();
        filter.append("id", item.getId());
        final Document newData = new Document();
        newData.append("created", item.getCreated());
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        collection.updateOne(filter, updateObject);
        return true;
    }

    @Override
    public List<Invoice> delete(Invoice item) {
        super.delete(item.getId());
        return this.getAll();
    }

    @Override
    public Optional<Invoice> findById(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        final Document invoice = collection.find(filter).first();
        return (invoice != null) ? Optional.of(createInvoice(invoice)) : Optional.empty();
    }

    @Override
    public List<Invoice> getAll() {
        final List<Invoice> invoices = new ArrayList<>();
        final FindIterable<Document> documents = collection.find();
        for (Document invoice : documents) {
            invoices.add(createInvoice(invoice));
        }
        return invoices;
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id);
    }

    public long getTotalCountInvoices() {
        return collection.countDocuments();
    }

    public Map<BigDecimal, Long> groupByTotalPrice() {
        return getAll()
                .stream()
                .collect(Collectors.groupingBy(this::calculateTotalSumInvoice, Collectors.counting()));
    }

    public List<Invoice> getInvoiceMoreExpensiveThanAmount(@NonNull final BigDecimal amount) {
        return getAll()
                .stream()
                .filter(x -> calculateTotalSumInvoice(x).compareTo(amount) > 0)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalSumInvoice(@NonNull final Invoice invoice) {
        return invoice.getVehicles()
                .stream()
                .map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Invoice createInvoice(Document invoice) {
        final Set<Vehicle> vehicles = new HashSet<>();
        invoice.getList("vehicles", String.class).stream()
                .map(this::getVehicle)
                .forEach(vehicles::add);
        return new Invoice(
                invoice.getString("id"),
                LocalDateTime.parse(invoice.getString("created"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
                vehicles
        );
    }

    private Vehicle getVehicle(String id) {
        final Auto auto = getAuto(id);
        if (auto == null) {
            final Airplane airplane = getAirplane(id);
            if (airplane == null) {
                return getMotorbike(id);
            }
            return airplane;
        }
        return auto;
    }

    private Auto getAuto(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        return COLLECTION_AUTO.find(filter)
                .map(x -> GSON.fromJson(x.toJson(), Auto.class))
                .first();
    }

    private Airplane getAirplane(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        return COLLECTION_AIRPLANE.find(filter)
                .map(x -> GSON.fromJson(x.toJson(), Airplane.class))
                .first();
    }

    private Motorbike getMotorbike(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        return COLLECTION_MOTORBIKE.find(filter)
                .map(x -> GSON.fromJson(x.toJson(), Motorbike.class))
                .first();
    }
}