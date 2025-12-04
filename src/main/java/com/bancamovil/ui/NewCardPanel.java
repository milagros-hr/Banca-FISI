package com.bancamovil.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.CardDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Card;
import com.bancamovil.models.User;
import com.bancamovil.utils.CardUtils;

public class NewCardPanel extends JPanel {
    private DashboardFrame dashboard;
    private User user;
    private AccountDAO accountDAO;
    private CardDAO cardDAO;
  
    private UITheme.RoundedComboBox<String> comboTipoTarjeta;
    private UITheme.RoundedComboBox<String> comboRed;
    private JComboBox<Account> comboCuentas;
    private JCheckBox checkVincularCuenta;
    private JTextField txtLimiteCredito;
    private JPanel limitePanel;
    private JPanel cuentaPanel;
    
    private JPanel cardPreviewPanel;
    private JLabel previewCardNumber;
    private JLabel previewCardHolder;
    private JLabel previewCardExpiry;
    private JLabel previewCardType;
    private JLabel previewCardNetwork;
    
    private JPanel contentPanel;
    
    // Colores tema claro
    private static final Color BG_LIGHT = new Color(248, 250, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color SUCCESS_BG = new Color(220, 252, 231);
    private static final Color SUCCESS_BORDER = new Color(34, 197, 94);
    private static final Color SUCCESS_TEXT = new Color(22, 163, 74);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    public NewCardPanel(DashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        this.accountDAO = new AccountDAO();
        this.cardDAO = new CardDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);

        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_LIGHT);
        scrollPane.getViewport().setBackground(BG_LIGHT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Listener para hacer responsive
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayout();
            }
        });
    }

    private void adjustLayout() {
        if (contentPanel != null) {
            int width = getWidth();
            if (width < 900) {
                contentPanel.removeAll();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                
                JPanel formWrapper = new JPanel(new BorderLayout());
                formWrapper.setOpaque(false);
                formWrapper.add(createFormCard(), BorderLayout.NORTH);
                formWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
                formWrapper.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
                
                JPanel previewWrapper = new JPanel(new BorderLayout());
                previewWrapper.setOpaque(false);
                previewWrapper.add(createCardPreview(), BorderLayout.NORTH);
                previewWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
                previewWrapper.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
                
                contentPanel.add(formWrapper);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                contentPanel.add(previewWrapper);
            } else {
                contentPanel.removeAll();
                contentPanel.setLayout(new GridBagLayout());
                
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(0, 0, 0, 25);
                gbc.weightx = 0.55;
                gbc.weighty = 1.0;
                gbc.gridx = 0;
                gbc.gridy = 0;

                JPanel formWrapper = new JPanel(new BorderLayout());
                formWrapper.setOpaque(false);
                formWrapper.add(createFormCard(), BorderLayout.NORTH);
                contentPanel.add(formWrapper, gbc);

                gbc.gridx = 1;
                gbc.weightx = 0.45;
                gbc.insets = new Insets(0, 0, 0, 0);
                JPanel previewWrapper = new JPanel(new BorderLayout());
                previewWrapper.setOpaque(false);
                previewWrapper.add(createCardPreview(), BorderLayout.NORTH);
                contentPanel.add(previewWrapper, gbc);
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        mainPanel.add(createHeader());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createInfoBox());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(contentPanel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(59, 130, 246, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        iconContainer.setPreferredSize(new Dimension(60, 60));
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel("💳");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        iconLabel.setForeground(new Color(59, 130, 246));
        iconContainer.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Solicitar Nueva Tarjeta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Genera tu tarjeta virtual al instante");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subtitleLabel);

        headerPanel.add(iconContainer);
        headerPanel.add(textPanel);

        return headerPanel;
    }

    private JPanel createInfoBox() {
        JPanel infoBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SUCCESS_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(SUCCESS_BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        infoBox.setLayout(new BorderLayout(15, 0));
        infoBox.setBorder(new EmptyBorder(16, 20, 16, 20));
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        infoBox.setOpaque(false);

        JLabel iconInfo = new JLabel("i");
        iconInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        iconInfo.setForeground(SUCCESS_TEXT);
        iconInfo.setPreferredSize(new Dimension(30, 30));
        iconInfo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel infoText = new JLabel("<html>" +
                "<span style='color: #16a34a'>✓</span> La tarjeta será generada instantáneamente<br>" +
                "<span style='color: #16a34a'>✓</span> Tarjetas de débito deben estar vinculadas a una cuenta<br>" +
                "<span style='color: #16a34a'>✓</span> Tarjetas de crédito pueden ser independientes</html>");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoText.setForeground(SUCCESS_TEXT);

        infoBox.add(iconInfo, BorderLayout.WEST);
        infoBox.add(infoText, BorderLayout.CENTER);

        return infoBox;
    }

    private JPanel createFormCard() {
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        formCard.setOpaque(false);

        JLabel formTitle = new JLabel("Configuración de la Tarjeta");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        formTitle.setForeground(TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] tiposTarjeta = {"Débito", "Crédito"};
        comboTipoTarjeta = new UITheme.RoundedComboBox<>(tiposTarjeta);
        comboTipoTarjeta.addActionListener(e -> {
            actualizarFormulario();
            updateCardPreview();
        });

        String[] redesTarjeta = {"VISA", "MASTERCARD"};
        comboRed = new UITheme.RoundedComboBox<>(redesTarjeta);
        comboRed.addActionListener(e -> updateCardPreview());

        List<Account> cuentas = accountDAO.obtenerCuentasPorUsuario(user.getId());
        comboCuentas = new UITheme.RoundedComboBox<>();
        for (Account cuenta : cuentas) {
            comboCuentas.addItem(cuenta);
        }

        checkVincularCuenta = new JCheckBox("Vincular a una cuenta existente");
        checkVincularCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkVincularCuenta.setForeground(TEXT_PRIMARY);
        checkVincularCuenta.setOpaque(false);
        checkVincularCuenta.setFocusPainted(false);
        checkVincularCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkVincularCuenta.setVisible(false);
        checkVincularCuenta.addActionListener(e -> {
            comboCuentas.setEnabled(checkVincularCuenta.isSelected());
            cuentaPanel.setVisible(checkVincularCuenta.isSelected());
        });

        txtLimiteCredito = createStyledTextField();
        txtLimiteCredito.setText("5000.00");
        txtLimiteCredito.setVisible(false);

        JPanel tipoPanel = createInputPanel("Tipo de Tarjeta", comboTipoTarjeta);
        JPanel redPanel = createInputPanel("Red de Pago", comboRed);
        cuentaPanel = createInputPanel("Cuenta a Vincular", comboCuentas);
        limitePanel = createInputPanel("Límite de Crédito (opcional)", txtLimiteCredito);

        formCard.add(tipoPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 16)));
        formCard.add(redPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 16)));
        formCard.add(limitePanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 16)));
        formCard.add(checkVincularCuenta);
        formCard.add(Box.createRigidArea(new Dimension(0, 12)));
        formCard.add(cuentaPanel);

        actualizarFormulario();
        
        return formCard;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(12, 16, 12, 16));
        field.setOpaque(false);
        return field;
    }

    private JPanel createCardPreview() {
        JPanel previewContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        previewContainer.setLayout(new BoxLayout(previewContainer, BoxLayout.Y_AXIS));
        previewContainer.setBorder(new EmptyBorder(25, 25, 25, 25));
        previewContainer.setOpaque(false);

        JLabel previewTitle = new JLabel("Vista Previa de tu Tarjeta");
        previewTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        previewTitle.setForeground(TEXT_PRIMARY);
        previewTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        previewContainer.add(previewTitle);
        previewContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        cardPreviewPanel = createVirtualCard();
        
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(cardPreviewPanel);
        cardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContainer.add(cardWrapper);

        previewContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel disclaimer = new JLabel("<html><center>Esta es una vista previa. Los datos reales<br>serán generados al confirmar la solicitud.</center></html>");
        disclaimer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        disclaimer.setForeground(TEXT_MUTED);
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);
        disclaimer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel disclaimerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        disclaimerPanel.setOpaque(false);
        disclaimerPanel.add(disclaimer);
        disclaimerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContainer.add(disclaimerPanel);

        return previewContainer;
    }

    private JPanel createVirtualCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth();
                int h = getHeight();

                String tipo = (String) comboTipoTarjeta.getSelectedItem();
                boolean esDebito = tipo != null && tipo.contains("Débito");

                GradientPaint gradient;
                if (esDebito) {
                    gradient = new GradientPaint(0, 0, new Color(59, 130, 246), w, h, new Color(37, 99, 235));
                } else {
                    gradient = new GradientPaint(0, 0, new Color(139, 92, 246), w, h, new Color(109, 40, 217));
                }

                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));

                GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40),
                    0, h / 3, new Color(255, 255, 255, 0)
                );
                g2.setPaint(shine);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h / 3, 24, 24));

                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(w - 120, -40, 200, 200);
                g2.fillOval(w - 80, h - 60, 150, 150);

                g2.dispose();
            }
        };
        card.setLayout(null);
        card.setPreferredSize(new Dimension(340, 200));
        card.setMinimumSize(new Dimension(280, 165));
        card.setMaximumSize(new Dimension(340, 200));
        card.setOpaque(false);

        JLabel chipLabel = new JLabel("▣");
        chipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        chipLabel.setForeground(new Color(255, 215, 0, 200));
        chipLabel.setBounds(25, 45, 50, 40);
        card.add(chipLabel);

        previewCardNetwork = new JLabel("VISA");
        previewCardNetwork.setFont(new Font("Segoe UI", Font.BOLD, 22));
        previewCardNetwork.setForeground(Color.WHITE);
        previewCardNetwork.setBounds(270, 20, 60, 30);
        card.add(previewCardNetwork);

        previewCardNumber = new JLabel("•••• •••• •••• ••••");
        previewCardNumber.setFont(new Font("Consolas", Font.BOLD, 20));
        previewCardNumber.setForeground(Color.WHITE);
        previewCardNumber.setBounds(25, 95, 280, 30);
        card.add(previewCardNumber);

        JLabel holderLabel = new JLabel("TITULAR");
        holderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        holderLabel.setForeground(new Color(255, 255, 255, 150));
        holderLabel.setBounds(25, 135, 100, 15);
        card.add(holderLabel);

        previewCardHolder = new JLabel(user.getNombreCompleto().toUpperCase());
        previewCardHolder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        previewCardHolder.setForeground(Color.WHITE);
        previewCardHolder.setBounds(25, 150, 180, 20);
        card.add(previewCardHolder);

        JLabel expiryLabel = new JLabel("VÁLIDA HASTA");
        expiryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        expiryLabel.setForeground(new Color(255, 255, 255, 150));
        expiryLabel.setBounds(220, 135, 100, 15);
        card.add(expiryLabel);

        previewCardExpiry = new JLabel("••/••");
        previewCardExpiry.setFont(new Font("Segoe UI", Font.BOLD, 13));
        previewCardExpiry.setForeground(Color.WHITE);
        previewCardExpiry.setBounds(220, 150, 60, 20);
        card.add(previewCardExpiry);

        previewCardType = new JLabel("DÉBITO");
        previewCardType.setFont(new Font("Segoe UI", Font.BOLD, 11));
        previewCardType.setForeground(new Color(255, 255, 255, 180));
        previewCardType.setBounds(25, 175, 100, 15);
        card.add(previewCardType);

        return card;
    }

    private void updateCardPreview() {
        String tipo = (String) comboTipoTarjeta.getSelectedItem();
        String red = (String) comboRed.getSelectedItem();

        if (tipo != null) {
            previewCardType.setText(tipo.contains("Débito") ? "DÉBITO" : "CRÉDITO");
        }
        if (red != null) {
            previewCardNetwork.setText(red);
        }

        cardPreviewPanel.repaint();
    }

    private JPanel createInputPanel(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JComboBox) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            field.setPreferredSize(new Dimension(300, 44));
        } else {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSolicitar = createPrimaryButton("Generar Tarjeta");
        btnSolicitar.setPreferredSize(new Dimension(180, 46));
        btnSolicitar.addActionListener(e -> solicitarTarjeta());

        JButton btnCancelar = createSecondaryButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 46));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());

        buttonPanel.add(btnSolicitar);
        buttonPanel.add(btnCancelar);

        return buttonPanel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(37, 99, 235));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(59, 130, 246));
                } else {
                    g2.setColor(new Color(59, 130, 246));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(226, 232, 240));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(241, 245, 249));
                } else {
                    g2.setColor(Color.WHITE);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(TEXT_SECONDARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ════════════════════════════════════════════════════════════════════════
    // LÓGICA DE BACKEND - SIN MODIFICACIONES
    // ════════════════════════════════════════════════════════════════════════

    private void solicitarTarjeta() {
        try {
            String tipoSeleccionado = (String) comboTipoTarjeta.getSelectedItem();
            String tipoTarjeta = tipoSeleccionado.contains("Débito") ? "DEBITO" : "CREDITO";
            String red = (String) comboRed.getSelectedItem();
            boolean esDebito = "DEBITO".equals(tipoTarjeta);

            if (esDebito && comboCuentas.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una cuenta para vincular la tarjeta de débito.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!esDebito && checkVincularCuenta.isSelected() && comboCuentas.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una cuenta para vincular.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String mensaje = "¿Confirma la solicitud de una tarjeta de " + tipoTarjeta + " " + red + "?";
            int confirmacion = JOptionPane.showConfirmDialog(this, mensaje,
                    "Confirmar Solicitud", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) return;

            Card nuevaTarjeta = new Card();

            String numeroTarjeta;
            do {
                numeroTarjeta = CardUtils.generarNumeroTarjeta(red, null);
            } while (cardDAO.existeNumeroTarjeta(numeroTarjeta));

            nuevaTarjeta.setNumeroTarjeta(numeroTarjeta);
            nuevaTarjeta.setTipoTarjeta(Card.TipoTarjeta.valueOf(tipoTarjeta));
            nuevaTarjeta.setNombreTitular(user.getNombreCompleto().toUpperCase());
            nuevaTarjeta.setFechaVencimiento(CardUtils.generarFechaVencimiento());
            nuevaTarjeta.setCvv(CardUtils.generarCVV());
            nuevaTarjeta.setActiva(true);

            if (esDebito || checkVincularCuenta.isSelected()) {
                Account cuentaSeleccionada = (Account) comboCuentas.getSelectedItem();
                nuevaTarjeta.setCuentaId(cuentaSeleccionada.getId());
            } else {
                nuevaTarjeta.setCuentaId(null);
            }

            if (!esDebito) {
                String limiteStr = txtLimiteCredito.getText().trim();
                if (!limiteStr.isEmpty()) {
                    nuevaTarjeta.setLimiteCredito(new BigDecimal(limiteStr));
                } else {
                    nuevaTarjeta.setLimiteCredito(new BigDecimal("5000.00"));
                }
            } else {
                nuevaTarjeta.setLimiteCredito(null);
            }

            if (cardDAO.crearTarjeta(nuevaTarjeta)) {
                String mensajeExito = String.format(
                        "¡Tarjeta generada exitosamente!\n\n" +
                                "Tipo: %s %s\n" +
                                "Número: %s\n" +
                                "Titular: %s\n" +
                                "Vencimiento: %s\n" +
                                "CVV: %s",
                        tipoTarjeta, red,
                        CardUtils.formatearNumeroTarjeta(numeroTarjeta),
                        nuevaTarjeta.getNombreTitular(),
                        nuevaTarjeta.getFechaFormateada(),
                        nuevaTarjeta.getCvv()
                );

                JOptionPane.showMessageDialog(this, mensajeExito,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al generar la tarjeta. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El límite de crédito debe ser un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarFormulario() {
        String tipoSeleccionado = (String) comboTipoTarjeta.getSelectedItem();
        boolean esDebito = tipoSeleccionado != null && tipoSeleccionado.contains("Débito");

        checkVincularCuenta.setVisible(!esDebito);
        limitePanel.setVisible(!esDebito);
        txtLimiteCredito.setVisible(!esDebito);

        if (esDebito) {
            comboCuentas.setEnabled(true);
            cuentaPanel.setVisible(true);
            checkVincularCuenta.setSelected(true);
        } else {
            cuentaPanel.setVisible(checkVincularCuenta.isSelected());
            comboCuentas.setEnabled(checkVincularCuenta.isSelected());
        }

        revalidate();
        repaint();
    }
}