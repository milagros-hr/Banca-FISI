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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.TransactionDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.bancamovil.models.Transaction.TipoTransaccion;

public class PayServicePanel extends JPanel {

    private final DashboardFrame dashboard;
    private final Account cuenta;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    private JComboBox<String> comboServicio;
    private JTextField txtCodigo;
    private JTextField txtMonto;
    private int selectedServiceIndex = 0;
    private JPanel cardsContainer;

    private static final String[] ICONOS_SERVICIOS = {"⚡", "💧", "📱", "🌐", "📺"};
    private static final Color[] COLORES_SERVICIOS = {
        new Color(245, 158, 11),   // Amarillo - Luz
        new Color(6, 182, 212),    // Cyan - Agua
        new Color(139, 92, 246),   // Violeta - Teléfono
        new Color(59, 130, 246),   // Azul - Internet
        new Color(236, 72, 153)    // Rosa - Cable
    };

    private final String[] servicios = {
            "Luz del Sur",
            "SEDAPAL - Agua",
            "Movistar - Telefono",
            "Claro - Internet",
            "Movistar - Cable"
    };

    public PayServicePanel(DashboardFrame dashboard, Account cuenta) {
        this.dashboard = dashboard;
        this.cuenta = cuenta;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); 
        setBorder(new EmptyBorder(30, 40, 30, 40));

