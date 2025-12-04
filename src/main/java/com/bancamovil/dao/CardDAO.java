package com.bancamovil.dao;

import com.bancamovil.models.Card;
import com.bancamovil.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    
    /**
     * Crea una nueva tarjeta en la base de datos
     */
    public boolean crearTarjeta(Card tarjeta) {
        String sql = "INSERT INTO tarjetas (cuenta_id, numero_tarjeta, tipo_tarjeta, " +
                     "nombre_titular, fecha_vencimiento, cvv, limite_credito, activa, fecha_emision) " + // <<< AÑADIDO fecha_emision
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, tarjeta.getCuentaId(), Types.INTEGER);
            stmt.setString(2, tarjeta.getNumeroTarjeta());
            stmt.setString(3, tarjeta.getTipoTarjeta().toString());
            stmt.setString(4, tarjeta.getNombreTitular());
            stmt.setDate(5, Date.valueOf(tarjeta.getFechaVencimiento()));
            stmt.setString(6, tarjeta.getCvv());
            stmt.setBigDecimal(7, tarjeta.getLimiteCredito());
            stmt.setBoolean(8, tarjeta.isActiva());
            stmt.setDate(9, tarjeta.getFechaEmision() != null ? Date.valueOf(tarjeta.getFechaEmision()) : null); // <<< AÑADIDO
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al crear tarjeta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todas las tarjetas de un usuario
     */
    public List<Card> obtenerTarjetasPorUsuario(int userId) {
        List<Card> tarjetas = new ArrayList<>();
        String sql = "SELECT t.* FROM tarjetas t " +
                     "INNER JOIN cuentas c ON t.cuenta_id = c.id " +
                     "WHERE c.usuario_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tarjetas.add(mapear(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener tarjetas por usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tarjetas;
    }
    
    /**
     * Obtiene una tarjeta por su ID
     */
    public Card obtenerPorId(int id) {
        String sql = "SELECT * FROM tarjetas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener tarjeta por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Bloquea o desbloquea una tarjeta
     */
    public boolean cambiarEstadoTarjeta(int id, boolean activa) {
        String sql = "UPDATE tarjetas SET activa = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, activa);
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al cambiar estado de tarjeta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Verifica si ya existe una tarjeta con ese número
     */
    public boolean existeNumeroTarjeta(String numeroTarjeta) {
        String sql = "SELECT COUNT(*) FROM tarjetas WHERE numero_tarjeta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroTarjeta);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("Error al verificar número de tarjeta: " + e.getMessage());
        }
        
        return false;
    }
    
    private Card mapear(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId(rs.getInt("id"));
        card.setCuentaId((Integer) rs.getObject("cuenta_id"));
        card.setNumeroTarjeta(rs.getString("numero_tarjeta"));
        card.setTipoTarjeta(Card.TipoTarjeta.valueOf(rs.getString("tipo_tarjeta")));
        card.setNombreTitular(rs.getString("nombre_titular"));
        
        // Mapeo de fechas
        if (rs.getDate("fecha_vencimiento") != null) {
            card.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        }
        if (rs.getTimestamp("fecha_emision") != null) { // Usamos Timestamp si el campo es TIMESTAMP o DATETIME
             card.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime().toLocalDate()); // <<< Mapeo de Fecha Emision
        }
        
        card.setCvv(rs.getString("cvv"));
        card.setLimiteCredito(rs.getBigDecimal("limite_credito"));
        card.setActiva(rs.getBoolean("activa"));
        return card;
    }
}