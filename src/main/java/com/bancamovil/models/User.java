package com.bancamovil.models;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String passwordHash;
    private LocalDateTime fechaCreacion;
    private boolean activo;
    
    // Constructor vacío
    public User() {}
    
    // Constructor con parámetros básicos
    public User(int id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getDni() { 
        return dni; 
    }
    
    public void setDni(String dni) { 
        this.dni = dni; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getApellido() { 
        return apellido; 
    }
    
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getTelefono() { 
        return telefono; 
    }
    
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
    
    public LocalDateTime getFechaCreacion() { 
        return fechaCreacion; 
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }
    
    public boolean isActivo() { 
        return activo; 
    }
    
    public void setActivo(boolean activo) { 
        this.activo = activo; 
    }
    
    public String getNombreCompleto() { 
        return nombre + " " + apellido; 
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", dni='" + dni + "', nombre='" + nombre + "'}";
    }
}