package com.bancamovil.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.CardDAO;
import com.bancamovil.dao.TransactionDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Card;
import com.bancamovil.models.User;
import com.bancamovil.services.AuthService;

public class ProfilePanel extends JPanel {
    private DashboardFrame dashboard;
    private User user;
    private AccountDAO accountDAO;
    private CardDAO cardDAO;
    private TransactionDAO transactionDAO;
    private AuthService authService;
    
    public ProfilePanel(DashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        this.accountDAO = new AccountDAO();
        this.cardDAO = new CardDAO();
        this.transactionDAO = new TransactionDAO();
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(248, 250, 252));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JPanel headerPanel = createHeader();
        JPanel personalInfoCard = createPersonalInfoCard();
        JPanel cuentasCard = createCuentasCard();
        JPanel tarjetasCard = createTarjetasCard();
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnVolver = createStyledButton("Volver al Inicio", UITheme.ICON_ARROW_LEFT, new Color(71, 85, 105), Color.WHITE);
        btnVolver.addActionListener(e -> dashboard.mostrarDashboardHome());
        bottomPanel.add(btnVolver);
        
        mainContent.add(headerPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContent.add(personalInfoCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(cuentasCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(tarjetasCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(bottomPanel);
        mainContent.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(248, 250, 252));
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new UITheme.ModernScrollBarUI());
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        String initials = obtenerIniciales(user.getNombreCompleto());
        JPanel avatar = UITheme.createAvatar(initials, 80, UITheme.PRIMARY);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Mi Perfil");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel welcomeLabel = new JLabel("Bienvenido, " + user.getNombreCompleto());
        welcomeLabel.setFont(UITheme.FONT_BODY);
        welcomeLabel.setForeground(UITheme.TEXT_SECONDARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalGlue());
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(avatar);
        leftPanel.add(textPanel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private String obtenerIniciales(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isEmpty()) return "??";
        String[] partes = nombreCompleto.trim().split("\\s+");
        if (partes.length >= 2) {
            return (partes[0].charAt(0) + "" + partes[1].charAt(0)).toUpperCase();
        }
        return nombreCompleto.substring(0, Math.min(2, nombreCompleto.length())).toUpperCase();
    }
    
    private JPanel createPersonalInfoCard() {
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(20, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 30, 25, 30));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        
        JLabel sectionTitle = new JLabel("Información Personal");
        sectionTitle.setFont(UITheme.FONT_SUBTITLE);
        sectionTitle.setForeground(new Color(30, 41, 59));
        
        titlePanel.add(iconLabel);
        titlePanel.add(sectionTitle);
        
        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        card.add(createInfoRowWithIcon("👤", "Nombre Completo", user.getNombreCompleto()));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(UITheme.createSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        card.add(createInfoRowWithIcon("🆔", "DNI", enmascararDNI(user.getDni())));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(UITheme.createSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        card.add(createInfoRowWithIcon("✉️", "Correo Electrónico", user.getEmail()));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(UITheme.createSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        String telefono = user.getTelefono() != null ? user.getTelefono() : "No registrado";
        card.add(createInfoRowWithIcon("📱", "Teléfono", telefono));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(UITheme.createSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        
        String estado = user.isActivo() ? "✓ Activo" : "✗ Inactivo";
        Color estadoColor = user.isActivo() ? UITheme.SUCCESS : UITheme.ERROR;
        card.add(createInfoRowWithIcon("⚡", "Estado del Usuario", estado, estadoColor));
        
        return card;
    }
    
    private JPanel createInfoRowWithIcon(String icon, String label, String value) {
        return createInfoRowWithIcon(icon, label, value, new Color(15, 23, 42));
    }
    
    private JPanel createInfoRowWithIcon(String icon, String label, String value, Color valueColor) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Icono con ancho fijo para alineación
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setPreferredSize(new Dimension(30, 30));
        iconLabel.setMinimumSize(new Dimension(30, 30));
        iconLabel.setMaximumSize(new Dimension(30, 30));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        row.add(iconLabel);
        row.add(Box.createRigidArea(new Dimension(10, 0)));
        
        // Label con ancho fijo para alineación
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(UITheme.FONT_BODY);
        lblLabel.setForeground(new Color(100, 116, 139));
        lblLabel.setPreferredSize(new Dimension(180, 30));
        lblLabel.setMinimumSize(new Dimension(180, 30));
        lblLabel.setMaximumSize(new Dimension(180, 30));
        
        row.add(lblLabel);
        row.add(Box.createHorizontalGlue());
        
        // Valor
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UITheme.FONT_BODY_BOLD);
        lblValue.setForeground(valueColor);
        
        row.add(lblValue);
        
        return row;
    }
    
    private JPanel createCuentasCard() {
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(20, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 30, 25, 30));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(UITheme.ICON_BANK);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        
        JLabel sectionTitle = new JLabel("Mis Cuentas");
        sectionTitle.setFont(UITheme.FONT_SUBTITLE);
        sectionTitle.setForeground(new Color(30, 41, 59));
        
        titlePanel.add(iconLabel);
        titlePanel.add(sectionTitle);
        
        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        List<Account> cuentas = accountDAO.obtenerCuentasPorUsuario(user.getId());
        
        if (cuentas.isEmpty()) {
            JLabel noCuentas = new JLabel("No tiene cuentas registradas");
            noCuentas.setFont(UITheme.FONT_BODY);
            noCuentas.setForeground(UITheme.TEXT_SECONDARY);
            noCuentas.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(noCuentas);
        } else {
            for (int i = 0; i < cuentas.size(); i++) {
                Account cuenta = cuentas.get(i);
                
                if (i > 0) {
                    card.add(Box.createRigidArea(new Dimension(0, 12)));
                    card.add(UITheme.createSeparator());
                    card.add(Box.createRigidArea(new Dimension(0, 12)));
                }
                
                card.add(createCuentaItem(cuenta));
            }
        }
        
        return card;
    }
    
    private JPanel createCuentaItem(Account cuenta) {
        UITheme.RoundedPanel item = new UITheme.RoundedPanel(12, new Color(248, 250, 252), false);
        item.setLayout(new BorderLayout(15, 5));
        item.setBorder(new EmptyBorder(15, 15, 15, 15));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        
        String iconoCuenta = "💰";
        Color iconColor = UITheme.ACCENT;
        if (cuenta.getTipoCuenta() == Account.TipoCuenta.CORRIENTE) {
            iconoCuenta = "💳";
            iconColor = UITheme.PRIMARY;
        } else if (cuenta.getTipoCuenta() == Account.TipoCuenta.CTS) {
            iconoCuenta = "🏦";
            iconColor = UITheme.ACCENT_CYAN;
        }
        
        JPanel iconPanel = UITheme.createAvatar(iconoCuenta, 50, iconColor);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel lblTipo = new JLabel(cuenta.getTipoCuenta().toString());
        lblTipo.setFont(UITheme.FONT_HEADING);
        lblTipo.setForeground(new Color(30, 41, 59));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblNumero = new JLabel("Nº " + enmascararCuenta(cuenta.getNumeroCuenta()));
        lblNumero.setFont(new Font("Consolas", Font.PLAIN, 13));
        lblNumero.setForeground(UITheme.TEXT_SECONDARY);
        lblNumero.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMoneda = new JLabel("Moneda: " + cuenta.getMoneda());
        lblMoneda.setFont(UITheme.FONT_SMALL);
        lblMoneda.setForeground(UITheme.TEXT_MUTED);
        lblMoneda.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(lblTipo);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblNumero);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblMoneda);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JLabel lblSaldoTitle = new JLabel("Saldo Disponible");
        lblSaldoTitle.setFont(UITheme.FONT_CAPTION);
        lblSaldoTitle.setForeground(UITheme.TEXT_SECONDARY);
        lblSaldoTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel lblSaldo = new JLabel(cuenta.getSaldoFormateado());
        lblSaldo.setFont(UITheme.FONT_MONEY_SMALL);
        lblSaldo.setForeground(UITheme.SUCCESS);
        lblSaldo.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(lblSaldoTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rightPanel.add(lblSaldo);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton btnVerDetalles = createStyledButton("Ver Detalles", "📄", new Color(59, 130, 246), Color.WHITE);
        btnVerDetalles.setPreferredSize(new Dimension(130, 36));
        btnVerDetalles.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnVerDetalles.addActionListener(e -> {
            JOptionPane.showMessageDialog(item, 
                "Detalles de Cuenta: \nID: " + cuenta.getId() + 
                "\nNúmero Completo: " + cuenta.getNumeroCuenta() + 
                "\nTipo: " + cuenta.getTipoCuenta() + 
                "\nActiva: " + (cuenta.isActiva() ? "Sí" : "No"), 
                "Detalle de Cuenta", JOptionPane.INFORMATION_MESSAGE);
        });
        rightPanel.add(btnVerDetalles);
        
        item.add(iconPanel, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        item.add(rightPanel, BorderLayout.EAST);
        
        return item;
    }
    
    private JPanel createTarjetasCard() {
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(20, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 30, 25, 30));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(UITheme.ICON_CARD);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        
        JLabel sectionTitle = new JLabel("Mis Tarjetas");
        sectionTitle.setFont(UITheme.FONT_SUBTITLE);
        sectionTitle.setForeground(new Color(30, 41, 59));
        
        titlePanel.add(iconLabel);
        titlePanel.add(sectionTitle);
        
        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        List<Card> tarjetas = cardDAO.obtenerTarjetasPorUsuario(user.getId());
        
        if (tarjetas.isEmpty()) {
            JLabel noTarjetas = new JLabel("No tiene tarjetas registradas");
            noTarjetas.setFont(UITheme.FONT_BODY);
            noTarjetas.setForeground(UITheme.TEXT_SECONDARY);
            noTarjetas.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(noTarjetas);
        } else {
            for (int i = 0; i < tarjetas.size(); i++) {
                Card tarjeta = tarjetas.get(i);
                
                if (i > 0) {
                    card.add(Box.createRigidArea(new Dimension(0, 12)));
                    card.add(UITheme.createSeparator());
                    card.add(Box.createRigidArea(new Dimension(0, 12)));
                }
                
                card.add(createTarjetaItem(tarjeta));
            }
        }
        
        return card;
    }
    
    private JPanel createTarjetaItem(Card tarjeta) {
        UITheme.RoundedPanel item = new UITheme.RoundedPanel(12, new Color(248, 250, 252), false);
        item.setLayout(new BorderLayout(15, 5));
        item.setBorder(new EmptyBorder(15, 15, 15, 15));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        String iconoTarjeta = tarjeta.getTipoTarjeta() == Card.TipoTarjeta.DEBITO ? "🏧" : "💳";
        Color iconColor = tarjeta.getTipoTarjeta() == Card.TipoTarjeta.DEBITO ? UITheme.ACCENT_CYAN : UITheme.SECONDARY;
        
        JPanel iconPanel = UITheme.createAvatar(iconoTarjeta, 50, iconColor);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel lblTipo = new JLabel("Tarjeta " + tarjeta.getTipoTarjeta().toString());
        lblTipo.setFont(UITheme.FONT_HEADING);
        lblTipo.setForeground(new Color(30, 41, 59));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblNumero = new JLabel(tarjeta.getNumeroEnmascarado());
        lblNumero.setFont(new Font("Consolas", Font.BOLD, 14));
        lblNumero.setForeground(UITheme.TEXT_ACCENT);
        lblNumero.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblVencimiento = new JLabel("Vence: " + tarjeta.getFechaFormateada());
        lblVencimiento.setFont(UITheme.FONT_SMALL);
        lblVencimiento.setForeground(UITheme.TEXT_SECONDARY);
        lblVencimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(lblTipo);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblNumero);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblVencimiento);
        
