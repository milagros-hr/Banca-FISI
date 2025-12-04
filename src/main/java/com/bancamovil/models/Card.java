package com.bancamovil.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Card {
    private int id;
    private Integer cuentaId;
    private String numeroTarjeta;
    private TipoTarjeta tipoTarjeta;
    private String nombreTitular;
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmision; // <<< AÑADIDO EL CAMPO
    private String cvv;
    private BigDecimal limiteCredito;
    private boolean activa;

    public enum TipoTarjeta {
        DEBITO, CREDITO
    }
    
    // Constructor vacío
    public Card() {}
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getCuentaId() {
        return cuentaId;
    }
    
    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }
    
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }
    
    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }
    
    public String getNombreTitular() {
        return nombreTitular;
    }
    
    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    // <<< IMPLEMENTACIÓN CORREGIDA DE getFechaEmision()
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    // >>> FIN DE IMPLEMENTACIÓN CORREGIDA
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    
    public boolean isActiva() {
        return activa;
    }
    
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    /**
     * Retorna los últimos 4 dígitos enmascarados
     */
    public String getNumeroEnmascarado() {
        if (numeroTarjeta == null || numeroTarjeta.length() < 4) {
            return "****";
        }
        String ultimos4 = numeroTarjeta.substring(numeroTarjeta.length() - 4);
        return "**** **** **** " + ultimos4;
    }
    
    /**
     * Retorna la fecha en formato MM/YY
     */
    public String getFechaFormateada() {
        if (fechaVencimiento == null) return "";
        return String.format("%02d/%02d", 
            fechaVencimiento.getMonthValue(), 
            fechaVencimiento.getYear() % 100);
    }
    
    /**
     * Verifica si la tarjeta está vencida
     */
    public boolean estaVencida() {
        if (fechaVencimiento == null) return true;
        // Se compara con la fecha de hoy.
        return fechaVencimiento.isBefore(LocalDate.now());
    }
    
    public String getEstado() {
        if (estaVencida()) return "VENCIDA";
        if (!activa) return "BLOQUEADA";
        return "ACTIVA";
    }
}