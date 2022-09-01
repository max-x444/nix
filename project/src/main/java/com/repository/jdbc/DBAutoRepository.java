package com.repository.jdbc;

import com.model.vehicle.Auto;
import com.repository.CrudRepository;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBAutoRepository implements CrudRepository<Auto> {
    private static DBAutoRepository instance;
    private static DBVehicleRepository dbVehicleRepository;

    private DBAutoRepository() {
        dbVehicleRepository = DBVehicleRepository.getInstance();
    }

    public static DBAutoRepository getInstance() {
        if (instance == null) {
            instance = new DBAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<Auto> findById(String id) {
        return dbVehicleRepository.findById(id)
                .map(value -> (Auto) value);
    }

    @Override
    @SneakyThrows
    public List<Auto> getAll() {
        final List<Auto> result = new ArrayList<>();
        final String sql = """
                SELECT vehicle_id FROM public."vehicle"
                WHERE type = 'AUTO';""";
        final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            findById(resultSet.getString("vehicle_id")).ifPresent(result::add);
        }
        return result;
    }

    @Override
    @SneakyThrows
    public boolean save(Auto auto) {
        if (auto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        if (dbVehicleRepository.findById(auto.getId()).isEmpty()) {
            if (dbVehicleRepository.save(auto)) {
                final String sql = """
                        INSERT INTO public."auto" (vehicle_id, body_type)
                        VALUES (?, ?)
                        RETURNING *;""";
                final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
                preparedStatement.setString(1, auto.getId());
                preparedStatement.setString(2, auto.getBodyType());
                final ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } else {
            return update(auto);
        }
        return false;
    }

    @Override
    public boolean save(List<Auto> autoList) {
        if (autoList.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        autoList.forEach(this::save);
        return true;
    }

    @Override
    @SneakyThrows
    public boolean update(Auto auto) {
        if (dbVehicleRepository.update(auto)) {
            final String sql = """
                    UPDATE public."auto"
                    SET body_type = ?
                    WHERE auto.vehicle_id = ?
                    RETURNING *;""";
            final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
            preparedStatement.setString(1, auto.getBodyType());
            preparedStatement.setString(2, auto.getId());
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
    public List<Auto> delete(Auto auto) {
        if (auto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        dbVehicleRepository.delete(auto.getId());
        return getAll();
    }
}