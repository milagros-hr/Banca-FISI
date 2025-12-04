package com.bancamovil.dao;

import com.bancamovil.db.DatabaseConnection;
import com.bancamovil.models.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Registra una nueva transacción en la base de datos (Usa su propia conexión).
     * Útil para Depósitos o Pagos Simples.
     */
    public boolean registrar(Transaction t) {
        // CORREGIDO: Usamos 'tipo_transaccion'
        String sql = "INSERT INTO transacciones " +
                "(tipo_transaccion, monto, descripcion, cuenta_origen_id, cuenta_destino_id, fecha_transaccion) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTipo().toString());
            stmt.setBigDecimal(2, t.getMonto());
            stmt.setString(3, t.getDescripcion());
            stmt.setObject(4, t.getCuentaOrigenId(), Types.INTEGER);
            stmt.setObject(5, t.getCuentaDestinoId(), Types.INTEGER);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al registrar transacción simple: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Registra una transacción usando una conexión ya abierta (para Transacciones Atómicas).
     */
    public boolean registrar(Transaction t, Connection conn) throws SQLException {
        String sql = "INSERT INTO transacciones " +
                "(tipo_transaccion, monto, descripcion, cuenta_origen_id, cuenta_destino_id, fecha_transaccion) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        // Usamos try-with-resources, pero sin cerrar la conexión (conn) que fue pasada.
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTipo().toString());
            stmt.setBigDecimal(2, t.getMonto());
            stmt.setString(3, t.getDescripcion());
            stmt.setObject(4, t.getCuentaOrigenId(), Types.INTEGER);
            stmt.setObject(5, t.getCuentaDestinoId(), Types.INTEGER);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar transacción ATÓMICA: " + e.getMessage());
            throw e; // Relanzar para que AccountDAO haga el ROLLBACK
        }
    }


    /**
     * Obtiene las últimas transacciones para una cuenta específica.
     */
    public List<Transaction> obtenerUltimas(int cuentaId, int cantidad) {
        List<Transaction> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones WHERE cuenta_origen_id = ? OR cuenta_destino_id = ? " +
                     "ORDER BY fecha_transaccion DESC LIMIT ?"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);
            stmt.setInt(2, cuentaId);
            stmt.setInt(3, cantidad);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener últimas transacciones: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtiene un número limitado de transacciones para una cuenta (usado también para historial).
     */
    public List<Transaction> obtenerPorCuenta(int cuentaId, int limit) {
        List<Transaction> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones WHERE cuenta_origen_id = ? OR cuenta_destino_id = ? " +
                     "ORDER BY fecha_transaccion DESC LIMIT ?"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);
            stmt.setInt(2, cuentaId);
            stmt.setInt(3, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener transacciones por cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Mapea el ResultSet a un objeto Transaction.
     */
    private Transaction mapear(ResultSet rs) throws Exception {
        Transaction t = new Transaction(
            // CORREGIDO: Usamos el nombre de columna real 'tipo_transaccion'
            Transaction.TipoTransaccion.valueOf(rs.getString("tipo_transaccion")), 
            rs.getBigDecimal("monto"),
            rs.getString("descripcion")
        );

        t.setId(rs.getInt("id"));
        t.setCuentaOrigenId((Integer) rs.getObject("cuenta_origen_id"));
        t.setCuentaDestinoId((Integer) rs.getObject("cuenta_destino_id"));
        t.setFecha(rs.getTimestamp("fecha_transaccion").toLocalDateTime());
        
        // Cargar estado - si existe en la BD, usarlo; sino, asumir EXITOSA
        try {
            String estadoStr = rs.getString("estado");
            if (estadoStr != null && !estadoStr.isEmpty()) {
                t.setEstado(Transaction.EstadoTransaccion.valueOf(estadoStr));
            } else {
                t.setEstado(Transaction.EstadoTransaccion.EXITOSA);
            }
        } catch (Exception e) {
            // Si la columna no existe o hay error, usar EXITOSA por defecto
            t.setEstado(Transaction.EstadoTransaccion.EXITOSA);
        }

        return t;
    }
}