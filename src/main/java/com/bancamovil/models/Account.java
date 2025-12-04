package com.bancamovil.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Account {
    private int id;
    private int usuarioId;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private Moneda moneda;
    private LocalDateTime fechaApertura;
    private boolean activa;

    public Integer getCuentaOrigenId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // ELIMINADO: public Integer getCuentaOrigenId() { ... }
    
    public enum TipoCuenta {
        AHORROS, CORRIENTE, CTS
    }
    
    
    public enum Moneda {
        PEN, USD
    }
    
    // Constructor vacío
    public Account() {}
    
    // Constructor con parámetros principales
    public Account(int id, String numeroCuenta, TipoCuenta tipoCuenta, BigDecimal saldo, Moneda moneda) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.moneda = moneda;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public int getUsuarioId() { 
        return usuarioId; 
    }
    
    public void setUsuarioId(int usuarioId) { 
        this.usuarioId = usuarioId; 
    }
    
    public String getNumeroCuenta() { 
        return numeroCuenta; 
    }
    
    public void setNumeroCuenta(String numeroCuenta) { 
        this.numeroCuenta = numeroCuenta; 
    }
    
    public TipoCuenta getTipoCuenta() { 
        return tipoCuenta; 
    }
    
    public void setTipoCuenta(TipoCuenta tipoCuenta) { 
        this.tipoCuenta = tipoCuenta; 
    }
    
    public BigDecimal getSaldo() { 
        return saldo; 
    }
    
    public void setSaldo(BigDecimal saldo) { 
        this.saldo = saldo; 
    }
    
    public Moneda getMoneda() { 
        return moneda; 
    }
    
    public void setMoneda(Moneda moneda) { 
        this.moneda = moneda; 
    }
    
    public LocalDateTime getFechaApertura() { 
        return fechaApertura; 
    }
    
    public void setFechaApertura(LocalDateTime fechaApertura) { 
        this.fechaApertura = fechaApertura; 
    }
    
    public boolean isActiva() { 
        return activa; 
    }
    
    public void setActiva(boolean activa) { 
        this.activa = activa; 
    }
    
    public String getSaldoFormateado() {
        // Formato: [S/.] o [$] + Saldo
        String simbolo = moneda == Moneda.PEN ? "S/. " : "$ ";
        return simbolo + String.format("%.2f", saldo);
    }
    
    public double getBalance() {
       return saldo.doubleValue();
    }

    public void setBalance(double balance) {
        this.saldo = BigDecimal.valueOf(balance);
    }

    public String getAccountNumber() {
        return numeroCuenta;
    }

    public void setAccountNumber(String accountNumber) {
        this.numeroCuenta = accountNumber;
    }

    /**
     * Define el formato que se verá en el JComboBox.
     */
    @Override
    public String toString() {
        String saldoStr = String.format("%.2f", saldo);
        return tipoCuenta + " - " + numeroCuenta + " - " + saldoStr;
    }
}