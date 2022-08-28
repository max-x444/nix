package com.repository.jdbc;

import com.config.JDBCConfig;
import com.model.constants.Manufacturer;
import com.model.vehicle.Airplane;
import com.model.vehicle.Auto;
import com.model.vehicle.Engine;
import com.model.vehicle.Motorbike;
import com.model.vehicle.Vehicle;
import com.repository.CrudRepository;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DBVehicleRepository implements CrudRepository<Vehicle> {
    protected final Connection connection;
    private static DBVehicleRepository instance;

    public static DBVehicleRepository getInstance() {
        if (instance == null) {
            instance = new DBVehicleRepository();
        }
        return instance;
    }

    private DBVehicleRepository() {
        connection = JDBCConfig.getConnection();
    }

    @Override
    @SneakyThrows
    public Optional<Vehicle> findById(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final String sql = """
                SELECT * FROM public."vehicle"
                FULL OUTER JOIN public."invoice" USING("invoice_id")
                   FULL OUTER JOIN public."auto" USING("vehicle_id")
                   FULL OUTER JOIN public."airplane" USING("vehicle_id")
                   FULL OUTER JOIN public."motorbike" USING("vehicle_id")
                   FULL OUTER JOIN public."engine" USING("vehicle_id")
                   FULL OUTER JOIN public."detail" USING("vehicle_id")
                WHERE vehicle.vehicle_id = ?;""";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            final Vehicle vehicle = mapRowToObject(resultSet);
            final List<String> details = new ArrayList<>();
            details.add(resultSet.getString("name"));
            while (resultSet.next()) {
                details.add(resultSet.getString("name"));
            }
            vehicle.setDetails(details);
            return Optional.of(vehicle);
        }
        return Optional.empty();
    }

    @Override
    @SneakyThrows
    public List<Vehicle> getAll() {
        final List<Vehicle> result = new ArrayList<>();
        final String sql = """
                SELECT vehicle_id FROM public."vehicle";""";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            findById(resultSet.getString("vehicle_id")).ifPresent(result::add);
        }
        return result;
    }

    @Override
    @SneakyThrows
    public boolean save(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        if (findById(vehicle.getId()).isPresent()) {
            return update(vehicle);
        } else {
            final String sql = """
                    INSERT INTO public."vehicle" (model, manufacturer, price, count, type, vehicle_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    RETURNING vehicle_id;""";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (mapObjectToRow(preparedStatement, vehicle)) {
                final ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    insertIntoDetail(vehicle, resultSet.getString("vehicle_id"));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean save(List<Vehicle> vehicleList) {
        if (vehicleList.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        vehicleList.forEach(this::save);
        return true;
    }

    @Override
    @SneakyThrows
    public boolean update(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        final String sql = """
                UPDATE public."vehicle"
                SET model = ?, manufacturer = ?, price = ?, count = ?, type = ?
                WHERE vehicle_id = ?
                RETURNING vehicle_id;""";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        mapObjectToRow(preparedStatement, vehicle);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            clearDetailById(resultSet.getString("vehicle_id"));
            insertIntoDetail(vehicle, resultSet.getString("vehicle_id"));
            return true;
        }
        return false;
    }

    @Override
    @SneakyThrows
    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final String sql = """        
                WITH ins AS (
                DELETE FROM public."vehicle"
                WHERE vehicle.vehicle_id = ?
                RETURNING invoice_id AS ins_id)
                DELETE FROM public."invoice"
                WHERE (SELECT COUNT(vehicle.invoice_id)
                       FROM public."vehicle"
                       WHERE vehicle.invoice_id in (SELECT ins_id FROM ins)) = 1
                RETURNING invoice.invoice_id;""";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    @Override
    public List<Vehicle> delete(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        delete(vehicle.getId());
        return getAll();
    }

    @SneakyThrows
    private Vehicle mapRowToObject(@NonNull final ResultSet resultSet) {
        switch (resultSet.getString("type")) {
            case "AUTO" -> {
                return createAuto(resultSet);
            }
            case "MOTORBIKE" -> {
                return createMotorbike(resultSet);
            }
            case "AIRPLANE" -> {
                return createAirplane(resultSet);
            }
            default -> throw new IllegalArgumentException("Cannot build " + resultSet.getString("type"));
        }
    }

    @SneakyThrows
    private Auto createAuto(@NonNull final ResultSet resultSet) {
        final Auto auto = new Auto(
                resultSet.getString("vehicle_id"),
                resultSet.getString("model"),
                Manufacturer.valueOf(resultSet.getString("manufacturer")),
                resultSet.getBigDecimal("price"),
                resultSet.getString("body_type"),
                resultSet.getInt("count"));
        auto.setInvoiceId(resultSet.getString("invoice_id"));
        return auto;
    }

    @SneakyThrows
    private Motorbike createMotorbike(@NonNull final ResultSet resultSet) {
        final Motorbike motorbike = new Motorbike(
                resultSet.getString("vehicle_id"),
                resultSet.getString("model"),
                Manufacturer.valueOf(resultSet.getString("manufacturer")),
                resultSet.getBigDecimal("price"),
                resultSet.getBigDecimal("lean_angle").doubleValue(),
                resultSet.getInt("count"),
                resultSet.getTimestamp("created_motorbike").toLocalDateTime(),
                resultSet.getString("currency"),
                new Engine(resultSet.getInt("volume"), resultSet.getString("brand")));
        motorbike.setInvoiceId(resultSet.getString("invoice_id"));
        return motorbike;
    }

    @SneakyThrows
    private Airplane createAirplane(@NonNull final ResultSet resultSet) {
        final Airplane airplane = new Airplane(
                resultSet.getString("vehicle_id"),
                resultSet.getString("model"),
                Manufacturer.valueOf(resultSet.getString("manufacturer")),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("number_of_passenger_seats"),
                resultSet.getInt("count"));
        airplane.setInvoiceId(resultSet.getString("invoice_id"));
        return airplane;
    }

    @SneakyThrows
    private boolean mapObjectToRow(@NonNull final PreparedStatement preparedStatement, @NonNull final Vehicle vehicle) {
        preparedStatement.setString(1, vehicle.getModel());
        preparedStatement.setString(2, vehicle.getManufacturer().getTitle());
        preparedStatement.setBigDecimal(3, vehicle.getPrice());
        preparedStatement.setInt(4, vehicle.getCount());
        preparedStatement.setString(5, vehicle.getType().getTitle());
        preparedStatement.setString(6, vehicle.getId());
        return true;
    }

    @SneakyThrows
    private void clearDetailById(@NonNull final String vehicleId) {
        final String sql = """
                DELETE FROM public."detail"
                WHERE vehicle_id = ?;""";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, vehicleId);
        preparedStatement.execute();
    }

    @SneakyThrows
    private void insertIntoDetail(@NonNull final Vehicle vehicle, @NonNull final String vehicleId) {
        if (!vehicle.getDetails().isEmpty()) {
            connection.setAutoCommit(false);
            final String sql = """
                    INSERT INTO public."detail" (detail_id, name, vehicle_id) VALUES (?, ?, ?);""";
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (String detail : vehicle.getDetails()) {
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setString(2, detail);
                preparedStatement.setString(3, vehicleId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        }
    }
}