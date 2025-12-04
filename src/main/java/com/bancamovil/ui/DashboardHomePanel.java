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
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.bancamovil.services.TransactionService;
import static com.bancamovil.ui.UITheme.BG_WHITE;
import static com.bancamovil.ui.UITheme.BORDER_LIGHT;
import static com.bancamovil.ui.UITheme.FONT_BODY;
import static com.bancamovil.ui.UITheme.FONT_BODY_BOLD;
import static com.bancamovil.ui.UITheme.FONT_CAPTION;
import static com.bancamovil.ui.UITheme.FONT_HEADING;
import static com.bancamovil.ui.UITheme.FONT_MONEY_SMALL;
import static com.bancamovil.ui.UITheme.FONT_SMALL;
import static com.bancamovil.ui.UITheme.FONT_SUBTITLE;
import com.bancamovil.ui.UITheme.ModernScrollBarUI;
import static com.bancamovil.ui.UITheme.PRIMARY;
import static com.bancamovil.ui.UITheme.PRIMARY_DARK;
import static com.bancamovil.ui.UITheme.TEXT_DARK;
import static com.bancamovil.ui.UITheme.TEXT_MUTED;

public class DashboardHomePanel extends JPanel {

    private JPanel movementsListPanel;
    private DashboardFrame dashboard;
    private Account cuenta;
    private TransactionService service;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public DashboardHomePanel(DashboardFrame dashboard, Account cuenta, TransactionService service) {
        this.dashboard = dashboard;
        this.cuenta = cuenta;
        this.service = service;

        initUI();
        cargarMovimientos();
    }