        UITheme.RoundedPanel card = new UITheme.RoundedPanel(24, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 40, 35, 40));

        // Header con icono
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel("💳");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 16));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Pago de Servicios");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(new Color(30, 41, 59)); 

        JLabel subtitleLabel = new JLabel("Paga tus servicios de forma rápida y segura");
        subtitleLabel.setFont(UITheme.FONT_SMALL);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(iconLabel);
        headerPanel.add(titlePanel);

        // Info de cuenta
        UITheme.RoundedPanel infoPanel = new UITheme.RoundedPanel(16, new Color(248, 250, 252), false, true);
        infoPanel.setGlowColor(UITheme.PRIMARY); 
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(18, 22, 18, 22));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);

        JPanel leftInfo = new JPanel();
        leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
        leftInfo.setOpaque(false);

        JLabel lblCuentaTitle = new JLabel("Cuenta de origen");
        lblCuentaTitle.setFont(UITheme.FONT_SMALL);
        lblCuentaTitle.setForeground(UITheme.TEXT_SECONDARY);

        JLabel lblCuentaNum = new JLabel(cuenta.getNumeroCuenta());
        lblCuentaNum.setFont(UITheme.FONT_BODY_BOLD);
        lblCuentaNum.setForeground(new Color(30, 41, 59));

        leftInfo.add(lblCuentaTitle);
        leftInfo.add(Box.createVerticalStrut(4));
        leftInfo.add(lblCuentaNum);

        JPanel rightInfo = new JPanel();
        rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
        rightInfo.setOpaque(false);
        rightInfo.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSaldoTitle = new JLabel("Saldo disponible");
        lblSaldoTitle.setFont(UITheme.FONT_SMALL);
        lblSaldoTitle.setForeground(UITheme.TEXT_SECONDARY);
        lblSaldoTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSaldoTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblSaldo = new JLabel(cuenta.getMoneda() + " " + cuenta.getSaldoFormateado());
        lblSaldo.setFont(UITheme.FONT_MONEY_SMALL);
        lblSaldo.setForeground(UITheme.SUCCESS); 
        lblSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSaldo.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightInfo.add(lblSaldoTitle);
        rightInfo.add(Box.createVerticalStrut(2));
        rightInfo.add(lblSaldo);

        infoRow.add(leftInfo, BorderLayout.WEST);
        infoRow.add(rightInfo, BorderLayout.EAST);

        infoPanel.add(infoRow);

        // Sección de selección de servicios
        JLabel lblSelectService = new JLabel("Selecciona un servicio");
        lblSelectService.setFont(UITheme.FONT_BODY_BOLD);
        lblSelectService.setForeground(new Color(30, 41, 59));
        lblSelectService.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardsContainer = new JPanel(new GridLayout(1, 5, 15, 0));
        cardsContainer.setOpaque(false);
        cardsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        for (int i = 0; i < servicios.length; i++) {
            cardsContainer.add(createServiceCard(i));
        }

        comboServicio = new JComboBox<>(servicios);
        comboServicio.setVisible(false);

        // Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCodigo = UITheme.createRoundedTextField();
        txtCodigo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        txtCodigo.setToolTipText("Ej: 12345678");
        txtCodigo.setOpaque(true);
        txtCodigo.setBackground(new Color(249, 250, 251));
        txtCodigo.setForeground(new Color(30, 41, 59));

        txtMonto = UITheme.createRoundedTextField();
        txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        txtMonto.setToolTipText("0.00");
        txtMonto.setOpaque(true);
        txtMonto.setBackground(new Color(249, 250, 251));
        txtMonto.setForeground(new Color(30, 41, 59));

        formPanel.add(createInputPanel("Código de Cliente", txtCodigo, "Ingresa el código de tu servicio"));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createInputPanel("Monto a Pagar", txtMonto, "Ingresa el monto en " + cuenta.getMoneda()));

        // Panel de resumen
        UITheme.RoundedPanel summaryPanel = new UITheme.RoundedPanel(12, new Color(99, 102, 241, 15), false);
        summaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel summaryText = new JLabel("El pago se procesará inmediatamente desde tu cuenta");
        summaryText.setFont(UITheme.FONT_SMALL);
        summaryText.setForeground(UITheme.PRIMARY);

        summaryPanel.add(infoIcon);
        summaryPanel.add(summaryText);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPagar = UITheme.createPrimaryButton("Pagar Servicio", "💳");
        btnPagar.setPreferredSize(new Dimension(200, 52));
        btnPagar.addActionListener(e -> ejecutarPago());

        JButton btnVolver = UITheme.createSecondaryButton("Volver", "←");
        btnVolver.setPreferredSize(new Dimension(140, 52));
        btnVolver.addActionListener(e -> dashboard.mostrarDashboardHome());

        buttonPanel.add(btnPagar);
        buttonPanel.add(btnVolver);

        // Agregar todo al card
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(25));
        card.add(infoPanel);
        card.add(Box.createVerticalStrut(30));
        card.add(lblSelectService);
        card.add(Box.createVerticalStrut(14));
        card.add(cardsContainer);
        card.add(Box.createVerticalStrut(28));
        card.add(formPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(summaryPanel);
        card.add(Box.createVerticalStrut(28));
        card.add(buttonPanel);

        // Wrapper para centrar
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 30, 0);

        card.setMaximumSize(new Dimension(700, 800));
        card.setPreferredSize(new Dimension(650, 750));

        wrapper.add(card, gbc);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel createServiceCard(int index) {
        JPanel card = new JPanel() {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            private boolean selected = (index == selectedServiceIndex);

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
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        hoverTimer.start();
                    }
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectService(index);
                    }
                });
            }

            public void setSelected(boolean sel) {
                this.selected = sel;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int radius = 16;
                Color serviceColor = COLORES_SERVICIOS[index];

                // Glow effect sutil
                if (selected || hoverProgress > 0) {
                    float progress = selected ? 1f : hoverProgress * 0.5f;
                    int glowSize = (int)(4 + progress * 4);
                    for (int i = glowSize; i > 0; i--) {
                        int alpha = (int)((25 * progress) - i * 4); 
                        g2.setColor(new Color(serviceColor.getRed(), serviceColor.getGreen(), serviceColor.getBlue(), Math.max(0, alpha)));
                        g2.fillRoundRect(-i, -i, w + i*2, h + i*2, radius + i, radius + i);
                    }
                }

                // Fondo BLANCO para que resalten los colores
                if (selected) {
                    g2.setColor(new Color(serviceColor.getRed(), serviceColor.getGreen(), serviceColor.getBlue(), 20));
                } else {
                    g2.setColor(Color.WHITE); 
                }
                g2.fillRoundRect(0, 0, w, h, radius, radius);

                // Borde
                if (selected) {
                    g2.setColor(serviceColor);
                    g2.setStroke(new BasicStroke(2f));
                } else {
                    Color borderColor = interpolateColor(new Color(226, 232, 240), serviceColor, hoverProgress);
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(1f));
                }
                g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);

                // Indicador de selección (Check)
                if (selected) {
                    g2.setColor(serviceColor);
                    g2.fillOval(w - 20, 8, 12, 12);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2.drawString("✓", w - 17, 17);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 8, 12, 8));
        card.setPreferredSize(new Dimension(100, 110)); 

        // Icono del servicio
        JLabel iconLabel = new JLabel(ICONOS_SERVICIOS[index]);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre corto del servicio
        String shortName = getShortServiceName(servicios[index]);
        JLabel nameLabel = new JLabel(shortName);
        nameLabel.setFont(UITheme.FONT_SMALL_BOLD);
        nameLabel.setForeground(index == selectedServiceIndex ? new Color(30, 41, 59) : UITheme.TEXT_SECONDARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private String getShortServiceName(String fullName) {
        if (fullName.contains("Luz")) return "Luz";
        if (fullName.contains("Agua") || fullName.contains("SEDAPAL")) return "Agua";
        if (fullName.contains("Telefono")) return "Teléfono";
        if (fullName.contains("Internet")) return "Internet";
        if (fullName.contains("Cable")) return "Cable";
        return fullName;
    }

    private void selectService(int index) {
        selectedServiceIndex = index;
        comboServicio.setSelectedIndex(index);

        cardsContainer.removeAll();
        for (int i = 0; i < servicios.length; i++) {
            cardsContainer.add(createServiceCard(i));
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private Color interpolateColor(Color c1, Color c2, float progress) {
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * progress);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * progress);
        return new Color(r, g, b);
    }

    private void ejecutarPago() {
        try {
            String servicio = (String) comboServicio.getSelectedItem();
            String codigo = txtCodigo.getText().trim();
             
            if (txtMonto.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
             
            BigDecimal monto = new BigDecimal(txtMonto.getText().trim());

            if (codigo.isEmpty() || monto.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos con valores válidos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (monto.compareTo(dashboard.getCuentaActual().getSaldo()) > 0) {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    String.format("Confirmar pago de %s por %s %.2f?",
                            servicio, cuenta.getMoneda(), monto),
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = accountDAO.pagarServicio(cuenta.getId(), monto);

            if (ok) {
                Transaction t = new Transaction(
                        TipoTransaccion.PAGO_SERVICIO,
                        monto,
                        "Pago " + servicio + " - Cod: " + codigo
                );
                t.setCuentaOrigenId(cuenta.getId());
                transactionDAO.registrar(t);

                JOptionPane.showMessageDialog(this,
                        "Pago realizado con éxito.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dashboard.actualizarDatos();
                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al procesar el pago.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createInputPanel(String label, JComponent field, String hint) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel labelRow = new JPanel(new BorderLayout());
        labelRow.setOpaque(false);
        labelRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(new Color(30, 41, 59)); 

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(UITheme.FONT_CAPTION);
        hintLabel.setForeground(UITheme.TEXT_MUTED);

        labelRow.add(lbl, BorderLayout.WEST);
        labelRow.add(hintLabel, BorderLayout.EAST);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(labelRow);
        panel.add(Box.createVerticalStrut(10));
        panel.add(field);

        return panel;
    }
}