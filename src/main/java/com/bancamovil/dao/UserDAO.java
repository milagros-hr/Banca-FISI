package com.bancamovil.dao;

import com.bancamovil.db.DatabaseConnection;
import com.bancamovil.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {

    /**
     * Autenticar usuario (Login) con BCrypt
     */
    public User autenticar(String dni, String password) {
        String sql = "SELECT * FROM usuarios WHERE dni = ? AND activo = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");

                // Verificar la contraseña usando BCrypt
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Registrar nuevo usuario con contraseña encriptada
     */
    public boolean registrar(User user, String password) {
        String sql = "INSERT INTO usuarios (dni, nombre, apellido, email, telefono, password_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Generar salt y hashear la contraseña
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);

            stmt.setString(1, user.getDni());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getApellido());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getTelefono());
            stmt.setString(6, hashedPassword);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtener usuario por ID
     */
    public User obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtener usuario por DNI
     */
    public User obtenerPorDni(String dni) {
        String sql = "SELECT * FROM usuarios WHERE dni = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por DNI: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // =========================================================
    // NUEVAS FUNCIONES PARA VERIFICACIÓN DE CREDENCIALES POR EMAIL
    // =========================================================

    /**
     * Obtener usuario por Email, incluyendo el password_hash.
     * Necesario para verificación y recuperación de contraseña.
     */
    public User obtenerPorEmail(String email) {
        String sql = "SELECT id, dni, nombre, apellido, email, telefono, password_hash, activo " +
                     "FROM usuarios WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuarioConHash(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por Email: " + e.getMessage());
        }
        return null;
    }

    // =========================================================
    // NUEVO: ACTUALIZAR CONTRASEÑA (RECUPERAR / RESTABLECER)
    // =========================================================

    /**
     * Actualizar contraseña usando BCrypt (por ID)
     */
    public boolean actualizarPassword(int userId, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE id = ? AND activo = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generar el nuevo hash
            String salt = BCrypt.gensalt();
            String hashed = BCrypt.hashpw(nuevaPassword, salt);

            stmt.setString(1, hashed);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña (por ID): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    // =========================================================
    // MAPPERS
    // =========================================================

    /**
     * Mapea un ResultSet a un objeto User (sin password hash)
     */
    private User mapearUsuario(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setDni(rs.getString("dni"));
        user.setNombre(rs.getString("nombre"));
        user.setApellido(rs.getString("apellido"));
        user.setEmail(rs.getString("email"));
        user.setTelefono(rs.getString("telefono"));
        user.setActivo(rs.getBoolean("activo"));
        return user;
    }

    /**
     * Mapea un usuario incluyendo el hash (para comparación o verificación)
     */
    private User mapearUsuarioConHash(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setDni(rs.getString("dni"));
        user.setNombre(rs.getString("nombre"));
        user.setApellido(rs.getString("apellido"));
        user.setEmail(rs.getString("email"));
        user.setTelefono(rs.getString("telefono"));
        user.setActivo(rs.getBoolean("activo"));
        user.setPasswordHash(rs.getString("password_hash"));
        return user;
    }
}
