package com.bancamovil.services;

import com.bancamovil.dao.UserDAO;
import com.bancamovil.models.User;

/**
 * Servicio de Usuario
 * Lógica de negocio relacionada con usuarios
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Obtener usuario por ID
     */
    public User getUserById(int id) {
        return userDAO.obtenerPorId(id);
    }
    
    /**
     * Obtener usuario por DNI
     */
    public User getUserByDni(String dni) {
        return userDAO.obtenerPorDni(dni);
    }
    
    /**
     * Validar DNI (8 dígitos numéricos)
     */
    public boolean validateDni(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        
        return dni.length() == 8 && dni.matches("\\d+");
    }
    
    /**
     * Validar email
     */
    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Validar contraseña
     */
    public boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        return password.length() >= 4;
    }
}