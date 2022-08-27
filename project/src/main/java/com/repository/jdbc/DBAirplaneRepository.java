package com.repository.jdbc;

import com.model.vehicle.Airplane;
import com.repository.CrudRepository;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DBAirplaneRepository implements CrudRepository<Airplane> {
    private static DBAirplaneRepository instance;
    private static DBVehicleRepository dbVehicleRepository;

    private DBAirplaneRepository() {
    }

    public static DBAirplaneRepository getInstance() {
        if (instance == null) {
            dbVehicleRepository = DBVehicleRepository.getInstance();
            instance = new DBAirplaneRepository();
        }
        return instance;
    }

    @Override
    public Optional<Airplane> findById(String id) {
        return dbVehicleRepository.findById(id)
                .map(airplane -> (Airplane) airplane);
    }

    @Override
    public List<Airplane> getAll() {
        return dbVehicleRepository.getAll().stream()
                .filter(vehicle -> vehicle.getType().getTitle().equalsIgnoreCase("airplane"))
                .map(vehicle -> (Airplane) vehicle)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public boolean save(Airplane airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("Airplane must not be null");
        }
        if (dbVehicleRepository.findById(airplane.getId()).isEmpty()) {
            if (dbVehicleRepository.save(airplane)) {
                final String sql = """
                        INSERT INTO public."airplane" (vehicle_id, number_of_passenger_seats)
                        VALUES (?, ?)
                        RETURNING *;""";
                final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
                preparedStatement.setString(1, airplane.getId());
                preparedStatement.setInt(2, airplane.getNumberOfPassengerSeats());
                final ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } else {
            return update(airplane);
        }
        return false;
    }

    @Override
    public boolean save(List<Airplane> airplaneList) {
        if (airplaneList.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        airplaneList.forEach(this::save);
        return true;
    }

    @Override
    @SneakyThrows
    public boolean update(Airplane airplane) {
        if (dbVehicleRepository.update(airplane)) {
            final String sql = """
                    UPDATE public."airplane"
                    SET number_of_passenger_seats = ?
                    WHERE airplane.vehicle_id = ?
                    RETURNING *;""";
            final PreparedStatement preparedStatement = dbVehicleRepository.connection.prepareStatement(sql);
            preparedStatement.setInt(1, airplane.getNumberOfPassengerSeats());
            preparedStatement.setString(2, airplane.getId());
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
    public List<Airplane> delete(Airplane airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("Airplane must not be null");
        }
        dbVehicleRepository.delete(airplane.getId());
        return getAll();
    }
}