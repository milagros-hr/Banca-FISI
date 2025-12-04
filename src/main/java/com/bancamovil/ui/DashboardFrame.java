package com.bancamovil.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.User;
import com.bancamovil.services.TransactionService;
import static com.bancamovil.ui.UITheme.BG_CARD;
import static com.bancamovil.ui.UITheme.BG_DARK;
import static com.bancamovil.ui.UITheme.BG_LIGHT;
import static com.bancamovil.ui.UITheme.BORDER;
import static com.bancamovil.ui.UITheme.FONT_BODY;
import static com.bancamovil.ui.UITheme.FONT_CAPTION;
import static com.bancamovil.ui.UITheme.FONT_MONEY;
import static com.bancamovil.ui.UITheme.FONT_SMALL;
import static com.bancamovil.ui.UITheme.FONT_SUBTITLE;
import com.bancamovil.ui.UITheme.ModernScrollBarUI;
import static com.bancamovil.ui.UITheme.PRIMARY;
import com.bancamovil.ui.UITheme.RoundedPanel;
import static com.bancamovil.ui.UITheme.SECONDARY;
import static com.bancamovil.ui.UITheme.TEXT_DARK;
import static com.bancamovil.ui.UITheme.TEXT_MUTED;
import static com.bancamovil.ui.UITheme.TEXT_PRIMARY;
import static com.bancamovil.ui.UITheme.TEXT_SECONDARY;
import static com.bancamovil.ui.UITheme.createAvatar;

public class DashboardFrame extends JFrame {

    private User user;
    private List<Account> cuentas;
    private Account cuentaActual;

    private final TransactionService transactionService = new TransactionService();
    private final AccountDAO accountDAO = new AccountDAO();  

    private JPanel mainContentPanel;
    private JPanel contentWrapperPanel;
    private JPanel accountInfoPanel;
    private JPanel topHeaderPanel;
    private JPanel bottomButtonsPanel;
    
    // Mantenemos esto como JPanel, porque es el CONTENIDO
    private JPanel sidebarPanel; 

    private JLabel lblSaldo;
    private JLabel lblTipoCuenta;
    private JComboBox<Account> comboCuentas;
    
    private String activeMenu = "Inicio";

    public DashboardFrame(User user, List<Account> cuentas) {
        this.user = user;
        this.cuentas = (cuentas == null || cuentas.isEmpty())
                ? accountDAO.obtenerCuentasPorUsuario(user.getId())
                : cuentas;

        this.cuentaActual = (this.cuentas == null || this.cuentas.isEmpty())
                ? null
                : this.cuentas.get(0);

        initUI();
        mostrarDashboardHome();
        setVisible(true);
    }

