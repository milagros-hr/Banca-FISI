package com.bancamovil.ui;

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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.User;

public class NewAccountPanel extends JPanel {
    private DashboardFrame dashboard;
    private User user;
    private AccountDAO accountDAO;

    private String selectedTipoCuenta = "AHORROS";
    private String selectedMoneda = "PEN";
    
    private AccountTypeCard cardAhorros;
    private AccountTypeCard cardCorriente;
    private AccountTypeCard cardCTS;
    private UITheme.RoundedComboBox<String> comboMoneda;
    
    private JPanel cardsPanel;
    private JPanel mainContainer;
    
    // Colores tema claro
    private static final Color BG_LIGHT = new Color(248, 250, 252);
    private static final Color CARD_LIGHT = Color.WHITE;
    private static final Color CARD_HOVER = new Color(248, 250, 252);
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color BORDER_LIGHT = new Color(226, 232, 240);

    public NewAccountPanel(DashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        this.accountDAO = new AccountDAO();
        initComponents();
        setupResponsive();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);

        mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(25, 30, 25, 30));

        mainContainer.add(createHeader());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContainer.add(createUserInfoCard());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 18)));
        mainContainer.add(createInfoBanner());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContainer.add(createAccountTypeSection());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContainer.add(createCurrencySection());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContainer.add(createButtonPanel());
        mainContainer.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupResponsive() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                
                // Ajustar padding según ancho
                if (width < 600) {
                    mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
                } else if (width < 900) {
                    mainContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
                } else {
                    mainContainer.setBorder(new EmptyBorder(25, 30, 25, 30));
                }
                
                // Cambiar layout de cards según ancho
                if (cardsPanel != null) {
                    if (width < 800) {
                        cardsPanel.setLayout(new GridLayout(3, 1, 0, 10));
                        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 510));
                        // Actualizar cada card para modo compacto
                        updateCardsForCompactMode(true);
                    } else {
                        cardsPanel.setLayout(new GridLayout(1, 3, 15, 0));
                        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
                        // Actualizar cada card para modo normal
                        updateCardsForCompactMode(false);
                    }
                    cardsPanel.revalidate();
                }
                
                revalidate();
                repaint();
            }
        });
    }
    
    private void updateCardsForCompactMode(boolean compact) {
        if (cardAhorros != null) cardAhorros.setCompactMode(compact);
        if (cardCorriente != null) cardCorriente.setCompactMode(compact);
        if (cardCTS != null) cardCTS.setCompactMode(compact);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(59, 130, 246, 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        iconContainer.setPreferredSize(new Dimension(56, 56));
        iconContainer.setMaximumSize(new Dimension(56, 56));
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Crear Nueva Cuenta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Selecciona el tipo de cuenta que deseas abrir");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(subtitleLabel);

        iconContainer.setAlignmentY(Component.CENTER_ALIGNMENT);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconContainer);
        headerPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        headerPanel.add(textPanel);

        return headerPanel;
    }

    private JPanel createUserInfoCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(CARD_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 18, 15, 18));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        card.setOpaque(false);

        JPanel avatarPanel = UITheme.createAvatar(
            user.getNombreCompleto().substring(0, 2).toUpperCase(), 
            42, 
            UITheme.PRIMARY
        );

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 12, 0, 0));

        JLabel lblTitle = new JLabel("Titular de la cuenta");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(user.getNombreCompleto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(TEXT_PRIMARY);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDni = new JLabel("DNI: " + user.getDni());
        lblDni.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDni.setForeground(TEXT_MUTED);
        lblDni.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblNombre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(lblDni);

        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.X_AXIS));
        leftContainer.setOpaque(false);
        
        avatarPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        leftContainer.add(avatarPanel);
        leftContainer.add(infoPanel);

        card.add(leftContainer, BorderLayout.WEST);

        return card;
    }

    private JPanel createInfoBanner() {
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(34, 197, 94, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(new Color(34, 197, 94, 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                g2.dispose();
            }
        };
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(12, 15, 12, 15));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        banner.setOpaque(false);

        JLabel iconInfo = new JLabel("ℹ️");
        iconInfo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel infoText = new JLabel("<html><b>Información:</b> La cuenta se creará con saldo inicial de 0.00. " +
                "El número de cuenta se generará automáticamente.</html>");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoText.setForeground(new Color(21, 128, 61));
        infoText.setBorder(new EmptyBorder(0, 10, 0, 0));

        banner.add(iconInfo, BorderLayout.WEST);
        banner.add(infoText, BorderLayout.CENTER);

        return banner;
    }

    private JPanel createAccountTypeSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Selecciona el tipo de cuenta");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        cardAhorros = new AccountTypeCard(
            "💰", "AHORROS", 
            "Ideal para ahorro personal",
            "• Sin costo de mantenimiento\n• Intereses mensuales\n• Retiros ilimitados",
            UITheme.ACCENT,
            true
        );
        
        cardCorriente = new AccountTypeCard(
            "💳", "CORRIENTE", 
            "Para movimientos frecuentes",
            "• Chequera disponible\n• Sobregiros permitidos\n• Operaciones ilimitadas",
            UITheme.PRIMARY,
            false
        );
        
        cardCTS = new AccountTypeCard(
            "🏦", "CTS", 
            "Compensación por tiempo de servicio",
            "• Depósitos del empleador\n• Retiros según ley\n• Mayor rentabilidad",
            UITheme.ACCENT_ORANGE,
            false
        );

        cardAhorros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("AHORROS");
            }
        });

        cardCorriente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("CORRIENTE");
            }
        });

        cardCTS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("CTS");
            }
        });

        cardsPanel.add(cardAhorros);
        cardsPanel.add(cardCorriente);
        cardsPanel.add(cardCTS);

        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        section.add(cardsPanel);

        return section;
    }

    private void selectAccountType(String tipo) {
        selectedTipoCuenta = tipo;
        cardAhorros.setSelected(tipo.equals("AHORROS"));
        cardCorriente.setSelected(tipo.equals("CORRIENTE"));
        cardCTS.setSelected(tipo.equals("CTS"));
    }

    private JPanel createCurrencySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Selecciona la moneda");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel currencyCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(CARD_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.dispose();
            }
        };
        currencyCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        currencyCard.setBorder(new EmptyBorder(8, 15, 8, 15));
        currencyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        currencyCard.setMaximumSize(new Dimension(350, 60));
        currencyCard.setOpaque(false);

        JLabel currencyIcon = new JLabel("💱");
        currencyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        String[] monedas = {"PEN", "USD"};
        comboMoneda = new UITheme.RoundedComboBox<>(monedas);
        comboMoneda.setPreferredSize(new Dimension(180, 38));
        
        // Forzar colores del tema claro
        comboMoneda.setBackground(Color.WHITE);
        comboMoneda.setForeground(TEXT_PRIMARY);
        
        // Intentar cambiar el renderer para que sea completamente blanco
        try {
            javax.swing.plaf.ComboBoxUI ui = comboMoneda.getUI();
            if (ui instanceof javax.swing.plaf.basic.BasicComboBoxUI) {
                // Cambiar el color del botón desplegable
                comboMoneda.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
            }
        } catch (Exception ex) {
            // Si falla, al menos tenemos los colores básicos
        }
        
        comboMoneda.addActionListener(e -> selectedMoneda = (String) comboMoneda.getSelectedItem());

        currencyCard.add(currencyIcon);
        currencyCard.add(comboMoneda);

        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 12)));
        section.add(currencyCard);

        return section;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCrear = UITheme.createPrimaryButton("Crear Cuenta", "✓");
        btnCrear.setPreferredSize(new Dimension(180, 44));
        btnCrear.addActionListener(e -> crearCuenta());

        JButton btnCancelar = UITheme.createSecondaryButton("Cancelar", "✕");
        btnCancelar.setPreferredSize(new Dimension(140, 44));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnCancelar);

        return buttonPanel;
    }

    private void crearCuenta() {
        try {
            String tipoCuenta = selectedTipoCuenta;
            String moneda = selectedMoneda;

            String mensaje = String.format(
                    "¿Confirma la creación de una cuenta de tipo %s en %s?\n\n" +
                            "Titular: %s\n" +
                            "Saldo inicial: %s 0.00",
                    tipoCuenta, moneda,
                    user.getNombreCompleto(),
                    moneda.equals("PEN") ? "S/." : "$"
            );

            int confirmacion = JOptionPane.showConfirmDialog(this, mensaje,
                    "Confirmar Creación de Cuenta", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) return;

            String numeroCuenta;
            do {
                numeroCuenta = generarNumeroCuenta();
            } while (accountDAO.obtenerPorNumero(numeroCuenta) != null);

            Account nuevaCuenta = new Account();
            nuevaCuenta.setUsuarioId(user.getId());
            nuevaCuenta.setNumeroCuenta(numeroCuenta);
            nuevaCuenta.setTipoCuenta(Account.TipoCuenta.valueOf(tipoCuenta));
            nuevaCuenta.setSaldo(BigDecimal.ZERO);
            nuevaCuenta.setMoneda(Account.Moneda.valueOf(moneda));

            if (accountDAO.crearCuenta(nuevaCuenta)) {
                String mensajeExito = String.format(
                        "¡Cuenta creada exitosamente!\n\n" +
                                "Número de cuenta: %s\n" +
                                "Tipo: %s\n" +
                                "Moneda: %s\n" +
                                "Saldo inicial: %s 0.00",
                        numeroCuenta, tipoCuenta, moneda,
                        moneda.equals("PEN") ? "S/." : "$"
                );

                JOptionPane.showMessageDialog(this, mensajeExito,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dashboard.actualizarDatos();
                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al crear la cuenta. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        String banco = "191";
        String cuenta = String.format("%08d", random.nextInt(100000000));
        String verificador = String.valueOf(random.nextInt(10));

        String tipo = "01";
        if ("CORRIENTE".equals(selectedTipoCuenta)) tipo = "02";
        else if ("CTS".equals(selectedTipoCuenta)) tipo = "03";

        return banco + "-" + cuenta + "-" + verificador + "-" + tipo;
    }

    private class AccountTypeCard extends JPanel {
        private String icon;
        private String tipoCuenta;
        private String description;
        private String features;
        private Color accentColor;
        private boolean selected;
        private boolean compactMode = false;

        public AccountTypeCard(String icon, String tipoCuenta, String description, String features, Color accentColor, boolean selected) {
            this.icon = icon;
            this.tipoCuenta = tipoCuenta;
            this.description = description;
            this.features = features;
            this.accentColor = accentColor;
            this.selected = selected;

            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 16, 16, 16));

            buildContent();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }
        
        public void setCompactMode(boolean compact) {
            if (this.compactMode != compact) {
                this.compactMode = compact;
                buildContent();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (selected) {
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 150));
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);
            } else {
                g2.setColor(CARD_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
            
            g2.dispose();
        }

        private void buildContent() {
            removeAll();

            if (compactMode) {
                // Modo horizontal compacto para ventanas pequeñas
                setLayout(new BorderLayout(12, 0));
                setBorder(new EmptyBorder(12, 16, 12, 16));
                
                // Panel izquierdo con icono
                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setOpaque(false);
                
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                leftPanel.add(iconLabel);
                
                // Panel central con información
                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                centerPanel.setOpaque(false);
                
                JLabel titleLabel = new JLabel(tipoCuenta);
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                titleLabel.setForeground(selected ? accentColor : TEXT_PRIMARY);
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel descLabel = new JLabel(description);
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                descLabel.setForeground(TEXT_SECONDARY);
                descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel featuresLabel = new JLabel("<html>" + features.replace("\n", "<br>") + "</html>");
                featuresLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                featuresLabel.setForeground(TEXT_MUTED);
                featuresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                centerPanel.add(titleLabel);
                centerPanel.add(Box.createRigidArea(new Dimension(0, 3)));
                centerPanel.add(descLabel);
                centerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
                centerPanel.add(featuresLabel);
                
                add(leftPanel, BorderLayout.WEST);
                add(centerPanel, BorderLayout.CENTER);
                
                if (selected) {
                    JLabel checkLabel = new JLabel("✓");
                    checkLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    checkLabel.setForeground(accentColor);
                    add(checkLabel, BorderLayout.EAST);
                }
                
            } else {
                // Modo vertical para ventanas amplias
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setBorder(new EmptyBorder(16, 16, 16, 16));
                
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
                iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel titleLabel = new JLabel(tipoCuenta);
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
                titleLabel.setForeground(selected ? accentColor : TEXT_PRIMARY);
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel descLabel = new JLabel(description);
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                descLabel.setForeground(TEXT_SECONDARY);
                descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel featuresLabel = new JLabel("<html>" + features.replace("\n", "<br>") + "</html>");
                featuresLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                featuresLabel.setForeground(TEXT_MUTED);
                featuresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                add(iconLabel);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(titleLabel);
                add(Box.createRigidArea(new Dimension(0, 4)));
                add(descLabel);
                add(Box.createRigidArea(new Dimension(0, 8)));
                add(featuresLabel);
                add(Box.createVerticalGlue());

                if (selected) {
                    JLabel checkLabel = new JLabel("✓ Seleccionado");
                    checkLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    checkLabel.setForeground(accentColor);
                    checkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    add(Box.createRigidArea(new Dimension(0, 8)));
                    add(checkLabel);
                }
            }

            revalidate();
            repaint();
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            buildContent();
        }
    }
}