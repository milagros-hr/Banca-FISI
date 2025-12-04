package com.bancamovil.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bancamovil.db.DatabaseConnection;
import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;

public class AccountDAO {

    /**
     * Obtiene una cuenta por su número de cuenta.
     */
    public Account obtenerPorNumero(String numero) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener cuenta por número: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    /**
     * ✅ CORREGIDO: Ahora acepta una descripción personalizada
     * Realiza una transferencia atómica manejada en Java.
     */
    public boolean transferir(int origenId, int destinoId, BigDecimal monto, String descripcion) {
        // Queries para modificar saldos
        String sqlRestar = "UPDATE cuentas SET saldo = saldo - ? WHERE id = ? AND saldo >= ? AND activa = 1";
        String sqlSumar = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ? AND activa = 1";
        
        TransactionDAO transactionDAO = new TransactionDAO(); 
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // 🛑 MANEJO DE ERROR DE CONEXIÓN
            if (conn == null) {
                System.err.println("La transferencia falló: La conexión a la DB es nula.");
                return false; 
            }

            conn.setAutoCommit(false); // *** Iniciar la transacción ***

            // 1. RESTAR de la cuenta origen
            try (PreparedStatement stmtRestar = conn.prepareStatement(sqlRestar)) {
                stmtRestar.setBigDecimal(1, monto);
                stmtRestar.setInt(2, origenId);
                stmtRestar.setBigDecimal(3, monto);
                
                if (stmtRestar.executeUpdate() == 0) {
                    conn.rollback(); 
                    return false; // Saldo insuficiente o cuenta origen inactiva.
                }
            }

            // 2. SUMAR a la cuenta destino
            try (PreparedStatement stmtSumar = conn.prepareStatement(sqlSumar)) {
                stmtSumar.setBigDecimal(1, monto);
                stmtSumar.setInt(2, destinoId);

                if (stmtSumar.executeUpdate() == 0) {
                    conn.rollback(); 
                    return false; // Cuenta destino inactiva o no existe.
                }
            }

            // 3. Registrar transacción con la descripción personalizada
            Transaction t = new Transaction(
                Transaction.TipoTransaccion.TRANSFERENCIA, 
                monto, 
                descripcion != null ? descripcion : "Transferencia interna/externa"
            );
            t.setCuentaOrigenId(origenId);
            t.setCuentaDestinoId(destinoId);
            
            // Usamos el método que recibe la conexión compartida
            if (!transactionDAO.registrar(t, conn)) { 
                conn.rollback(); 
                return false; // Falló el registro, deshacer todo.
            }

            conn.commit(); // *** Confirmar si todo fue exitoso ***
            return true;

        } catch (SQLException e) {
            System.err.println("Error al transferir: " + e.getMessage());
            try { 
                if (conn != null) conn.rollback(); // Rollback en caso de cualquier error SQL
            } catch (SQLException ex) { 
                // Ignorar error de rollback
            }
            e.printStackTrace();
            return false;
        } finally {
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaurar autocommit
                    conn.close(); // Cerrar la conexión principal
                }
            } catch (SQLException ex) { 
                // Ignorar error de cierre de conexión
            }
        }
    }

    /**
     * Aumenta el saldo de una cuenta (Depósito/Recarga).
     */
    public boolean depositar(int cuentaId, BigDecimal monto) {
        String sql = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, monto);
            stmt.setInt(2, cuentaId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al depositar: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Disminuye el saldo de una cuenta, verificando saldo suficiente (Pago).
     */
    public boolean pagarServicio(int cuentaId, BigDecimal monto) {
        String sql = "UPDATE cuentas SET saldo = saldo - ? WHERE id = ? AND saldo >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, monto);
            stmt.setInt(2, cuentaId);
            stmt.setBigDecimal(3, monto);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al pagar servicio: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene todas las cuentas asociadas a un usuario específico.
     */
    public List<Account> obtenerCuentasPorUsuario(int userId) {
        List<Account> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM cuentas WHERE usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cuentas.add(map(rs));
            }

        } catch (Exception e) {
            System.err.println("Error al obtener cuentas por usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return cuentas;
    }

    /**
     * Obtiene una cuenta por su ID.
     */
    public Account obtenerPorId(int id) {
        String sql = "SELECT * FROM cuentas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener cuenta por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Crea una nueva cuenta en la base de datos.
     */
    public boolean crearCuenta(Account nuevaCuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, saldo, tipo_cuenta, moneda, usuario_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaCuenta.getNumeroCuenta());
            stmt.setBigDecimal(2, nuevaCuenta.getSaldo());
            stmt.setString(3, nuevaCuenta.getTipoCuenta().toString());
            stmt.setString(4, nuevaCuenta.getMoneda().toString());
            stmt.setInt(5, nuevaCuenta.getUsuarioId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Mapea un ResultSet a un objeto Account.
     */
    private Account map(ResultSet rs) throws Exception {
        Account a = new Account();
        a.setId(rs.getInt("id"));
        a.setNumeroCuenta(rs.getString("numero_cuenta")); 
        a.setSaldo(rs.getBigDecimal("saldo"));
        a.setUsuarioId(rs.getInt("usuario_id"));
        a.setActiva(rs.getBoolean("activa"));

        
        // Mapear TipoCuenta y Moneda desde la DB
        String tipoCuentaStr = rs.getString("tipo_cuenta");
        if (tipoCuentaStr != null) {
            a.setTipoCuenta(Account.TipoCuenta.valueOf(tipoCuentaStr));
        }
        
        String monedaStr = rs.getString("moneda");
        if (monedaStr != null) {
            a.setMoneda(Account.Moneda.valueOf(monedaStr));
        }

        return a;
    }
}