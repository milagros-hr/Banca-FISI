package com.bancamovil.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private Integer cuentaOrigenId;
    private Integer cuentaDestinoId;
    private TipoTransaccion tipoTransaccion; 
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaTransaccion; 
    private EstadoTransaccion estado;

    // --- MÉTODOS CORREGIDOS QUE FALTABAN ---
    
    /**
     * CORREGIDO: Setter utilizado por el mapeo JDBC (TransactionDAO.mapear)
     */
    public void setFecha(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    /**
     * CORREGIDO: Getter utilizado por AccountDAO.transferir para obtener el tipo (Enum).
     */
    public TipoTransaccion getTipo() { 
        return this.tipoTransaccion;
    }

    /**
     * CORREGIDO: Getter utilizado por DashboardHomePanel para obtener la fecha (LocalDateTime).
     */
    public LocalDateTime getFecha() {
        return this.fechaTransaccion;
    }
    
    // --- ENUMS ---
    
    public enum TipoTransaccion {
        TRANSFERENCIA, 
        DEPOSITO, 
        RETIRO, 
        PAGO_SERVICIO,
        RECARGA
    }
    
    public enum EstadoTransaccion {
        EXITOSA, 
        PENDIENTE, 
        RECHAZADA
    }
    
    // --- CONSTRUCTORES ---
    
    public Transaction() {}
    
    // Constructor con parámetros principales
    public Transaction(TipoTransaccion tipo, BigDecimal monto, String descripcion) {
        this.tipoTransaccion = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaTransaccion = LocalDateTime.now();
        this.estado = EstadoTransaccion.EXITOSA;
    }
    
    // --- GETTERS & SETTERS COMPLETOS ---
        
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public Integer getCuentaOrigenId() { 
        return cuentaOrigenId; 
    }
    
    public void setCuentaOrigenId(Integer cuentaOrigenId) { 
        this.cuentaOrigenId = cuentaOrigenId; 
    }
    
    public Integer getCuentaDestinoId() { 
        return cuentaDestinoId; 
    }
    
    public void setCuentaDestinoId(Integer cuentaDestinoId) { 
        this.cuentaDestinoId = cuentaDestinoId; 
    }
    
    public TipoTransaccion getTipoTransaccion() { 
        return tipoTransaccion; 
    }
    
    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) { 
        this.tipoTransaccion = tipoTransaccion; 
    }
    
    public BigDecimal getMonto() { 
        return monto; 
    }
    
    public void setMonto(BigDecimal monto) { 
        this.monto = monto; 
    }
    
    public String getDescripcion() { 
        return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
    
    public LocalDateTime getFechaTransaccion() { 
        return fechaTransaccion; 
    }
    
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { 
        this.fechaTransaccion = fechaTransaccion; 
    }
    
    public EstadoTransaccion getEstado() { 
        return estado; 
    }
    
    public void setEstado(EstadoTransaccion estado) { 
        this.estado = estado; 
    }
    
    public String getType() {
        return tipoTransaccion.name();
    }

    public double getAmount() {
        return monto.doubleValue();
    }

    public java.util.Date getDate() {
        return java.sql.Timestamp.valueOf(fechaTransaccion); 
    }

    @Override
    public String toString() {
        return tipoTransaccion + " - S/ " + monto + " - " + descripcion;
    }
}