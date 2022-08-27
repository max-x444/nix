package com.repository.jdbc;

import com.model.vehicle.Motorbike;
import com.repository.CrudRepository;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DBMotorbikeRepository implements CrudRepository<Motorbike> {
    private static DBMotorbikeRepository instance;
    private static DBVehicleRepository dbVehicleRepository;

    private DBMotorbikeRepository() {
    }

    public static DBMotorbikeRepository getInstance() {
        if (instance == null) {
            dbVehicleRepository = DBVehicleRepository.getInstance();
            instance = new DBMotorbikeRepository();
        }
        return instance;
    }

    @Override
    public Optional<Motorbike> findById(String id) {
        return dbVehicleRepository.findById(id)
                .map(motorbike -> (Motorbike) motorbike);
    }

    @Override
    public List<Motorbike> getAll() {
        return dbVehicleRepository.getAll().stream()
                .filter(vehicle -> vehicle.getType().getTitle().equalsIgnoreCase("motorbike"))
                .map(vehicle -> (Motorbike) vehicle)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public boolean save(Motorbike motorbike) {
        if (motorbike == null) {
            throw new IllegalArgumentException("Motorbike must not be null");
        }
        if (dbVehicleRepository.findById(motorbike.getId()).isEmpty()) {
            if (dbVehicleRepository.save(motorbike)) {
                final String sql = """
                        WITH ins AS (
                        INSERT INTO public."motorbike" (created, lean_angle, currency, vehicle_id)
                            VALUES (?, ?, ?, ?)
                            RETURNING vehicle_id AS ins_id)
                        INSERT INTO public."engine" (vehicle_id, volume, brand)
                        SELECT ins_id, ?, ? FROM ins
                        RETURNING *;""";
                final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
                mapObjectToRow(preparedStatement, motorbike);
                final ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } else {
            return update(motorbike);
        }
        return false;
    }

    @Override
    public boolean save(List<Motorbike> motorbikeList) {
        if (motorbikeList.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        motorbikeList.forEach(this::save);
        return true;
    }

    @Override
    @SneakyThrows
    public boolean update(Motorbike motorbike) {
        if (dbVehicleRepository.update(motorbike)) {
            final String sql = """
                    WITH ins AS (
                        UPDATE public."motorbike"
                                     SET created = ?, lean_angle = ?, currency = ?
                                     WHERE motorbike.vehicle_id = ?
                                     RETURNING vehicle_id AS ins_id)
                        UPDATE public."engine"
                        SET volume = ?, brand = ?
                        WHERE engine.vehicle_id in (SELECT ins_id FROM ins)
                        RETURNING *;""";
            final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
            mapObjectToRow(preparedStatement, motorbike);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return dbVehicleRepository.delete(id);
    }

    @Override
    public List<Motorbike> delete(Motorbike motorbike) {
        if (motorbike == null) {
            throw new IllegalArgumentException("Motorbike must not be null");
        }
        dbVehicleRepository.delete(motorbike.getId());
        return getAll();
    }

    @SneakyThrows
    private void mapObjectToRow(@NonNull final PreparedStatement preparedStatement, @NonNull final Motorbike motorbike) {
        preparedStatement.setObject(1, motorbike.getCreated());
        preparedStatement.setDouble(2, motorbike.getLeanAngle());
        preparedStatement.setString(3, motorbike.getCurrency());
        preparedStatement.setString(4, motorbike.getId());
        preparedStatement.setInt(5, motorbike.getEngine().getVolume());
        preparedStatement.setString(6, motorbike.getEngine().getBrand());
    }
}