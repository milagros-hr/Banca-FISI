package com.bancamovil.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_HOST = "2kd6ad.h.filess.io";
    private static final String DB_PORT = "61002";
    private static final String DB_USER = "ATM_BancaDigital_fallhairgo";
    private static final String DB_PASSWORD = "8adc348d9999345a8acc5368e7a5bee91b63c802";
    private static final String DB_NAME = "ATM_BancaDigital_fallhairgo";

    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=UTC";

    // ✅ Devuelve una NUEVA conexión siempre
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado");
            return null;

        } catch (SQLException e) {
            System.err.println("❌ Error conectando a MySQL: " + e.getMessage());
            return null;
        }
    }
}