        if (tarjeta.getTipoTarjeta() == Card.TipoTarjeta.CREDITO && tarjeta.getLimiteCredito() != null) {
            JLabel lblLimite = new JLabel("Límite: S/. " + String.format("%.2f", tarjeta.getLimiteCredito()));
            lblLimite.setFont(UITheme.FONT_SMALL);
            lblLimite.setForeground(UITheme.TEXT_MUTED);
            lblLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            infoPanel.add(lblLimite);
        }
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        String estado = tarjeta.getEstado();
        Color estadoColor = UITheme.SUCCESS;
        if (estado.equals("VENCIDA")) {
            estadoColor = UITheme.ERROR;
        } else if (estado.equals("BLOQUEADA")) {
            estadoColor = UITheme.WARNING;
        }
        
        JLabel badgeEstado = UITheme.createBadge(estado, estadoColor);
        badgeEstado.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(badgeEstado);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnVerDetalles = createStyledButton("Detalles", UITheme.ICON_SECURITY, new Color(99, 102, 241), Color.WHITE);
        btnVerDetalles.setPreferredSize(new Dimension(110, 36));
        btnVerDetalles.addActionListener(e -> {
            if (solicitarYVerificarPassword()) {
                showFullCardDetails(tarjeta);
            } else {
                 JOptionPane.showMessageDialog(this,
                    "Contraseña incorrecta. Acceso denegado.",
                    "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonRow.add(btnVerDetalles);

        if (!tarjeta.estaVencida()) {
            String btnText = tarjeta.isActiva() ? "Bloquear" : "Desbloquear";
            String btnIcon = tarjeta.isActiva() ? "🔒" : "🔓";
            Color btnColor = tarjeta.isActiva() ? new Color(239, 68, 68) : new Color(34, 197, 94);
            JButton btnBloquear = createStyledButton(btnText, btnIcon, btnColor, Color.WHITE);
            btnBloquear.setPreferredSize(new Dimension(130, 36));
            btnBloquear.addActionListener(e -> {
                if (!solicitarYVerificarPassword()) {
                    JOptionPane.showMessageDialog(this,
                        "Contraseña incorrecta. La operación fue cancelada.",
                        "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea " + (tarjeta.isActiva() ? "bloquear" : "desbloquear") + " esta tarjeta?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean nuevoEstado = !tarjeta.isActiva();
                    if (cardDAO.cambiarEstadoTarjeta(tarjeta.getId(), nuevoEstado)) {
                        JOptionPane.showMessageDialog(this,
                            "Tarjeta " + (nuevoEstado ? "desbloqueada" : "bloqueada") + " exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dashboard.mostrarPerfil();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Error al cambiar el estado de la tarjeta",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonRow.add(btnBloquear);
        }
        
        rightPanel.add(buttonRow);
        
        item.add(iconPanel, BorderLayout.WEST);
        item.add(infoPanel, BorderLayout.CENTER);
        item.add(rightPanel, BorderLayout.EAST);
        
        return item;
    }

    private JButton createStyledButton(String text, String icon, Color bgColor, Color textColor) {
        JButton button = new JButton(icon + " " + text) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                                   java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(getBackground().darker());
                } else {
                    g2.setColor(getBackground());
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(UITheme.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        return button;
    }

    private boolean solicitarYVerificarPassword() {
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(this, pf, 
            "Confirme su contraseña para ver detalles sensibles o realizar la operación", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (okCxl == JOptionPane.OK_OPTION) {
            String password = new String(pf.getPassword());
            try {
                return authService.verificarCredenciales(user.getEmail(), password); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de servicio de autenticación.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void showFullCardDetails(Card tarjeta) {
        String limiteStr = tarjeta.getLimiteCredito() != null ? 
                           "S/. " + String.format("%.2f", tarjeta.getLimiteCredito()) : 
                           "N/A (Débito)";
        
        String detalles = String.format(
            "Detalles Sensibles de Tarjeta:\n\n" +
            "ID: %d\n" +
            "Número Completo: %s\n" + 
            "CVV: %s\n" + 
            "Nombre del Titular: %s\n" + 
            "Tipo: %s\n" +
            "Estado Activa: %s\n" +
            "Fecha de Vencimiento: %s\n" + 
            "Fecha de Emisión: %s\n" +
            "Límite de Crédito: %s",
            tarjeta.getId(),
            tarjeta.getNumeroTarjeta(), 
            tarjeta.getCvv(), 
            tarjeta.getNombreTitular(),
            tarjeta.getTipoTarjeta(),
            tarjeta.isActiva() ? "Sí" : "No",
            tarjeta.getFechaFormateada(),
            tarjeta.getFechaEmision() != null ? tarjeta.getFechaEmision().toString() : "N/A",
            limiteStr
        );
        
        JOptionPane.showMessageDialog(this, detalles, "Información Confidencial de Tarjeta", JOptionPane.WARNING_MESSAGE);
    }
    
    private String enmascararDNI(String dni) {
        if (dni == null || dni.length() < 4) return "****";
        return "****" + dni.substring(dni.length() - 4);
    }
    
    private String enmascararCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.length() < 6) return "******";
        String ultimos6 = numeroCuenta.substring(numeroCuenta.length() - 6);
        return "**** **** " + ultimos6;
    }
} 