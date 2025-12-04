package com.bancamovil.utils;

import com.bancamovil.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class LimpiarBaseDatos {
    
    public static void main(String[] args) {
        System.out.println("⚠️  LIMPIANDO TODA LA BASE DE DATOS...\n");
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Deshabilitar verificación de claves foráneas
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Limpiar todas las tablas
            stmt.executeUpdate("TRUNCATE TABLE transacciones");
            System.out.println("✅ Tabla 'transacciones' limpiada");
            
            stmt.executeUpdate("TRUNCATE TABLE cuentas");
            System.out.println("✅ Tabla 'cuentas' limpiada");
            
            stmt.executeUpdate("TRUNCATE TABLE usuarios");
            System.out.println("✅ Tabla 'usuarios' limpiada");
            
            // Reactivar verificación de claves foráneas
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            
            System.out.println("\n🎉 BASE DE DATOS LIMPIADA EXITOSAMENTE");
            System.out.println("Todas las tablas están vacías.");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}