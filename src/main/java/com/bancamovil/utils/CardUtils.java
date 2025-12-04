package com.bancamovil.utils;

import java.time.LocalDate;
import java.util.Random;

public class CardUtils {
    
    private static final Random random = new Random();
    
    /**
     * Genera un número de tarjeta válido según el algoritmo de Luhn
     * @param tipoRed "VISA" o "MASTERCARD"
     * @param bin BIN del banco (primeros 6 dígitos), si es null se genera uno aleatorio
     * @return Número de tarjeta de 16 dígitos
     */
    public static String generarNumeroTarjeta(String tipoRed, String bin) {
        StringBuilder numero = new StringBuilder();
        
        // Primer dígito según la red
        if ("VISA".equalsIgnoreCase(tipoRed)) {
            numero.append("4");
        } else if ("MASTERCARD".equalsIgnoreCase(tipoRed)) {
            numero.append("5");
        } else {
            numero.append("4"); // Por defecto VISA
        }
        
        // Si no se proporciona BIN, generar uno aleatorio
        if (bin == null || bin.isEmpty()) {
            // Generar 5 dígitos más para completar los 6 del BIN
            for (int i = 0; i < 5; i++) {
                numero.append(random.nextInt(10));
            }
        } else {
            // Usar el BIN proporcionado (sin el primer dígito que ya pusimos)
            numero.append(bin.substring(1, Math.min(6, bin.length())));
            // Completar si es necesario
            while (numero.length() < 6) {
                numero.append(random.nextInt(10));
            }
        }
        
        // Generar dígitos 7 a 15 (9 dígitos)
        for (int i = 0; i < 9; i++) {
            numero.append(random.nextInt(10));
        }
        
        // Calcular y agregar el dígito verificador (Luhn)
        int digitoVerificador = calcularDigitoLuhn(numero.toString());
        numero.append(digitoVerificador);
        
        return numero.toString();
    }
    
    /**
     * Calcula el dígito verificador según el algoritmo de Luhn
     */
    private static int calcularDigitoLuhn(String numero) {
        int suma = 0;
        boolean alternar = true;
        
        // Recorrer de derecha a izquierda
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            
            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            
            suma += digito;
            alternar = !alternar;
        }
        
        return (10 - (suma % 10)) % 10;
    }
    
    /**
     * Valida si un número de tarjeta es válido según Luhn
     */
    public static boolean validarNumeroTarjeta(String numero) {
        if (numero == null || numero.length() != 16) {
            return false;
        }
        
        int suma = 0;
        boolean alternar = false;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            
            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            
            suma += digito;
            alternar = !alternar;
        }
        
        return (suma % 10) == 0;
    }
    
    /**
     * Genera un CVV aleatorio de 3 dígitos
     */
    public static String generarCVV() {
        return String.format("%03d", random.nextInt(1000));
    }
    
    /**
     * Genera una fecha de vencimiento (5 años desde hoy)
     */
    public static LocalDate generarFechaVencimiento() {
        return LocalDate.now().plusYears(5);
    }
    
    /**
     * Determina la red de la tarjeta según el primer dígito
     */
    public static String obtenerRedTarjeta(String numero) {
        if (numero == null || numero.isEmpty()) {
            return "DESCONOCIDA";
        }
        
        char primerDigito = numero.charAt(0);
        
        if (primerDigito == '4') {
            return "VISA";
        } else if (primerDigito == '5') {
            return "MASTERCARD";
        } else {
            return "DESCONOCIDA";
        }
    }
    
    /**
     * Formatea el número de tarjeta en grupos de 4
     * Ejemplo: 1234567890123456 -> 1234 5678 9012 3456
     */
    public static String formatearNumeroTarjeta(String numero) {
        if (numero == null || numero.length() != 16) {
            return numero;
        }
        
        return numero.substring(0, 4) + " " + 
               numero.substring(4, 8) + " " + 
               numero.substring(8, 12) + " " + 
               numero.substring(12, 16);
    }
}