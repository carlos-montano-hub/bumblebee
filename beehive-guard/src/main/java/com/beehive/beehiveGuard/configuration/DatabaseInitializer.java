package com.beehive.beehiveGuard.configuration;

import lombok.Getter;

import java.sql.*;

public class DatabaseInitializer {

    private static final String DB_URL = System.getenv("JAVA_DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String DB_NAME = "beehive_guard";
    @Getter
    private static boolean initialized = false;

    public static void createDatabaseIfNotExists() {
        try (Connection connection = DriverManager.getConnection(DB_URL + "postgres", USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            // Check if the database exists
            ResultSet result = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname='" + DB_NAME + "';");
            if (!result.next()) {
                // If it doesn't exist, create it
                statement.executeUpdate("CREATE DATABASE " + DB_NAME);
                initialized = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