    private void initUI() {
        setTitle("Banca Digital - Dashboard");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600)); 
        setLayout(new BorderLayout());

        // 1. Creamos el contenido del menú
        sidebarPanel = createLeftMenu(); 
        
        // 2. Lo envolvemos en el ScrollPane
        JScrollPane sidebarScroll = new JScrollPane(sidebarPanel);
        sidebarScroll.setBorder(null);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Estilo del scrollbar
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);
        sidebarScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        sidebarScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        
        // Hacemos el scroll transparente
        sidebarScroll.setOpaque(false);
        sidebarScroll.getViewport().setOpaque(false); 

        add(sidebarScroll, BorderLayout.WEST);
        
        // Panel derecho (contenido)
        JScrollPane scrollPane = new JScrollPane(createRightContentArea());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createLeftMenu() {
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente oscuro
                GradientPaint gradient = new GradientPaint(
                    0, 0, BG_DARK,
                    0, getHeight(), new Color(20, 30, 50)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        // ---------------------------------------------------------
        // HEADER DEL MENÚ
        // ---------------------------------------------------------
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 20, 10, 20)); 

        // 1. Logo (Círculo)
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 54;
                int x = (getWidth() - size) / 2;
                int y = 20; 
                
                // Sombra suave (Glow)
                for (int i = 12; i > 0; i--) {
                    g2.setColor(new Color(99, 102, 241, 12 - i));
                    g2.fill(new Ellipse2D.Float(x - i, y - i, size + i*2, size + i*2));
                }
                
                // Círculo
                GradientPaint gradient = new GradientPaint(
                    x, y, PRIMARY,
                    x + size, y + size, SECONDARY
                );
                g2.setPaint(gradient);
                g2.fill(new Ellipse2D.Float(x, y, size, size));
                
                // Letra "B"
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (size - fm.stringWidth("B")) / 2;
                int textY = y + (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString("B", textX, textY);
                
                g2.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(280, 90)); 
        logoPanel.setMaximumSize(new Dimension(280, 90));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Título
        JLabel lblTitulo = new JLabel("Banca Digital");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Usuario
        String initials = getInitials(user.getNombreCompleto());
        JPanel avatarPanel = createAvatar(initials, 34, new Color(255, 255, 255, 30));
        
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        userRow.setOpaque(false);
        userRow.add(avatarPanel);
        
        JLabel lblSubtitulo = new JLabel(user.getNombreCompleto());
        lblSubtitulo.setFont(FONT_BODY);
        lblSubtitulo.setForeground(TEXT_SECONDARY);
        userRow.add(lblSubtitulo);
        userRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoPanel);
        headerPanel.add(Box.createVerticalStrut(2)); 
        headerPanel.add(lblTitulo);
        headerPanel.add(Box.createVerticalStrut(12)); 
        headerPanel.add(userRow);

        left.add(headerPanel);
        // AUMENTÉ ESTE ESPACIO DE 5 A 20
        left.add(Box.createVerticalStrut(20));

        // ---------------------------------------------------------
        // MENÚ DE NAVEGACIÓN
        // ---------------------------------------------------------
        JPanel menuWrapper = new JPanel();
        menuWrapper.setLayout(new BoxLayout(menuWrapper, BoxLayout.Y_AXIS));
        menuWrapper.setOpaque(false);
        menuWrapper.setBorder(new EmptyBorder(0, 12, 0, 12));

        // Sección principal
        JLabel menuTitle = new JLabel("MENÚ PRINCIPAL");
        menuTitle.setFont(FONT_CAPTION);
        menuTitle.setForeground(TEXT_MUTED);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        menuWrapper.add(menuTitle);

        menuWrapper.add(createMenuButton("Inicio", e -> { activeMenu = "Inicio"; mostrarDashboardHome(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Transferir", e -> { activeMenu = "Transferir"; mostrarTransferencia(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Pagar servicios", e -> { activeMenu = "Pagar servicios"; mostrarPagoServicios(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Recargar saldo", e -> { activeMenu = "Recargar saldo"; mostrarRecarga(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Historial", e -> { activeMenu = "Historial"; mostrarHistorial(); refreshSidebar(); }));
        
        menuWrapper.add(Box.createVerticalStrut(15));
        
        // Sección cuenta
        JLabel accountTitle = new JLabel("MI CUENTA");
        accountTitle.setFont(FONT_CAPTION);
        accountTitle.setForeground(TEXT_MUTED);
        accountTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        menuWrapper.add(accountTitle);
        
        menuWrapper.add(createMenuButton("Mi Perfil", e -> { activeMenu = "Mi Perfil"; mostrarPerfil(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Nueva Tarjeta", e -> { activeMenu = "Nueva Tarjeta"; mostrarSolicitarTarjeta(); refreshSidebar(); }));
        menuWrapper.add(Box.createVerticalStrut(4));
        menuWrapper.add(createMenuButton("Nueva Cuenta", e -> { activeMenu = "Nueva Cuenta"; mostrarCrearCuenta(); refreshSidebar(); }));

        left.add(menuWrapper);
        left.add(Box.createVerticalGlue()); 

        // Botón cerrar sesión
        JPanel logoutWrapper = new JPanel();
        logoutWrapper.setOpaque(false);
        logoutWrapper.setBorder(new EmptyBorder(10, 12, 20, 12));
        logoutWrapper.setLayout(new BoxLayout(logoutWrapper, BoxLayout.Y_AXIS));
        
        JPanel separatorLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 0),
                    getWidth()/2, 0, BORDER,
                    true
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        separatorLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorLine.setOpaque(false);
        
        JButton btnLogout = new JButton("Cerrar sesión") {
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
                
                if (hoverProgress > 0) {
                    g2.setColor(new Color(UITheme.ERROR.getRed(), UITheme.ERROR.getGreen(), UITheme.ERROR.getBlue(), (int)(hoverProgress * 20)));
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setFont(FONT_BODY);
        btnLogout.setForeground(UITheme.ERROR);
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setBorder(new EmptyBorder(14, 16, 14, 16));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setMaximumSize(new Dimension(250, 52));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        
        logoutWrapper.add(separatorLine);
        logoutWrapper.add(Box.createVerticalStrut(10));
        logoutWrapper.add(btnLogout);
        
        left.add(logoutWrapper);

        return left;
    }
    
    private void refreshSidebar() {
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }
    
    private JButton createMenuButton(String text, ActionListener action) {
        JButton btn = new JButton(text) {
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
                
                boolean isActive = getText().equals(activeMenu);
                float progress = isActive ? 1f : hoverProgress;
                
                if (progress > 0) {
                    Color bgColor = new Color(
                        PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(),
                        (int)(progress * 25)
                    );
                    g2.setColor(bgColor);
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                    
                    g2.setColor(PRIMARY);
                    int indicatorHeight = (int)(getHeight() * 0.6 * progress);
                    int indicatorY = (getHeight() - indicatorHeight) / 2;
                    g2.fillRoundRect(0, indicatorY, 4, indicatorHeight, 2, 2);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(text.equals(activeMenu) ? TEXT_PRIMARY : TEXT_SECONDARY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 16, 12, 16));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(250, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btn.addActionListener(e -> {
            action.actionPerformed(e);
            Container parent = btn.getParent();
            if (parent != null) parent.repaint();
        });
        
        return btn;
    }
    
    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "U";
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private JPanel createRightContentArea() {
        contentWrapperPanel = new JPanel(new GridBagLayout());
        contentWrapperPanel.setBackground(BG_LIGHT);
        contentWrapperPanel.setBorder(new EmptyBorder(28, 32, 28, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.weightx = 1.0;

        topHeaderPanel = createTopHeaderPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentWrapperPanel.add(topHeaderPanel, gbc);

        accountInfoPanel = createAccountInfoPanel();
        gbc.gridy = 1;
        contentWrapperPanel.add(accountInfoPanel, gbc);

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(true);
        mainContentPanel.setBackground(BG_LIGHT);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentWrapperPanel.add(mainContentPanel, gbc);
        
        bottomButtonsPanel = createBottomButtonsPanel();
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentWrapperPanel.add(bottomButtonsPanel, gbc);

        return contentWrapperPanel;
    }

    private JPanel createTopHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String saludo = getSaludo();
        JLabel lblUser = new JLabel(saludo + ", " + user.getNombre());
        lblUser.setForeground(TEXT_DARK);
        lblUser.setFont(FONT_SUBTITLE);
        
        JLabel lblFecha = new JLabel(getFechaActual());
        lblFecha.setForeground(TEXT_MUTED);
        lblFecha.setFont(FONT_SMALL);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(lblUser);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(lblFecha);
        
        panel.add(leftPanel, BorderLayout.WEST);

        return panel;
    }
    
    private String getSaludo() {
        int hora = java.time.LocalTime.now().getHour();
        if (hora < 12) return "Buenos días";
        if (hora < 19) return "Buenas tardes";
        return "Buenas noches";
    }
    
    private String getFechaActual() {
        return java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"))
        );
    }

    private JPanel createAccountInfoPanel() {
        RoundedPanel card = new RoundedPanel(24, BG_CARD, true);
        card.setLayout(new BorderLayout(24, 0));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblCuentaTitle = new JLabel("Cuenta activa");
        lblCuentaTitle.setFont(FONT_SMALL);
        lblCuentaTitle.setForeground(TEXT_SECONDARY);
        lblCuentaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboCuentas = new UITheme.RoundedComboBox<>();
        comboCuentas.setFont(FONT_BODY);
        comboCuentas.setPreferredSize(new Dimension(300, 45));
        comboCuentas.setMaximumSize(new Dimension(300, 45));
        if (cuentas != null) {
            for (Account a : cuentas) comboCuentas.addItem(a);
        }
        comboCuentas.addActionListener(e -> cambiarCuenta());
        comboCuentas.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(lblCuentaTitle);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(comboCuentas);

        card.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel lblSaldoTitle = new JLabel("Saldo disponible");
        lblSaldoTitle.setFont(FONT_SMALL);
        lblSaldoTitle.setForeground(TEXT_SECONDARY);
        lblSaldoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSaldo = new JLabel("S/. 0.00");
        lblSaldo.setFont(FONT_MONEY);
        lblSaldo.setForeground(UITheme.SUCCESS);
        lblSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTipoCuenta = new JLabel("");
        lblTipoCuenta.setFont(FONT_SMALL);
        lblTipoCuenta.setForeground(TEXT_SECONDARY);
        lblTipoCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblSaldoTitle);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(lblSaldo);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(lblTipoCuenta);
        centerPanel.add(Box.createVerticalGlue());

        card.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JButton btnTarjeta = UITheme.createActionButton("+ Nueva Tarjeta", PRIMARY, "");
        btnTarjeta.addActionListener(e -> mostrarSolicitarTarjeta());
        btnTarjeta.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnTarjeta.setMaximumSize(new Dimension(180, 44));

        JButton btnCuenta = UITheme.createActionButton("+ Nueva Cuenta", SECONDARY, "");
        btnCuenta.addActionListener(e -> mostrarCrearCuenta());
        btnCuenta.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnCuenta.setMaximumSize(new Dimension(180, 44));

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(btnTarjeta);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnCuenta);
        rightPanel.add(Box.createVerticalGlue());

        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }
    
    private JPanel createBottomButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        return panel;
    }

    public void mostrarDashboardHome() {
        topHeaderPanel.setVisible(true);
        accountInfoPanel.setVisible(true);
        bottomButtonsPanel.setVisible(true);
        setMainContent(new DashboardHomePanel(this, cuentaActual, transactionService));
        actualizarDatos();
    }

    public void mostrarTransferencia() {
        if (validarCuenta()) {
            ocultarPaneles();
            setMainContent(new TransferPanel(this, cuentaActual));
        }
    }

    public void mostrarPagoServicios() {
        if (validarCuenta()) {
            ocultarPaneles();
            setMainContent(new PayServicePanel(this, cuentaActual));
        }
    }

    public void mostrarRecarga() {
        if (validarCuenta()) {
            ocultarPaneles();
            setMainContent(new RechargePanel(this, cuentaActual));
        }
    }

    public void mostrarHistorial() {
        if (validarCuenta()) {
            try {
                ocultarPaneles();
                HistoryPanel historyPanel = new HistoryPanel(this, cuentaActual);
                setMainContent(historyPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarPerfil() {
        ocultarPaneles();
        setMainContent(new ProfilePanel(this, user));
    }

    public void mostrarSolicitarTarjeta() {
        ocultarPaneles();
        setMainContent(new NewCardPanel(this, user));
    }

    public void mostrarCrearCuenta() {
        ocultarPaneles();
        setMainContent(new NewAccountPanel(this, user));
    }

    private void ocultarPaneles() {
        topHeaderPanel.setVisible(false);
        accountInfoPanel.setVisible(false);
        bottomButtonsPanel.setVisible(false);
    }

    private boolean validarCuenta() {
        if (cuentaActual == null) {
            JOptionPane.showMessageDialog(this, "No hay cuenta seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void setMainContent(JPanel panel) {
        mainContentPanel.removeAll();
        panel.setOpaque(true);
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        contentWrapperPanel.revalidate();
        contentWrapperPanel.repaint();
    }

    public void actualizarDatos() {
        if (cuentaActual == null || lblSaldo == null) return;

        Account refreshed = accountDAO.obtenerPorId(cuentaActual.getId());
        if (refreshed != null) {
            this.cuentaActual = refreshed;
        } else {
            refreshed = this.cuentaActual;
        }

        lblSaldo.setText(refreshed.getSaldoFormateado());
        lblTipoCuenta.setText("Cuenta " + refreshed.getTipoCuenta().toString().toLowerCase() + " • " + refreshed.getMoneda());
        
        comboCuentas.removeAllItems();
        cuentas = accountDAO.obtenerCuentasPorUsuario(user.getId());
        if (cuentas != null) {
            for (Account a : cuentas) {
                comboCuentas.addItem(a);
                if (Objects.equals(a.getId(), cuentaActual.getId())) {
                    comboCuentas.setSelectedItem(a);
                }
            }
        }
        
        comboCuentas.repaint();

        Component[] components = mainContentPanel.getComponents();
        if (components.length > 0 && components[0] instanceof DashboardHomePanel homePanel) {
            homePanel.cargarMovimientos();
        }
    }

    private void cambiarCuenta() {
        Account a = (Account) comboCuentas.getSelectedItem();
        if (a != null && (cuentaActual == null || !Objects.equals(a.getId(), cuentaActual.getId()))) {
            cuentaActual = a;
            mostrarDashboardHome();
        }
    }

    public Account getCuentaActual() {
        return cuentaActual;
    }
    
    public User getUser() {
        return user;
    }
}     


