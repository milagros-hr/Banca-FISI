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
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.TransactionDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Transaction;
import com.bancamovil.models.Transaction.TipoTransaccion;

public class RechargePanel extends JPanel {
    private final DashboardFrame dashboard;
    private final Account cuenta;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    
    private JTextField txtMonto;
    
    private static final double[] MONTOS_RAPIDOS = {50.0, 100.0, 200.0, 500.0};
    
    public RechargePanel(DashboardFrame dashboard, Account cuenta) {
        this.dashboard = dashboard;
        this.cuenta = cuenta;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        
        UITheme.RoundedPanel mainCard = new UITheme.RoundedPanel(24, Color.WHITE, true);
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBorder(new EmptyBorder(40, 45, 40, 45));
        mainCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(UITheme.ICON_RECHARGE);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 16));
        
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Recargar Saldo");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(new Color(30, 41, 59));
        
        JLabel subtitleLabel = new JLabel("Agrega fondos a tu cuenta de manera rápida y segura");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        
        titleContainer.add(titleLabel);
        titleContainer.add(Box.createVerticalStrut(4));
        titleContainer.add(subtitleLabel);
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleContainer);

        UITheme.RoundedPanel balanceCard = new UITheme.RoundedPanel(20, new Color(240, 253, 244), false, true);
        balanceCard.setGlowColor(UITheme.SUCCESS);
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBorder(new EmptyBorder(24, 28, 24, 28));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        JLabel lblBalanceTitle = new JLabel("SALDO DISPONIBLE");
        lblBalanceTitle.setFont(UITheme.FONT_SMALL_BOLD);
        lblBalanceTitle.setForeground(UITheme.TEXT_SECONDARY);
        lblBalanceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblBalance = new JLabel(cuenta.getMoneda() + " " + cuenta.getSaldoFormateado());
        lblBalance.setFont(UITheme.FONT_MONEY);
        lblBalance.setForeground(UITheme.SUCCESS);
        lblBalance.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCuentaNum = new JLabel("Cuenta: " + cuenta.getNumeroCuenta());
        lblCuentaNum.setFont(UITheme.FONT_BODY);
        lblCuentaNum.setForeground(UITheme.TEXT_MUTED);
        lblCuentaNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        balanceCard.add(lblBalanceTitle);
        balanceCard.add(Box.createVerticalStrut(8));
        balanceCard.add(lblBalance);
        balanceCard.add(Box.createVerticalStrut(6));
        balanceCard.add(lblCuentaNum);

        JPanel quickAmountsSection = new JPanel();
        quickAmountsSection.setLayout(new BoxLayout(quickAmountsSection, BoxLayout.Y_AXIS));
        quickAmountsSection.setOpaque(false);
        quickAmountsSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblQuickTitle = new JLabel("Montos Rápidos");
        lblQuickTitle.setFont(UITheme.FONT_HEADING);
        lblQuickTitle.setForeground(new Color(30, 41, 59));
        lblQuickTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel quickButtonsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        quickButtonsPanel.setOpaque(false);
        quickButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickButtonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        for (double monto : MONTOS_RAPIDOS) {
            JButton btnQuick = createQuickAmountButton(monto);
            quickButtonsPanel.add(btnQuick);
        }
        
        quickAmountsSection.add(lblQuickTitle);
        quickAmountsSection.add(Box.createVerticalStrut(14));
        quickAmountsSection.add(quickButtonsPanel);

        JPanel formSection = new JPanel();
        formSection.setLayout(new BoxLayout(formSection, BoxLayout.Y_AXIS));
        formSection.setOpaque(false);
        formSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMontoTitle = new JLabel("O ingresa un monto personalizado");
        lblMontoTitle.setFont(UITheme.FONT_HEADING);
        lblMontoTitle.setForeground(new Color(30, 41, 59));
        lblMontoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel inputContainer = new JPanel();
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setOpaque(false);
        inputContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMontoLabel = new JLabel("Monto a recargar (" + cuenta.getMoneda() + ")");
        lblMontoLabel.setFont(UITheme.FONT_BODY_BOLD);
        lblMontoLabel.setForeground(UITheme.TEXT_SECONDARY);
        lblMontoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMonto = UITheme.createRoundedTextField();
        txtMonto.setText("0.00");
        txtMonto.setOpaque(true);
        txtMonto.setBackground(new Color(249, 250, 251));
        txtMonto.setForeground(new Color(30, 41, 59));
        txtMonto.setFont(UITheme.FONT_MONEY_SMALL);
        txtMonto.setHorizontalAlignment(JTextField.CENTER);
        txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        txtMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputContainer.add(lblMontoLabel);
        inputContainer.add(Box.createVerticalStrut(10));
        inputContainer.add(txtMonto);
        
        formSection.add(lblMontoTitle);
        formSection.add(Box.createVerticalStrut(16));
        formSection.add(inputContainer);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnRecargar = UITheme.createActionButton("Confirmar Recarga", UITheme.SUCCESS, UITheme.ICON_SUCCESS);
        btnRecargar.setPreferredSize(new Dimension(200, 52));
        btnRecargar.addActionListener(e -> ejecutarRecarga());
        
        JButton btnCancelar = UITheme.createSecondaryButton("Cancelar", UITheme.ICON_ARROW_LEFT);
        btnCancelar.setPreferredSize(new Dimension(140, 52));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());
        
        buttonPanel.add(btnRecargar);
        buttonPanel.add(btnCancelar);

        mainCard.add(headerPanel);
        mainCard.add(Box.createVerticalStrut(30));
        mainCard.add(balanceCard);
        mainCard.add(Box.createVerticalStrut(32));
        mainCard.add(quickAmountsSection);
        mainCard.add(Box.createVerticalStrut(28));
        mainCard.add(UITheme.createSeparator());
        mainCard.add(Box.createVerticalStrut(28));
        mainCard.add(formSection);
        mainCard.add(Box.createVerticalStrut(36));
        mainCard.add(buttonPanel);

        scrollContent.add(mainCard);
        
        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JButton createQuickAmountButton(double monto) {
        String texto = String.format("%s %.0f", cuenta.getMoneda(), monto);
        
        JButton btn = new JButton(texto) {
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
                
                Color bgBase = Color.WHITE;
                Color bgHover = new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), 40);
                
                int r = (int)(bgBase.getRed() + (bgHover.getRed() - bgBase.getRed()) * hoverProgress);
                int gb = (int)(bgBase.getGreen() + (bgHover.getGreen() - bgBase.getGreen()) * hoverProgress);
                int b = (int)(bgBase.getBlue() + (bgHover.getBlue() - bgBase.getBlue()) * hoverProgress);
                
                g2.setColor(new Color(r, gb, b));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                Color borderColor = interpolateColor(new Color(226, 232, 240), UITheme.PRIMARY, hoverProgress);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f + hoverProgress));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                
                if (hoverProgress > 0) {
                    int glowAlpha = (int)(hoverProgress * 30);
                    g2.setColor(new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(), UITheme.PRIMARY.getBlue(), glowAlpha));
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(UITheme.FONT_BODY_BOLD);
        btn.setForeground(new Color(30, 41, 59));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 55));
        btn.setBorder(new EmptyBorder(14, 16, 14, 16));
        
        btn.addActionListener(e -> {
            txtMonto.setText(String.format("%.2f", monto));
            txtMonto.requestFocus();
        });
        
        return btn;
    }
    
    private Color interpolateColor(Color c1, Color c2, float progress) {
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * progress);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * progress);
        return new Color(r, g, b);
    }
    
    private void ejecutarRecarga() {
        try {
            BigDecimal monto = new BigDecimal(txtMonto.getText().trim());
            
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                String.format("Confirmar recarga de %s %.2f a su cuenta?", cuenta.getMoneda(), monto),
                "Confirmar Recarga", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            boolean exito = accountDAO.depositar(cuenta.getId(), monto);
            
            if (exito) {
                Transaction transaccion = new Transaction(TipoTransaccion.RECARGA, monto, "Recarga de saldo");
                transaccion.setCuentaDestinoId(cuenta.getId());
                transactionDAO.registrar(transaccion);
                
                JOptionPane.showMessageDialog(this, "Recarga realizada con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                
                dashboard.actualizarDatos();     
                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this, "Error al procesar la recarga.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto valido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}