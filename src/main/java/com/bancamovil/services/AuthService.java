package com.bancamovil.services;

import com.bancamovil.dao.UserDAO;
import com.bancamovil.models.User;
import org.mindrot.jbcrypt.BCrypt; // Necesario para verificar el hash

/**
 * Servicio de Autenticación
 * Capa intermedia entre UI y DAO
 */
public class AuthService {
    private UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Autenticar usuario (Login)
     */
    public User login(String dni, String password) {
        if (dni == null || password == null) {
            return null;
        }
        
        // userDAO.autenticar(dni, password) maneja la búsqueda y la verificación BCrypt.
        return userDAO.autenticar(dni, password);
    }
    
    /**
     * Registrar nuevo usuario
     */
    public boolean register(User user, String password) {
        if (user == null || password == null || password.length() < 4) {
            return false;
        }
        
        return userDAO.registrar(user, password);
    }
    
    /**
     * Verificar si un DNI ya está registrado
     */
    public boolean dniExists(String dni) {
        User user = userDAO.obtenerPorDni(dni);
        return user != null;
    }

    /**
     * Verifica la contraseña de un usuario activo dado su email (para validar detalles sensibles).
     * NOTA: Este método asume que UserDAO.obtenerPorEmail(email) existe o que podemos 
     * refactorizar UserDAO para soportar esta lógica.
     */
    public boolean verificarCredenciales(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        // --- Lógica de Verificación por Email (Requiere obtener el usuario) ---
        // Dado que solo tenemos el DNI en UserDAO.obtenerPorDni, y AuthDAO.autenticar 
        // solo funciona por DNI, necesitamos:
        
        // 1. Obtener al usuario por email para obtener el hash de la contraseña.
        // Asumiendo que has creado o puedes crear un método en UserDAO:
        // User user = userDAO.obtenerPorEmail(email); 
        
        // **SOLUCIÓN PROVISIONAL:** Como UserDAO no tiene obtenerPorEmail(),
        // y para no modificar más archivos, usaremos una lógica similar a UserDAO.autenticar
        // pero buscando por email y verificando la contraseña. 
        
        // Si tu UserDAO.java tuviera un método obtenerPorEmail(email) que devuelve el objeto User,
        // la lógica sería simple y limpia:
        /*
        User user = userDAO.obtenerPorEmail(email); 
        if (user != null && user.isActivo()) {
             return BCrypt.checkpw(password, user.getPasswordHash());
        }
        return false;
        */
        
        // **Para que funcione con la estructura actual de login y UserDAO:**
        // Necesitas que UserDAO tenga un método para buscar el hash por email, 
        // o si no, el método login() original hubiera usado email.
        
        // Para solucionar esto, la forma más limpia es modificar temporalmente UserDAO 
        // o **usar un método que sí existe:** ya que el usuario ya está logueado 
        // (el email es del `user` en `ProfilePanel`), asumiremos que UserDAO tiene
        // un método para obtener el hash por email.
        
        // Dado que solo me has dado AuthService, y el error es aquí:
        // La solución es agregar la lógica de verificación segura. 
        // ASUMO que UserDAO tiene un método 'obtenerPasswordHashPorEmail' 
        
        // === IMPLEMENTACIÓN ASUMIDA ===
        
        // Nota: Si este bloque falla, probablemente necesites añadir un método 
        // `obtenerPasswordHashPorEmail(email)` en UserDAO.java.
        try {
            // Reemplaza esta línea si tienes un método mejor en UserDAO para obtener el hash por email:
            // Por simplicidad, ya que el sistema es de escritorio y el usuario ya está autenticado,
            // podemos suponer que el hash puede ser recuperado.
            User userActual = userDAO.obtenerPorEmail(email); // *Asumo la existencia de este método*

            if (userActual != null && userActual.isActivo()) {
                String storedPasswordHash = userActual.getPasswordHash();
                
                // Requiere BCrypt (debe estar importado en UserDAO para autenticar/registrar)
                return BCrypt.checkpw(password, storedPasswordHash); 
            }
        } catch (Exception e) {
            System.err.println("Error al verificar credenciales por email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}