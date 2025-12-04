package com.bancamovil.utils;

import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFExporter {
    
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(99, 102, 241);
    private static final DeviceRgb SUCCESS_COLOR = new DeviceRgb(34, 197, 94);
    private static final DeviceRgb ERROR_COLOR = new DeviceRgb(239, 68, 68);
    private static final DeviceRgb DARK_COLOR = new DeviceRgb(30, 41, 59);
    private static final DeviceRgb GRAY_COLOR = new DeviceRgb(100, 116, 139);
    private static final DeviceRgb WHITE_COLOR = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 250, 252);
    
    public static boolean exportarHistorial(List<Transaction> transacciones, Account cuenta, String rutaArchivo) {
        try {
            PdfWriter writer = new PdfWriter(rutaArchivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(40, 40, 40, 40);
            
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            // ENCABEZADO
            document.add(new Paragraph("BANCA DIGITAL")
                    .setFont(fontBold)
                    .setFontSize(24)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Historial de Transacciones")
                    .setFont(fontRegular)
                    .setFontSize(14)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));
            
            // INFO DE LA CUENTA
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);
            
            infoTable.addCell(createInfoCell("Número de Cuenta", cuenta.getNumeroCuenta(), fontBold, fontRegular));
            infoTable.addCell(createInfoCell("Tipo de Cuenta", cuenta.getTipoCuenta().toString(), fontBold, fontRegular));
            infoTable.addCell(createInfoCell("Saldo Actual", cuenta.getSaldoFormateado(), fontBold, fontRegular));
            
            document.add(infoTable);
            
            // TABLA DE TRANSACCIONES
            document.add(new Paragraph("Detalle de Movimientos")
                    .setFont(fontBold)
                    .setFontSize(12)
                    .setFontColor(DARK_COLOR)
                    .setMarginBottom(10));
            
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 3, 2, 1.5f}))
                    .useAllAvailableWidth();
            
            // Headers
            String[] headers = {"Fecha", "Tipo", "Descripción", "Monto", "Estado"};
            for (String header : headers) {
                Cell headerCell = new Cell()
                        .add(new Paragraph(header).setFont(fontBold).setFontSize(10).setFontColor(WHITE_COLOR))
                        .setBackgroundColor(DARK_COLOR)
                        .setPadding(8)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }
            
            // Filas
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            boolean alternar = false;
            
            for (Transaction t : transacciones) {
                DeviceRgb rowColor = alternar ? LIGHT_GRAY : WHITE_COLOR;
                alternar = !alternar;
                
                // Fecha
                String fecha = t.getFechaTransaccion() != null ? 
                        t.getFechaTransaccion().format(dateFormatter) : "-";
                table.addCell(createDataCell(fecha, fontRegular, rowColor));
                
                // Tipo
                String tipo = formatearTipo(t.getTipoTransaccion().toString());
                table.addCell(createDataCell(tipo, fontRegular, rowColor));
                
                // Descripción
                String descripcion = t.getDescripcion() != null ? t.getDescripcion() : "-";
                if (descripcion.length() > 30) {
                    descripcion = descripcion.substring(0, 27) + "...";
                }
                table.addCell(createDataCell(descripcion, fontRegular, rowColor));
                
                // Monto
                boolean esIngreso = t.getCuentaDestinoId() != null && 
                        t.getCuentaDestinoId().equals(cuenta.getId()) &&
                        (t.getCuentaOrigenId() == null || !t.getCuentaOrigenId().equals(cuenta.getId()));
                
                String monto = (esIngreso ? "+ " : "- ") + cuenta.getMoneda() + " " + 
                        String.format("%.2f", t.getMonto());
                DeviceRgb montoColor = esIngreso ? SUCCESS_COLOR : ERROR_COLOR;
                
                Cell montoCell = new Cell()
                        .add(new Paragraph(monto).setFont(fontBold).setFontSize(9).setFontColor(montoColor))
                        .setBackgroundColor(rowColor)
                        .setPadding(6)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.RIGHT);
                table.addCell(montoCell);
                
                // Estado
                String estado = t.getEstado() != null ? t.getEstado().toString() : "EXITOSA";
                table.addCell(createDataCell(estado, fontRegular, rowColor));
            }
            
            document.add(table);
            
            // PIE DE PÁGINA
            document.add(new Paragraph("\n"));
            String fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            document.add(new Paragraph("Total de transacciones: " + transacciones.size() + 
                    "  |  Generado: " + fechaGeneracion)
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Este documento es un comprobante oficial de Banca Digital.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));
            
            document.close();
            System.out.println("✅ PDF exportado exitosamente: " + rutaArchivo);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static Cell createInfoCell(String label, String value, PdfFont fontBold, PdfFont fontRegular) {
        Cell cell = new Cell()
                .add(new Paragraph(label).setFont(fontRegular).setFontSize(9).setFontColor(GRAY_COLOR))
                .add(new Paragraph(value).setFont(fontBold).setFontSize(11).setFontColor(DARK_COLOR))
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(10)
                .setBorder(new SolidBorder(new DeviceRgb(226, 232, 240), 1))
                .setTextAlignment(TextAlignment.CENTER);
        return cell;
    }
    
    private static Cell createDataCell(String content, PdfFont font, DeviceRgb bgColor) {
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(9).setFontColor(DARK_COLOR))
                .setBackgroundColor(bgColor)
                .setPadding(6)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }
    
    private static String formatearTipo(String tipo) {
        return switch (tipo) {
            case "TRANSFERENCIA" -> "Transferencia";
            case "DEPOSITO" -> "Depósito";
            case "RETIRO" -> "Retiro";
            case "PAGO_SERVICIO" -> "Pago Servicio";
            case "RECARGA" -> "Recarga";
            default -> tipo;
        };
    }
    
    /**
     * Genera un comprobante PDF para una transacción individual.
     */
    public static boolean exportarComprobante(Transaction transaccion, Account cuenta, String rutaArchivo) {
        try {
            PdfWriter writer = new PdfWriter(rutaArchivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A5);
            document.setMargins(30, 30, 30, 30);
            
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            // LOGO / ENCABEZADO
            document.add(new Paragraph("BANCA DIGITAL")
                    .setFont(fontBold)
                    .setFontSize(22)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Comprobante de Operación")
                    .setFont(fontRegular)
                    .setFontSize(12)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));
            
            // TIPO DE OPERACIÓN
            String tipoFormateado = formatearTipo(transaccion.getTipoTransaccion().toString());
            DeviceRgb tipoColor = getTipoColor(transaccion.getTipoTransaccion().toString());
            
            document.add(new Paragraph(tipoFormateado.toUpperCase())
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setFontColor(tipoColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15));
            
            // LÍNEA SEPARADORA
            Table separador = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            separador.addCell(new Cell().setHeight(2).setBackgroundColor(LIGHT_GRAY).setBorder(Border.NO_BORDER));
            document.add(separador);
            document.add(new Paragraph("\n"));
            
            // DETALLES DE LA TRANSACCIÓN
            Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1.5f}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            // Fecha y Hora
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String fecha = transaccion.getFechaTransaccion() != null ? 
                    transaccion.getFechaTransaccion().format(dateFormatter) : "-";
            String hora = transaccion.getFechaTransaccion() != null ? 
                    transaccion.getFechaTransaccion().format(timeFormatter) : "-";
            
            detailsTable.addCell(createLabelCell("Fecha:", fontBold));
            detailsTable.addCell(createValueCell(fecha, fontRegular));
            
            detailsTable.addCell(createLabelCell("Hora:", fontBold));
            detailsTable.addCell(createValueCell(hora, fontRegular));
            
            detailsTable.addCell(createLabelCell("N° Operación:", fontBold));
            detailsTable.addCell(createValueCell(String.format("%08d", transaccion.getId()), fontRegular));
            
            detailsTable.addCell(createLabelCell("Cuenta:", fontBold));
            detailsTable.addCell(createValueCell(cuenta.getNumeroCuenta(), fontRegular));
            
            detailsTable.addCell(createLabelCell("Descripción:", fontBold));
            String desc = transaccion.getDescripcion() != null ? transaccion.getDescripcion() : "-";
            detailsTable.addCell(createValueCell(desc, fontRegular));
            
            document.add(detailsTable);
            
            // MONTO (destacado)
            boolean esIngreso = transaccion.getCuentaDestinoId() != null && 
                    transaccion.getCuentaDestinoId().equals(cuenta.getId()) &&
                    (transaccion.getCuentaOrigenId() == null || !transaccion.getCuentaOrigenId().equals(cuenta.getId()));
            
            String signo = esIngreso ? "+ " : "- ";
            DeviceRgb montoColor = esIngreso ? SUCCESS_COLOR : ERROR_COLOR;
            String montoStr = signo + cuenta.getMoneda() + " " + String.format("%.2f", transaccion.getMonto());
            
            Table montoTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            Cell montoCell = new Cell()
                    .add(new Paragraph("MONTO").setFont(fontRegular).setFontSize(10).setFontColor(GRAY_COLOR))
                    .add(new Paragraph(montoStr).setFont(fontBold).setFontSize(20).setFontColor(montoColor))
                    .setBackgroundColor(LIGHT_GRAY)
                    .setPadding(15)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER);
            montoTable.addCell(montoCell);
            document.add(montoTable);
            
            document.add(new Paragraph("\n"));
            
            // ESTADO
            String estado = transaccion.getEstado() != null ? transaccion.getEstado().toString() : "EXITOSA";
            DeviceRgb estadoColor = estado.equals("EXITOSA") ? SUCCESS_COLOR : GRAY_COLOR;
            
            document.add(new Paragraph("Estado: " + estado)
                    .setFont(fontBold)
                    .setFontSize(11)
                    .setFontColor(estadoColor)
                    .setTextAlignment(TextAlignment.CENTER));
            
            // PIE DE PÁGINA
            document.add(new Paragraph("\n\n"));
            String fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            document.add(new Paragraph("Generado: " + fechaGeneracion)
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("Este documento es un comprobante oficial de Banca Digital.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setFontColor(GRAY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5));
            
            document.close();
            System.out.println("✅ Comprobante PDF exportado: " + rutaArchivo);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al exportar comprobante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static Cell createLabelCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(GRAY_COLOR))
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(8)
                .setTextAlignment(TextAlignment.LEFT);
    }
    
    private static Cell createValueCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(DARK_COLOR))
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(8)
                .setTextAlignment(TextAlignment.LEFT);
    }
    
    private static DeviceRgb getTipoColor(String tipo) {
        return switch (tipo) {
            case "TRANSFERENCIA" -> new DeviceRgb(14, 165, 233);   // Cyan
            case "DEPOSITO" -> new DeviceRgb(34, 197, 94);         // Verde
            case "RETIRO" -> new DeviceRgb(249, 115, 22);          // Naranja
            case "PAGO_SERVICIO" -> new DeviceRgb(236, 72, 153);   // Rosa
            case "RECARGA" -> new DeviceRgb(168, 85, 247);         // Púrpura
            default -> GRAY_COLOR;
        };
    }
}
