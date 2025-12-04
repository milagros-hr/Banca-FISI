package com.bancamovil;

import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.bancamovil.db.DatabaseConnection;
import com.bancamovil.ui.LoginFrame;

public class Main {
    
    public static void main(String[] args) {
        // Configurar Look and Feel para que se vea nativo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel del sistema");
            // Continuar con el Look and Feel por defecto
        }
        
        // Verificar conexión a base de datos antes de iniciar la aplicación
        if (!verificarConexionBaseDatos()) {
            mostrarErrorConexion();
            System.exit(1); // Terminar la aplicación si no hay conexión
        }
        
        // Iniciar aplicación en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame();
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar la aplicación.\nRevise los logs para más detalles.",
                    "Error Fatal",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    /**
     * Verifica que la conexión a la base de datos esté disponible
     */
    private static boolean verificarConexionBaseDatos() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión a base de datos verificada");
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ Error al verificar conexión: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Muestra un mensaje de error cuando no se puede conectar a la base de datos
     */
    private static void mostrarErrorConexion() {
        String mensaje = "No se pudo conectar a la base de datos.\n\n" +
                        "Posibles causas:\n" +
                        "• MySQL no está ejecutándose\n" +
                        "• La base de datos 'banca_movil' no existe\n" +
                        "• Usuario o contraseña incorrectos\n" +
                        "• Puerto 3306 bloqueado\n\n" +
                        "Revise DatabaseConnection.java y ejecute database.sql";
        
        JOptionPane.showMessageDialog(null,
            mensaje,
            "Error de Conexión a Base de Datos",
            JOptionPane.ERROR_MESSAGE);
        
        System.err.println("\n" + "=".repeat(60));
        System.err.println("ERROR DE CONEXIÓN A BASE DE DATOS");
        System.err.println("=".repeat(60));
        System.err.println(mensaje);
        System.err.println("=".repeat(60) + "\n");
    }
}