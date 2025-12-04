package com.bancamovil.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.bancamovil.dao.TransactionDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.bancamovil.utils.PDFExporter;

/**
 * Panel para ver el historial completo de transacciones con Gráficos Integrados
 */
public class HistoryPanel extends JPanel {
    private DashboardFrame dashboard;
    private Account cuenta;
    private TransactionDAO transactionDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private List<Transaction> allTransactions;
    private String currentFilter = "TODOS";
    private JPanel filterPanel;
    private JLabel lblTotalTransactions;

    // --- VARIABLES PARA EL GRÁFICO ---
    private DefaultCategoryDataset dataset;
    private JPanel chartPanelContainer;
    
    public HistoryPanel(DashboardFrame dashboard, Account cuenta) {
        this.dashboard = dashboard;
        this.cuenta = cuenta;
        this.transactionDAO = new TransactionDAO();
        this.dataset = new DefaultCategoryDataset();
        
        // Configuración básica del panel SIEMPRE
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setOpaque(true);
        
        try {
            initComponents();
            cargarHistorial();
        } catch (Exception e) {
            System.err.println("❌ ERROR en HistoryPanel: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar historial: " + e.getMessage());
        }
    }
    
    private void mostrarError(String mensaje) {
        removeAll();
        JLabel errorLabel = new JLabel(mensaje, SwingConstants.CENTER);
        errorLabel.setFont(UITheme.FONT_SUBTITLE);
        errorLabel.setForeground(UITheme.ERROR);
        add(errorLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void initComponents() {
        
        // ---------------------------------------------------------
        // 1. PANEL SUPERIOR (HEADER, STATS, FILTROS)
        // ---------------------------------------------------------
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        
        // Header con icono y título
        UITheme.RoundedPanel headerCard = new UITheme.RoundedPanel(20, Color.WHITE, true);
        headerCard.setLayout(new BorderLayout(20, 0));
        headerCard.setBorder(new EmptyBorder(20, 24, 20, 24));
        headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Icono grande
        JLabel iconLabel = new JLabel(UITheme.ICON_HISTORY);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        
        // Info del título
        JPanel titleInfo = new JPanel();
        titleInfo.setLayout(new BoxLayout(titleInfo, BoxLayout.Y_AXIS));
        titleInfo.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Historial de Transacciones");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(new Color(30, 41, 59));
        
        JLabel subtitleLabel = new JLabel(cuenta.getTipoCuenta() + ": " + cuenta.getNumeroCuenta());
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        
        titleInfo.add(titleLabel);
        titleInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        titleInfo.add(subtitleLabel);
        
        // Estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setOpaque(false);
        
        // Total de transacciones
        lblTotalTransactions = new JLabel("0");
        lblTotalTransactions.setFont(UITheme.FONT_MONEY_SMALL);
        lblTotalTransactions.setForeground(UITheme.PRIMARY);
        JPanel statCard = createStatCard("Total Transacciones", lblTotalTransactions);
        statsPanel.add(statCard);
        
        headerCard.add(iconLabel, BorderLayout.WEST);
        headerCard.add(titleInfo, BorderLayout.CENTER);
        headerCard.add(statsPanel, BorderLayout.EAST);
        
        // Panel de filtros - COLORES MÁS CLAROS
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBorder(new EmptyBorder(15, 0, 5, 0));
        
        JLabel filterLabel = new JLabel("Filtrar por:");
        filterLabel.setFont(UITheme.FONT_BODY_BOLD);
        filterLabel.setForeground(UITheme.TEXT_SECONDARY);
        filterPanel.add(filterLabel);
        
        String[] filters = {"TODOS", "TRANSFERENCIA", "DEPOSITO", "RETIRO", "PAGO_SERVICIO", "RECARGA"};
        String[] filterLabels = {"Todos", "Transferencias", "Depósitos", "Retiros", "Pagos", "Recargas"};
        
        // COLORES MÁS CLAROS Y VISIBLES
        Color[] filterColors = {
            new Color(99, 102, 241),   // Todos - Azul índigo claro
            new Color(14, 165, 233),   // Transferencias - Cyan claro
            new Color(34, 197, 94),    // Depósitos - Verde claro
            new Color(249, 115, 22),   // Retiros - Naranja claro
            new Color(236, 72, 153),   // Pagos - Rosa claro
            new Color(168, 85, 247)    // Recargas - Púrpura claro
        };
        
        for (int i = 0; i < filters.length; i++) {
            JButton filterBtn = createFilterButton(filterLabels[i], filters[i], filterColors[i]);
            if (i == 0) {
                filterBtn.setBackground(filterColors[i]);
                filterBtn.setForeground(Color.WHITE);
            }
            filterPanel.add(filterBtn);
        }
        
        topPanel.add(headerCard);
        topPanel.add(filterPanel);
        
        // ---------------------------------------------------------
        // 2. TABLA DE TRANSACCIONES
        // ---------------------------------------------------------
        String[] columnNames = {"Fecha", "Hora", "Tipo", "Descripción", "Monto", "Estado", "PDF"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel) {
            private int hoveredRow = -1;
            
            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }
                    }
                });
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1;
                        repaint();
                    }
                });
            }
            
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row == hoveredRow && !isCellSelected(row, column)) {
                    c.setBackground(new Color(241, 245, 249));
                }
                return c;
            }
            
            public int getHoveredRow() {
                return hoveredRow;
            }
        };
        
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(50);
        table.setBackground(new Color(248, 250, 252)); // Fondo claro para la tabla
        table.setForeground(new Color(30, 41, 59));
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 40));
        table.setSelectionForeground(new Color(30, 41, 59));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true); // IMPORTANTE: Llenar todo el viewport
        table.setOpaque(true);
        
        // Header de la tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(UITheme.TEXT_SECONDARY);
        header.setFont(UITheme.FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.PRIMARY));
        header.setReorderingAllowed(false);
        
        // Renderer para header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(248, 250, 252));
                label.setForeground(UITheme.TEXT_SECONDARY);
                label.setBorder(new EmptyBorder(0, 14, 0, 14));
                label.setFont(UITheme.FONT_BODY_BOLD);
                return label;
            }
        };
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Ajustar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(240);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setMaxWidth(70);
        table.getColumnModel().getColumn(6).setMinWidth(50);
        
        // Renderer para celdas normales con hover
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                
                Color rowColor = row % 2 == 0 ? new Color(255, 255, 255) : new Color(248, 250, 252);
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                } else {
                    label.setBackground(rowColor);
                }
                label.setForeground(new Color(30, 41, 59));
                
                if (column == 3 && value != null) {
                    String text = value.toString();
                    if (text.length() > 40) {
                        label.setText(text.substring(0, 37) + "...");
                        label.setToolTipText(text);
                    } else {
                        label.setText(text);
                        label.setToolTipText(null);
                    }
                }
                
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setVerticalAlignment(SwingConstants.CENTER); // Alineación vertical centrada
                label.setFont(UITheme.FONT_BODY);
                label.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                label.setOpaque(true);
                
                return label;
            }
        };
        
        // Renderer para tipo con badge de color
        DefaultTableCellRenderer tipoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                Color rowColor = row % 2 == 0 ? new Color(255, 255, 255) : new Color(248, 250, 252);
                panel.setBackground(isSelected ? table.getSelectionBackground() : rowColor);
                panel.setBorder(new EmptyBorder(8, 10, 8, 10)); // Padding vertical para centrar
                
                if (value != null) {
                    String tipo = value.toString();
                    Color badgeColor = getBadgeColor(tipo);
                    
                    JLabel badge = new JLabel(tipo) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 35));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    badge.setFont(UITheme.FONT_SMALL_BOLD);
                    badge.setForeground(badgeColor);
                    badge.setBorder(new EmptyBorder(6, 12, 6, 12));
                    badge.setOpaque(false);
                    badge.setVerticalAlignment(SwingConstants.CENTER);
                    panel.add(badge);
                }
                
                return panel;
            }
        };
        
        // Renderer para monto
        DefaultTableCellRenderer montoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                
                if (value != null) {
                    String montoStr = value.toString();
                    if (montoStr.startsWith("+")) {
                        c.setForeground(UITheme.SUCCESS);
                    } else if (montoStr.startsWith("-")) {
                        c.setForeground(UITheme.ERROR);
                    } else {
                        c.setForeground(new Color(30, 41, 59));
                    }
                }
                
                ((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
                ((JLabel)c).setVerticalAlignment(SwingConstants.CENTER);
                ((JLabel)c).setFont(UITheme.FONT_BODY_BOLD);
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                
                return c;
            }
        };
        
        // Renderer para estado con badge
        DefaultTableCellRenderer estadoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                Color rowColor = row % 2 == 0 ? new Color(255, 255, 255) : new Color(248, 250, 252);
                panel.setBackground(isSelected ? table.getSelectionBackground() : rowColor);
                panel.setBorder(new EmptyBorder(8, 6, 8, 6)); // Padding vertical para centrar
                
                if (value != null) {
                    String estado = value.toString();
                    Color statusColor = estado.equals("EXITOSA") ? UITheme.SUCCESS : UITheme.TEXT_MUTED;
                    String icon = estado.equals("EXITOSA") ? " " : " ";
                    
                    JLabel badge = new JLabel(icon + estado) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), 25));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    badge.setFont(UITheme.FONT_CAPTION);
                    badge.setForeground(statusColor);
                    badge.setBorder(new EmptyBorder(4, 10, 4, 10));
                    badge.setOpaque(false);
                    badge.setVerticalAlignment(SwingConstants.CENTER);
                    panel.add(badge);
                }
                
                return panel;
            }
        };
        
        // Renderer para botón PDF
        TableCellRenderer pdfRenderer = (JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) -> {
            JButton btn = new JButton("📄");
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            btn.setToolTipText("Descargar comprobante PDF");
            btn.setBackground(new Color(99, 102, 241, 20));
            btn.setForeground(UITheme.PRIMARY);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        };
        
        table.getColumnModel().getColumn(6).setCellRenderer(pdfRenderer);
        
        // Click listener para la columna PDF
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (column == 6 && row >= 0) {
                    HistoryPanel.this.descargarComprobante(row);
                }
            }
        });
        
        // Aplicar renderers
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            if (i == 2) {
                table.getColumnModel().getColumn(i).setCellRenderer(tipoRenderer);
            } else if (i == 4) {
                table.getColumnModel().getColumn(i).setCellRenderer(montoRenderer);
            } else if (i == 5) {
                table.getColumnModel().getColumn(i).setCellRenderer(estadoRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
            }
        }
        
        JScrollPane scrollPane = new UITheme.RoundedScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(248, 250, 252));
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        
        // ---------------------------------------------------------
        // 3. PANEL INFERIOR: GRÁFICO + BOTÓN VOLVER (INTEGRACIÓN)
        // ---------------------------------------------------------
        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);
        
        // --- Contenedor para el Gráfico ---
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setOpaque(false);
        // El contenido real del gráfico se insertará en actualizarGrafico()
        
        // --- Panel de Botones (Volver a la izquierda, Exportar a la derecha) ---
        JPanel bottomBtnPanel = new JPanel(new BorderLayout());
        bottomBtnPanel.setOpaque(false);
        bottomBtnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Botón Volver (izquierda)
        JButton btnCerrar = UITheme.createSecondaryButton("Volver al Inicio", null);
        btnCerrar.addActionListener(e -> dashboard.mostrarDashboardHome());
        
        // Botón Exportar PDF (derecha) - con color distintivo
        JButton btnExportarPDF = createExportButton();
        btnExportarPDF.addActionListener(e -> exportarPDF());
        
        bottomBtnPanel.add(btnCerrar, BorderLayout.WEST);
        bottomBtnPanel.add(btnExportarPDF, BorderLayout.EAST);
        
        // Agregamos todo al contenedor sur
        southContainer.add(chartPanelContainer);
        southContainer.add(Box.createVerticalStrut(15));
        southContainer.add(bottomBtnPanel);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southContainer, BorderLayout.SOUTH);
    }
    
    // --------------------------------------------------------------------------------
    // MÉTODOS DE LÓGICA (CARGAR DATOS Y ACTUALIZAR GRÁFICO)
    // --------------------------------------------------------------------------------
    
    private void cargarHistorial() {
        try {
            tableModel.setRowCount(0);
            // Obtenemos los datos una sola vez
            allTransactions = transactionDAO.obtenerPorCuenta(cuenta.getId(), 50);
            
            if (allTransactions == null) {
                allTransactions = new ArrayList<>();
            }
            
            // 1. Llenar Tabla
            aplicarDatosATabla(allTransactions);
            lblTotalTransactions.setText(String.valueOf(allTransactions.size()));
            
            // 2. Llenar Gráfico (Usando los mismos datos)
            actualizarGrafico(allTransactions);
            
            System.out.println("✅ Historial cargado: " + allTransactions.size() + " transacciones");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
            allTransactions = new ArrayList<>();
            lblTotalTransactions.setText("0");
        }
    }
    
    // Método extraído para reutilizarlo en el filtro
    private void aplicarDatosATabla(List<Transaction> lista) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (Transaction t : lista) {
            String fecha = t.getFechaTransaccion().format(dateFormatter);
            String hora = t.getFechaTransaccion().format(timeFormatter);
            String tipo = formatearTipo(t.getTipoTransaccion().toString());
            String descripcion = t.getDescripcion();
            String estado = t.getEstado().toString();
            
            String monto;
            boolean esIngreso = t.getCuentaDestinoId() != null &&  
                                 t.getCuentaDestinoId().equals(cuenta.getId()) &&
                                 (t.getCuentaOrigenId() == null || !t.getCuentaOrigenId().equals(cuenta.getId()));
            
            if (esIngreso) {
                monto = "+ " + cuenta.getMoneda() + " " + String.format("%.2f", t.getMonto());
            } else {
                monto = "- " + cuenta.getMoneda() + " " + String.format("%.2f", t.getMonto());
            }
            
            tableModel.addRow(new Object[]{fecha, hora, tipo, descripcion, monto, estado, "📄"});
        }
    }
    
    /**
     * Descarga el comprobante PDF de una transacción específica.
     */
    private void descargarComprobante(int rowIndex) {
        if (allTransactions == null || rowIndex < 0 || rowIndex >= allTransactions.size()) {
            JOptionPane.showMessageDialog(this, 
                "No se puede generar el comprobante.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener transacción filtrada (considerar el filtro actual)
        List<Transaction> listaActual = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (currentFilter.equals("TODOS") || t.getTipoTransaccion().toString().equals(currentFilter)) {
                listaActual.add(t);
            }
        }
        
        if (rowIndex >= listaActual.size()) {
            JOptionPane.showMessageDialog(this, 
                "Transacción no encontrada.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Transaction transaccion = listaActual.get(rowIndex);
        
        // Selector de archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Comprobante PDF");
        String nombreArchivo = String.format("Comprobante_%s_%08d.pdf", 
            transaccion.getTipoTransaccion().toString(), 
            transaccion.getId());
        fileChooser.setSelectedFile(new java.io.File(nombreArchivo));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            
            if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
                rutaArchivo += ".pdf";
            }
            
            boolean exito = PDFExporter.exportarComprobante(transaccion, cuenta, rutaArchivo);
            
            if (exito) {
                int opcion = JOptionPane.showConfirmDialog(this,
                    "Comprobante generado exitosamente.\n¿Desea abrir el archivo?",
                    "Éxito",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
                    } catch (Exception e) {
                        System.err.println("No se pudo abrir el archivo: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al generar el comprobante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lógica integrada del Gráfico de Barras
     */
    private void actualizarGrafico(List<Transaction> transacciones) {
        try {
            dataset.clear();
            
            // Usamos TreeMap para que las fechas se ordenen automáticamente
            Map<LocalDate, Double> ingresos = new TreeMap<>();
            Map<LocalDate, Double> egresos = new TreeMap<>();
            
            // Procesar transacciones para el gráfico
            if (transacciones != null) {
                for (Transaction t : transacciones) {
                    if (t.getFechaTransaccion() == null || t.getMonto() == null) continue;
                    
                    LocalDate fecha = t.getFechaTransaccion().toLocalDate();
                    double monto = t.getMonto().doubleValue();
                    
                    boolean esEgreso = t.getCuentaOrigenId() != null && t.getCuentaOrigenId().equals(cuenta.getId());
                    
                    if (esEgreso) {
                        egresos.put(fecha, egresos.getOrDefault(fecha, 0.0) + monto);
                    } else {
                        ingresos.put(fecha, ingresos.getOrDefault(fecha, 0.0) + monto);
                    }
                }
            }
            
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
            
            // Llenamos el dataset (JFreeChart)
            for (LocalDate f : ingresos.keySet()) {
                 dataset.addValue(ingresos.get(f), "Ingresos", f.format(fmt));
            }
            for (LocalDate f : egresos.keySet()) {
                 dataset.addValue(egresos.get(f), "Egresos", f.format(fmt));
            }

            // Crear el gráfico
            JFreeChart chart = ChartFactory.createBarChart(
                null,       // Sin título (lo ponemos en el panel)
                "Fecha",    // Eje X
                "Monto",    // Eje Y
                dataset
            );
            
            // Estilizar el gráfico para que coincida con UITheme
            chart.setBackgroundPaint(null); // Transparente para ver el fondo de la tarjeta
            
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE); 
            plot.setOutlineVisible(false);
            plot.setRangeGridlinePaint(new Color(226, 232, 240));
            
            // Estilo de los ejes
            plot.getDomainAxis().setTickLabelPaint(UITheme.TEXT_SECONDARY);
            plot.getDomainAxis().setLabelPaint(new Color(30, 41, 59));
            plot.getRangeAxis().setTickLabelPaint(UITheme.TEXT_SECONDARY);
            plot.getRangeAxis().setLabelPaint(new Color(30, 41, 59));
            
            // Colores de las barras
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, UITheme.SUCCESS); // Serie 0: Ingresos (Verde)
            renderer.setSeriesPaint(1, UITheme.ERROR);   // Serie 1: Egresos (Rojo)
            renderer.setDrawBarOutline(false);
            
            // Crear el Panel del gráfico
            ChartPanel cp = new ChartPanel(chart);
            cp.setOpaque(false);
            cp.setPreferredSize(new Dimension(0, 220)); // Altura fija
            
            // Envolver en un panel redondeado (Estilo Tarjeta)
            UITheme.RoundedPanel chartCard = new UITheme.RoundedPanel(20, Color.WHITE, true);
            chartCard.setLayout(new BorderLayout());
            chartCard.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            JLabel titleChart = new JLabel("Flujo de Dinero (Últimas Transacciones)");
            titleChart.setFont(UITheme.FONT_BODY_BOLD);
            titleChart.setForeground(new Color(30, 41, 59));
            titleChart.setBorder(new EmptyBorder(0, 0, 10, 0));
            
            chartCard.add(titleChart, BorderLayout.NORTH);
            chartCard.add(cp, BorderLayout.CENTER);
            
            // Actualizar la interfaz
            chartPanelContainer.removeAll();
            chartPanelContainer.add(chartCard, BorderLayout.CENTER);
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear gráfico: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --------------------------------------------------------------------------------
    // HELPERS Y LISTENERS (FILTROS, COLORES, ETC.)
    // --------------------------------------------------------------------------------
    
    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.FONT_CAPTION);
        lblTitle.setForeground(UITheme.TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        valuePanel.setOpaque(false);
        valuePanel.add(valueLabel);
        
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(valuePanel);
        
        return card;
    }
    
    private JButton createFilterButton(String text, String filterType, Color color) {
        JButton btn = new JButton(text) {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            
            {
                hoverTimer = new Timer(16, e -> {
                    if (hovering && hoverProgress < 1f) {
                        hoverProgress = Math.min(1f, hoverProgress + 0.2f);
                        repaint();
                    } else if (!hovering && hoverProgress > 0f) {
                        hoverProgress = Math.max(0f, hoverProgress - 0.2f);
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                });
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        hoverTimer.start();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        hoverTimer.start();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isActive = filterType.equals(currentFilter);
                
                if (isActive) {
                    // Botón activo - color sólido
                    g2.setColor(color);
                } else {
                    // Botón inactivo - fondo claro con hover
                    Color bgColor = new Color(
                        (int)(250 + (color.getRed() - 250) * hoverProgress * 0.15f),
                        (int)(250 + (color.getGreen() - 250) * hoverProgress * 0.15f),
                        (int)(251 + (color.getBlue() - 251) * hoverProgress * 0.15f)
                    );
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Borde para botones inactivos
                if (!isActive) {
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(80 + hoverProgress * 100)));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(UITheme.FONT_SMALL_BOLD);
        btn.setForeground(filterType.equals("TODOS") ? Color.WHITE : color);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        
        btn.addActionListener(e -> {
            currentFilter = filterType;
            updateFilterButtons();
            applyFilter();
        });
        
        return btn;
    }
    
    private void updateFilterButtons() {
        String[] filters = {"TODOS", "TRANSFERENCIA", "DEPOSITO", "RETIRO", "PAGO_SERVICIO", "RECARGA"};
        Color[] filterColors = {
            new Color(99, 102, 241),   // Todos
            new Color(14, 165, 233),   // Transferencias
            new Color(34, 197, 94),    // Depósitos
            new Color(249, 115, 22),   // Retiros
            new Color(236, 72, 153),   // Pagos
            new Color(168, 85, 247)    // Recargas
        };
        
        int index = 0;
        for (Component comp : filterPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String text = btn.getText();
                boolean isActive = isFilterActive(text);
                
                // Determinar el color del texto según el estado
                if (isActive) {
                    btn.setForeground(Color.WHITE);
                } else {
                    // Encontrar el índice del filtro
                    int colorIndex = getColorIndexForButton(text);
                    if (colorIndex >= 0 && colorIndex < filterColors.length) {
                        btn.setForeground(filterColors[colorIndex]);
                    }
                }
                btn.repaint();
            }
        }
    }
    
    private int getColorIndexForButton(String buttonText) {
        return switch (buttonText) {
            case "Todos" -> 0;
            case "Transferencias" -> 1;
            case "Depósitos" -> 2;
            case "Retiros" -> 3;
            case "Pagos" -> 4;
            case "Recargas" -> 5;
            default -> -1;
        };
    }
    
    private boolean isFilterActive(String buttonText) {
        return switch (currentFilter) {
            case "TODOS" -> buttonText.equals("Todos");
            case "TRANSFERENCIA" -> buttonText.equals("Transferencias");
            case "DEPOSITO" -> buttonText.equals("Depósitos");
            case "RETIRO" -> buttonText.equals("Retiros");
            case "PAGO_SERVICIO" -> buttonText.equals("Pagos");
            case "RECARGA" -> buttonText.equals("Recargas");
            default -> false;
        };
    }
    
    private void applyFilter() {
        tableModel.setRowCount(0);
        if (allTransactions == null) return;
        
        List<Transaction> filteredList = new ArrayList<>();
        
        for (Transaction t : allTransactions) {
            if (currentFilter.equals("TODOS")) {
                filteredList.add(t);
            } else if (t.getTipoTransaccion().toString().equals(currentFilter)) {
                filteredList.add(t);
            }
        }
        
        aplicarDatosATabla(filteredList);
        lblTotalTransactions.setText(String.valueOf(filteredList.size()));
        
        // Si quisieras que el gráfico también se filtre, descomenta esto:
        // actualizarGrafico(filteredList);
    }
    
    private Color getBadgeColor(String tipo) {
        if (tipo.contains("Transferencia")) return new Color(14, 165, 233);
        if (tipo.contains("Depósito")) return new Color(34, 197, 94);
        if (tipo.contains("Retiro")) return new Color(249, 115, 22);
        if (tipo.contains("Pago")) return new Color(236, 72, 153);
        if (tipo.contains("Recarga")) return new Color(168, 85, 247);
        return UITheme.TEXT_SECONDARY;
    }
    
    private String formatearTipo(String tipo) {
        return switch (tipo) {
            case "TRANSFERENCIA" -> "Transferencia Saliente";
            case "DEPOSITO" -> "Depósito Entrante";
            case "RETIRO" -> "Retiro ATM";
            case "PAGO_SERVICIO" -> "Pago de Servicio";
            case "RECARGA" -> "Recarga";
            default -> tipo;
        };
    }
    
    // --------------------------------------------------------------------------------
    // EXPORTAR A PDF
    // --------------------------------------------------------------------------------
    
    private JButton createExportButton() {
        JButton btn = new JButton("Exportar PDF") {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            
            {
                hoverTimer = new Timer(16, e -> {
                    if (hovering && hoverProgress < 1f) {
                        hoverProgress = Math.min(1f, hoverProgress + 0.15f);
                        repaint();
                    } else if (!hovering && hoverProgress > 0f) {
                        hoverProgress = Math.max(0f, hoverProgress - 0.15f);
                        repaint();
                    } else {
                        ((Timer)e.getSource()).stop();
                    }
                });
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        hoverTimer.start();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        hoverTimer.start();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Color rojo/naranja para distinguirlo
                Color baseColor = new Color(220, 38, 38); // Rojo
                Color hoverColor = new Color(185, 28, 28); // Rojo más oscuro
                
                Color bgColor = new Color(
                    (int)(baseColor.getRed() + (hoverColor.getRed() - baseColor.getRed()) * hoverProgress),
                    (int)(baseColor.getGreen() + (hoverColor.getGreen() - baseColor.getGreen()) * hoverProgress),
                    (int)(baseColor.getBlue() + (hoverColor.getBlue() - baseColor.getBlue()) * hoverProgress)
                );
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(UITheme.FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
        btn.setPreferredSize(new Dimension(150, 44));
        
        return btn;
    }
    
    private void exportarPDF() {
        if (allTransactions == null || allTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay transacciones para exportar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Selector de archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Historial como PDF");
        fileChooser.setSelectedFile(new java.io.File("Historial_" + cuenta.getNumeroCuenta() + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Asegurar extensión .pdf
            if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
                rutaArchivo += ".pdf";
            }
            
            // Exportar
            boolean exito = PDFExporter.exportarHistorial(allTransactions, cuenta, rutaArchivo);
            
            if (exito) {
                int opcion = JOptionPane.showConfirmDialog(this,
                    "PDF exportado exitosamente.\n¿Desea abrir el archivo?",
                    "Éxito",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
                    } catch (Exception e) {
                        System.err.println("No se pudo abrir el archivo: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al exportar el PDF. Revise la consola.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}