package com.repository.mongo;

import com.config.MongoFactoryUtil;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
import com.model.vehicle.Invoice;
import com.model.vehicle.Motorbike;
import com.model.vehicle.Vehicle;
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

import static com.mongodb.client.model.Filters.eq;

public class JSONInvoiceRepository extends JSONRepository<Invoice> {
    private static final MongoCollection<Document> COLLECTION_AUTO =
            MongoFactoryUtil.connect("nix").getCollection("auto");
    private static final MongoCollection<Document> COLLECTION_AIRPLANE =
            MongoFactoryUtil.connect("nix").getCollection("airplane");
    private static final MongoCollection<Document> COLLECTION_MOTORBIKE =
            MongoFactoryUtil.connect("nix").getCollection("motorbike");
    private static JSONInvoiceRepository instance;

    private JSONInvoiceRepository() {
        super(Invoice.class);
    }

    public static JSONInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new JSONInvoiceRepository();
        }
        return instance;
    }

    @Override
    public Optional<Invoice> findById(String id) {
        final Document invoice = collection.find(eq("_id", id)).first();
        return (invoice != null) ? Optional.of(createInvoice(invoice)) : Optional.empty();
    }

    @Override
    public List<Invoice> getAll() {
        final List<Invoice> invoices = new ArrayList<>();
        for (Document invoice : collection.find()) {
            invoices.add(createInvoice(invoice));
        }
        return invoices;
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
    public boolean save(List<Invoice> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        items.forEach(this::save);
        return true;
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

    private Invoice createInvoice(Document document) {
        final Set<Vehicle> vehicles = new HashSet<>();
        document.getList("vehicles", String.class).stream()
                .map(this::getVehicle)
                .forEach(vehicles::add);
        return new Invoice(
                document.getString("_id"),
                LocalDateTime.parse(document.getString("created"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
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
        return COLLECTION_AUTO.find(eq("_id", id))
                .map(x -> GSON.fromJson(x.toJson(), Auto.class))
                .first();
    }

    private Airplane getAirplane(String id) {
        return COLLECTION_AIRPLANE.find(eq("_id", id))
                .map(x -> GSON.fromJson(x.toJson(), Airplane.class))
                .first();
    }

    private Motorbike getMotorbike(String id) {
        return COLLECTION_MOTORBIKE.find(eq("_id", id))
                .map(x -> GSON.fromJson(x.toJson(), Motorbike.class))
                .first();
    }
}