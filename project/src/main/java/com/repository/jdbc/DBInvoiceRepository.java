package com.repository.jdbc;

import com.model.vehicle.Invoice;
import com.model.vehicle.Vehicle;
import com.repository.CrudRepository;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DBInvoiceRepository implements CrudRepository<Invoice> {
    private static DBInvoiceRepository instance;
    private static DBVehicleRepository dbVehicleRepository;

    private DBInvoiceRepository() {
        dbVehicleRepository = DBVehicleRepository.getInstance();
    }

    public static DBInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new DBInvoiceRepository();
        }
        return instance;
    }

    @SneakyThrows
    public Optional<Invoice> findById(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final String sql = """
                SELECT * FROM public."invoice"
                FULL OUTER JOIN public."vehicle" USING("invoice_id")
                WHERE invoice.invoice_id = ?;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            final String invoiceId = resultSet.getString("invoice_id");
            final LocalDateTime created = resultSet.getTimestamp("created").toLocalDateTime();
            final Set<Vehicle> vehicleSet = new LinkedHashSet<>();
            dbVehicleRepository.findById(resultSet.getString("vehicle_id")).ifPresent(vehicleSet::add);
            while (resultSet.next()) {
                dbVehicleRepository.findById(resultSet.getString("vehicle_id")).ifPresent(vehicleSet::add);
            }
            return Optional.of(mapRowToObject(invoiceId, created, vehicleSet));
        } else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    @Override
    public List<Invoice> getAll() {
        final List<Invoice> result = new ArrayList<>();
        final String sql = """
                SELECT invoice_id FROM public."invoice";""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            findById(resultSet.getString("invoice_id")).ifPresent(result::add);
        }
        return result;
    }

    @SneakyThrows
    @Override
    public boolean save(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        if (findById(invoice.getId()).isPresent()) {
            return update(invoice);
        } else {
            if (checkInvoice(invoice) == 0) {
                final String sql = """
                        INSERT INTO public."invoice" (invoice_id, created)
                        VALUES (?, ?)
                        RETURNING invoice_id;""";
                final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
                preparedStatement.setString(1, invoice.getId());
                preparedStatement.setObject(2, invoice.getCreated());
                final ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return updateInvoiceIdInVehicle(invoice.getVehicles(), resultSet.getString("invoice_id"));
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean save(List<Invoice> invoiceList) {
        if (invoiceList.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        invoiceList.forEach(this::save);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        final String sql = """
                UPDATE public."invoice"
                SET created = ?
                WHERE invoice_id = ?
                RETURNING *;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        preparedStatement.setObject(1, invoice.getCreated());
        preparedStatement.setString(2, invoice.getId());
        final ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    @SneakyThrows
    @Override
    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final String sql = """        
                DELETE FROM public."invoice"
                WHERE invoice_id = ?
                RETURNING *;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    @Override
    public List<Invoice> delete(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        delete(invoice.getId());
        return getAll();
    }

    @SneakyThrows
    public Map<Invoice, BigDecimal> groupByInvoice() {
        final Map<Invoice, BigDecimal> map = new LinkedHashMap<>();
        final String sql = """
                SELECT invoice_id, SUM(count * price) AS total_price
                FROM public."vehicle"
                GROUP BY invoice_id ORDER BY total_price DESC;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();
        Optional<Invoice> invoice;
        while (resultSet.next()) {
            invoice = findById(resultSet.getString("invoice_id"));
            if (invoice.isPresent()) {
                map.put(invoice.get(), resultSet.getBigDecimal("total_price"));
            }
        }
        return map;
    }

    @SneakyThrows
    public List<Invoice> getInvoiceMoreExpensiveThanAmount(@NonNull final BigDecimal amount) {
        final List<Invoice> invoiceList = new ArrayList<>();
        final String sql = """
                SELECT invoice_id, SUM(count * price) AS total_price
                FROM public."vehicle"
                GROUP BY invoice_id
                HAVING SUM(count * price) > ?
                ORDER BY total_price DESC""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, amount);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            findById(resultSet.getString("invoice_id")).ifPresent(invoiceList::add);
        }
        return invoiceList;
    }

    @SneakyThrows
    public int getTotalCountInvoices() {
        final String sql = """
                SELECT COUNT(invoice_id) AS total_count_invoices
                FROM public."invoice";""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();
        return (resultSet.next()) ? resultSet.getInt("total_count_invoices") : 0;
    }

    @SneakyThrows
    private Invoice mapRowToObject(String invoiceId, LocalDateTime created, Set<Vehicle> vehicleList) {
        return new Invoice(invoiceId, created, vehicleList);
    }

    @SneakyThrows
    private boolean updateInvoiceIdInVehicle(@NonNull final Set<Vehicle> vehicleSet, @NonNull final String invoice_id) {
        dbVehicleRepository.connection.setAutoCommit(false);
        final String sql = """
                UPDATE public."vehicle"
                SET invoice_id = ?
                WHERE vehicle_id = ? AND invoice_id IS NULL;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        for (Vehicle vehicle : vehicleSet) {
            preparedStatement.setString(1, invoice_id);
            preparedStatement.setString(2, vehicle.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        dbVehicleRepository.connection.commit();
        dbVehicleRepository.connection.setAutoCommit(true);
        return true;
    }

    @SneakyThrows
    private int checkInvoice(@NonNull final Invoice invoice) {
        int countInvoices = 0;
        final String sql = """
                SELECT COUNT(invoice_id) AS total_count
                FROM public."vehicle"
                WHERE vehicle_id = ? AND invoice_id IS NOT NULL;""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        ResultSet resultSet;
        for (Vehicle vehicle : invoice.getVehicles()) {
            preparedStatement.setString(1, vehicle.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countInvoices += resultSet.getInt("total_count");
            }
        }
        return countInvoices;
    }
}