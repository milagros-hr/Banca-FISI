package com.bancamovil.services;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.TransactionDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.bancamovil.models.Transaction.TipoTransaccion;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio de Transacciones
 * Lógica de negocio para operaciones bancarias
 */
public class TransactionService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    
    public TransactionService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }
    
    /**
     * Realizar transferencia entre cuentas
     * ✅ CORREGIDO: Ahora pasa la descripción a AccountDAO.transferir()
     */
    public boolean transfer(Account cuentaOrigen, String numeroCuentaDestino, BigDecimal monto, String descripcion) {

        if (cuentaOrigen == null || numeroCuentaDestino == null || monto == null) return false;
        if (monto.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (monto.compareTo(cuentaOrigen.getSaldo()) > 0) return false;

        // Verificar cuenta destino
        Account cuentaDestino = accountDAO.obtenerPorNumero(numeroCuentaDestino);
        if (cuentaDestino == null) return false;
        if (cuentaDestino.getId() == cuentaOrigen.getId()) return false;

        // Preparar descripción
        String desc = (descripcion != null && !descripcion.isEmpty())
                ? descripcion
                : "Transferencia a " + numeroCuentaDestino;

        // ✅ SOLUCIÓN: Pasamos la descripción al método transferir()
        // AccountDAO.transferir() se encarga de registrar la transacción internamente
        boolean exito = accountDAO.transferir(
            cuentaOrigen.getId(), 
            cuentaDestino.getId(), 
            monto,
            desc  // ← Ahora pasamos la descripción personalizada
        );
        
        return exito;
    }
    
    /**
     * Realizar recarga / depósito
     */
    public boolean recharge(Account cuenta, BigDecimal monto) {
        if (cuenta == null || monto == null) return false;
        if (monto.compareTo(new BigDecimal("1.00")) < 0) return false;
        if (monto.compareTo(new BigDecimal("10000.00")) > 0) return false;

        boolean exito = accountDAO.depositar(cuenta.getId(), monto);

        if (exito) {
            Transaction transaccion = new Transaction(
                TipoTransaccion.RECARGA,
                monto,
                "Recarga de saldo"
            );
            transaccion.setCuentaOrigenId(cuenta.getId());
            transactionDAO.registrar(transaccion);

            // actualizar copia local
            cuenta.setSaldo(cuenta.getSaldo().add(monto));
        }

        return exito;
    }
    
    /**
     * Pago de servicios
     */
    public boolean payService(Account cuenta, String servicio, String codigo, BigDecimal monto) {

        if (cuenta == null || servicio == null || monto == null) return false;
        if (monto.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (monto.compareTo(cuenta.getSaldo()) > 0) return false;

        boolean exito = accountDAO.pagarServicio(cuenta.getId(), monto);

        if (exito) {
            Transaction transaccion = new Transaction(
                TipoTransaccion.PAGO_SERVICIO,
                monto,
                "Pago " + servicio + 
                    ((codigo != null && !codigo.isEmpty()) ? " - Código: " + codigo : "")
            );
            transaccion.setCuentaOrigenId(cuenta.getId());
            transactionDAO.registrar(transaccion);

            // actualizar copia local
            cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        }

        return exito;
    }
    
    /**
     * Historial completo o limitado
     */
    public List<Transaction> getTransactionHistory(Account cuenta, int limit) {
        if (cuenta == null) return null;
        return transactionDAO.obtenerPorCuenta(cuenta.getId(), limit);
    }
    
    /**
     * Últimas transacciones (para dashboard)
     */
    public List<Transaction> getRecentTransactions(Account cuenta, int count) {
        if (cuenta == null) return null;
        return transactionDAO.obtenerUltimas(cuenta.getId(), count);
    }
    
    public boolean validateAmount(BigDecimal monto) {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean hasSufficientBalance(Account cuenta, BigDecimal monto) {
        return cuenta != null && monto != null && cuenta.getSaldo().compareTo(monto) >= 0;
    }
}