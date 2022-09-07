package com.service;

import com.config.JDBCConfig;
import lombok.SneakyThrows;

import java.sql.Statement;

public class DBTableService {
    private static Statement statement;
    private static DBTableService instance;

    private DBTableService() {
    }

    @SneakyThrows
    public static DBTableService getInstance() {
        if (instance == null) {
            instance = new DBTableService();
            statement = JDBCConfig.getConnection().createStatement();
        }
        return instance;
    }

    public void createTables() {
        createInvoice();
        createVehicle();
        createDetail();
        createAuto();
        createAirplane();
        createMotorbike();
        createEngine();
    }

    @SneakyThrows
    public void dropTables() {
        final String sql = """
                DROP TABLE IF EXISTS invoice CASCADE;
                DROP TABLE IF EXISTS vehicle CASCADE;
                DROP TABLE IF EXISTS detail CASCADE;
                DROP TABLE IF EXISTS auto CASCADE;
                DROP TABLE IF EXISTS airplane CASCADE;
                DROP TABLE IF EXISTS motorbike CASCADE;
                DROP TABLE IF EXISTS engine CASCADE;""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createInvoice() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS invoice (
                invoice_id VARCHAR(255) NOT NULL,
                created TIMESTAMPTZ,
                PRIMARY KEY (invoice_id));""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createVehicle() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS vehicle (
                vehicle_id VARCHAR(255) NOT NULL,
                manufacturer VARCHAR(255),
                type VARCHAR(255),
                price NUMERIC,
                model VARCHAR(255),
                count INTEGER,
                invoice_id VARCHAR(255),
                PRIMARY KEY (vehicle_id),
                CONSTRAINT fk_invoice
                FOREIGN KEY(invoice_id)
                REFERENCES invoice(invoice_id)
                ON DELETE CASCADE);""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createDetail() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS detail (
                vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES vehicle(vehicle_id) ON DELETE CASCADE,
                name VARCHAR(255));""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createAuto() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS auto (
                vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES vehicle(vehicle_id) ON DELETE CASCADE,
                body_type VARCHAR(255));""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createAirplane() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS airplane (
                vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES vehicle(vehicle_id) ON DELETE CASCADE,
                number_of_passenger_seats INTEGER);""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createMotorbike() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS motorbike (
                vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES vehicle(vehicle_id) ON DELETE CASCADE,
                created_motorbike TIMESTAMPTZ,
                lean_angle DOUBLE PRECISION,
                currency VARCHAR(255));""";
        statement.executeUpdate(sql);
    }

    @SneakyThrows
    private void createEngine() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS engine (
                vehicle_id VARCHAR(255) NOT NULL PRIMARY KEY REFERENCES motorbike(vehicle_id) ON DELETE CASCADE,
                volume INTEGER,
                brand VARCHAR(255));""";
        statement.executeUpdate(sql);
    }
}