    private void initUI() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 24));
        setBorder(new EmptyBorder(8, 0, 0, 0));
        
        // Panel de acciones rápidas
        JPanel quickActionsPanel = createQuickActionsPanel();
        add(quickActionsPanel, BorderLayout.NORTH);
        
        // Panel de movimientos
        JPanel movementsPanel = createMovementsPanel();
        add(movementsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        
        // Título
        JLabel lblTitle = new JLabel("Acciones Rápidas");
        lblTitle.setFont(FONT_HEADING);
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Grid de acciones
        JPanel actionsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        actionsGrid.setOpaque(false);
        actionsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        
        actionsGrid.add(createQuickActionCard("💸", "Transferir", "Envía dinero", 
            new Color(99, 102, 241), e -> dashboard.mostrarTransferencia()));
        actionsGrid.add(createQuickActionCard("📄", "Pagar", "Servicios", 
            new Color(34, 197, 94), e -> dashboard.mostrarPagoServicios()));
        actionsGrid.add(createQuickActionCard("💰", "Recargar", "Añade saldo", 
            new Color(249, 115, 22), e -> dashboard.mostrarRecarga()));
        actionsGrid.add(createQuickActionCard("📋", "Historial", "Ver todo", 
            new Color(6, 182, 212), e -> dashboard.mostrarHistorial()));
        
        container.add(lblTitle);
        container.add(Box.createVerticalStrut(16));
        container.add(actionsGrid);
        
        return container;
    }
    
    private JPanel createQuickActionCard(String icon, String title, String subtitle, Color accentColor, ActionListener action) {
        JPanel card = new JPanel() {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            
            {
                hoverTimer = new Timer(16, e -> {
                    if (hovering && hoverProgress < 1f) {
                        hoverProgress = Math.min(1f, hoverProgress + 0.12f);
                        repaint();
                    } else if (!hovering && hoverProgress > 0f) {
                        hoverProgress = Math.max(0f, hoverProgress - 0.12f);
                        repaint();
                    } else {
                        hoverTimer.stop();
                    }
                });
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        hoverTimer.start();
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        hoverTimer.start();
                    }
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Sombra con hover
                int shadowSize = (int)(4 + hoverProgress * 4);
                for (int i = shadowSize; i > 0; i--) {
                    int alpha = (int)((15 + hoverProgress * 10) - i * 3);
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), Math.max(0, alpha)));
                    g2.fillRoundRect(-i, -i + 2, w + i*2, h + i*2, 20 + i, 20 + i);
                }
                
                // Fondo
                g2.setColor(BG_WHITE);
                g2.fillRoundRect(0, 0, w, h, 20, 20);
                
                // Borde con acento en hover
                Color borderColor = new Color(
                    (int)(BORDER_LIGHT.getRed() + (accentColor.getRed() - BORDER_LIGHT.getRed()) * hoverProgress * 0.5f),
                    (int)(BORDER_LIGHT.getGreen() + (accentColor.getGreen() - BORDER_LIGHT.getGreen()) * hoverProgress * 0.5f),
                    (int)(BORDER_LIGHT.getBlue() + (accentColor.getBlue() - BORDER_LIGHT.getBlue()) * hoverProgress * 0.5f)
                );
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                
                // Indicador de color superior
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, w, 4, 20, 20);
                g2.fillRect(0, 2, w, 4);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 16, 20));
        
        // Icono en círculo
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con color suave
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25));
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));
        iconPanel.setMaximumSize(new Dimension(48, 48));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconPanel.add(iconLabel);
        iconPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_BODY_BOLD);
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(FONT_SMALL);
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(subtitleLabel);
        
        return card;
    }
    
    private JPanel createMovementsPanel() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BorderLayout());
        
        // Header con título y botón ver todo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        JLabel lblTitle = new JLabel("Últimos Movimientos");
        lblTitle.setFont(FONT_HEADING);
        lblTitle.setForeground(TEXT_DARK);
        
        JButton btnVerTodo = new JButton("Ver historial completo →");
        btnVerTodo.setFont(FONT_SMALL);
        btnVerTodo.setForeground(PRIMARY);
        btnVerTodo.setBorder(null);
        btnVerTodo.setContentAreaFilled(false);
        btnVerTodo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerTodo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVerTodo.setForeground(PRIMARY_DARK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVerTodo.setForeground(PRIMARY);
            }
        });
        btnVerTodo.addActionListener(e -> dashboard.mostrarHistorial());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnVerTodo, BorderLayout.EAST);
        
        // Lista de movimientos
        movementsListPanel = new JPanel();
        movementsListPanel.setLayout(new BoxLayout(movementsListPanel, BoxLayout.Y_AXIS));
        movementsListPanel.setOpaque(false);
        
        JScrollPane sc = new JScrollPane(movementsListPanel);
        sc.setBorder(null);
        sc.setOpaque(false);
        sc.getViewport().setOpaque(false);
        sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        sc.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(sc, BorderLayout.CENTER);
        
        return container;
    }

    public void cargarMovimientos() {
        cuenta = dashboard.getCuentaActual(); 
        movementsListPanel.removeAll();
        
        if (cuenta == null || service == null) {
            addEmptyMessage("No hay cuenta seleccionada", "Selecciona una cuenta para ver tus movimientos");
            return;
        }
        
        List<Transaction> list = service.getRecentTransactions(cuenta, 8); 

        if (list == null || list.isEmpty()) {
            addEmptyMessage("Sin movimientos", "Aún no tienes transacciones en esta cuenta");
        } else {
            for (int i = 0; i < list.size(); i++) {
                Transaction t = list.get(i);
                movementsListPanel.add(createMovementCard(t, cuenta.getMoneda().toString(), cuenta.getId()));
                if (i < list.size() - 1) {
                    movementsListPanel.add(Box.createVerticalStrut(12));
                }
            }
            movementsListPanel.add(Box.createVerticalGlue());
        }
        
        movementsListPanel.revalidate();
        movementsListPanel.repaint();
    }
    
    private void addEmptyMessage(String title, String subtitle) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBorder(new EmptyBorder(60, 0, 60, 0));
        
        JLabel iconLabel = new JLabel("📭");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SUBTITLE);
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(FONT_BODY);
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        emptyPanel.add(iconLabel);
        emptyPanel.add(Box.createVerticalStrut(16));
        emptyPanel.add(titleLabel);
        emptyPanel.add(Box.createVerticalStrut(8));
        emptyPanel.add(subtitleLabel);
        
        movementsListPanel.add(emptyPanel);
    }
    
    private JPanel createMovementCard(Transaction t, String monedaStr, Integer currentAccountId) {
        boolean isDebit = t.getCuentaOrigenId() != null && t.getCuentaOrigenId().equals(currentAccountId);
        BigDecimal absoluteMonto = t.getMonto().abs();
        
        Color amountColor = isDebit ? UITheme.ERROR : UITheme.SUCCESS;
        String amountPrefix = isDebit ? "- " : "+ ";
        String currencySymbol = monedaStr.equals("PEN") ? "S/. " : "$ ";
        String formattedAmount = amountPrefix + currencySymbol + String.format("%,.2f", absoluteMonto);
        
        // Card principal con hover
        JPanel card = new JPanel() {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            
            {
                hoverTimer = new Timer(16, e -> {
                    if (hovering && hoverProgress < 1f) {
                        hoverProgress = Math.min(1f, hoverProgress + 0.12f);
                        repaint();
                    } else if (!hovering && hoverProgress > 0f) {
                        hoverProgress = Math.max(0f, hoverProgress - 0.12f);
                        repaint();
                    } else {
                        hoverTimer.stop();
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
                
                int w = getWidth();
                int h = getHeight();
                
                // Sombra sutil
                int shadowOffset = (int)(2 + hoverProgress * 2);
                for (int i = shadowOffset; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 8 - i * 2));
                    g2.fillRoundRect(-i, -i + 2, w + i*2, h + i*2, 18, 18);
                }
                
                // Fondo
                g2.setColor(BG_WHITE);
                g2.fillRoundRect(0, 0, w, h, 18, 18);
                
                // Borde sutil en hover
                if (hoverProgress > 0) {
                    g2.setColor(new Color(BORDER_LIGHT.getRed(), BORDER_LIGHT.getGreen(), BORDER_LIGHT.getBlue(), (int)(hoverProgress * 150)));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setOpaque(false);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icono con fondo coloreado
        String iconText = getIconForType(t.getTipo().toString());
        Color iconBg = isDebit ? UITheme.ERROR_LIGHT : UITheme.SUCCESS_LIGHT;
        Color iconFg = isDebit ? UITheme.ERROR : UITheme.SUCCESS;
        
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLabel.setForeground(iconFg);
        iconPanel.add(iconLabel);
        
        card.add(iconPanel, BorderLayout.WEST);

        // Centro - Descripción y fecha
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        JLabel lblDescription = new JLabel(t.getDescripcion());
        lblDescription.setFont(FONT_BODY_BOLD);
        lblDescription.setForeground(TEXT_DARK);
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String dateStr = t.getFecha().toLocalDate().format(DATE_FORMAT);
        String timeStr = t.getFecha().toLocalTime().format(TIME_FORMAT);
        String tipoStr = formatTipo(t.getTipo().toString());
        
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setOpaque(false);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblDate = new JLabel(dateStr + " • " + timeStr);
        lblDate.setFont(FONT_SMALL);
        lblDate.setForeground(TEXT_MUTED);
        
        JLabel lblTipo = UITheme.createBadge(tipoStr, isDebit ? UITheme.ERROR : UITheme.SUCCESS);
        lblTipo.setFont(FONT_CAPTION);
        
        metaPanel.add(lblDate);
        metaPanel.add(Box.createHorizontalStrut(10));
        metaPanel.add(lblTipo);
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblDescription);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(metaPanel);
        centerPanel.add(Box.createVerticalGlue());
        
        card.add(centerPanel, BorderLayout.CENTER);

        // Derecha - Monto
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JLabel lblAmount = new JLabel(formattedAmount);
        lblAmount.setFont(FONT_MONEY_SMALL);
        lblAmount.setForeground(amountColor);
        lblAmount.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(lblAmount);
        rightPanel.add(Box.createVerticalGlue());
        
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private String formatTipo(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "TRANSFERENCIA" -> "Transferencia";
            case "RECARGA" -> "Recarga";
            case "PAGO_SERVICIO" -> "Pago";
            case "DEPOSITO" -> "Depósito";
            case "RETIRO" -> "Retiro";
            default -> tipo;
        };
    }
    
    private String getIconForType(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "TRANSFERENCIA" -> "↔️";
            case "RECARGA" -> "💰";
            case "PAGO_SERVICIO" -> "💳";
            case "DEPOSITO" -> "📥";
            case "RETIRO" -> "📤";
            default -> "💵";
        };
    }
}
