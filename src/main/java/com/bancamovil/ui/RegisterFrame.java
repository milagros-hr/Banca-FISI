package com.bancamovil.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.UserDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Account.Moneda;
import com.bancamovil.models.Account.TipoCuenta;
import com.bancamovil.models.User;

public class RegisterFrame extends JFrame {
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private LoginFrame loginFrame;
    
    private UITheme.RoundedTextField txtDni, txtNombre, txtApellido, txtEmail, txtTelefono;
    private UITheme.RoundedPasswordField txtPassword, txtConfirmPassword;
    
    private JLabel lblDniStatus, lblNombreStatus, lblApellidoStatus, lblEmailStatus;
    private JLabel lblPasswordStatus, lblConfirmPasswordStatus;
    
    private int currentStep = 1;
    private JPanel step1Indicator, step2Indicator;
    private JLabel step1Label, step2Label;
    private CardLayout cardLayout;
    private JPanel stepsContainer;
    
    private JButton btnNext, btnBack, btnRegister;
    // Necesitamos una referencia al panel de navegación para actualizarlo
    private JPanel navigationPanel; 

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.userDAO = new UserDAO();
        this.accountDAO = new AccountDAO();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Crear Cuenta - Banca Digital");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = Math.min(550, (int)(screenWidth * 0.40));
        int windowHeight = Math.min(750, (int)(screenHeight * 0.85));
        
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setResizable(true);
        setMinimumSize(new Dimension(450, 600));
        
        getContentPane().setBackground(UITheme.BG_DARK);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, UITheme.BG_DARK, 0, getHeight(), UITheme.BG_CARD);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        JPanel contentContainer = createContentPanel();
        
        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UITheme.BG_DARK);
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new UITheme.ModernScrollBarUI());
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                revalidate();
                repaint();
            }
        });
    }
    
    private JPanel createContentPanel() {
        JPanel paddedWrapper = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = getParent() != null ? getParent().getWidth() : 500;
                return d;
            }
        };
        paddedWrapper.setOpaque(false);
        paddedWrapper.setBorder(new EmptyBorder(40, 50, 40, 50)); 
        paddedWrapper.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(createHeaderPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        contentPanel.add(createStepIndicator());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        contentPanel.add(createFormCard());
        
        paddedWrapper.add(contentPanel, BorderLayout.CENTER);
        return paddedWrapper;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS)); 
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        // 1. Círculo del Icono
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, UITheme.PRIMARY, getWidth(), getHeight(), UITheme.SECONDARY);
                g2.setPaint(gradient);
                g2.fillOval(0, 0, 64, 64);
                g2.dispose();
            }
        };
        iconCircle.setPreferredSize(new Dimension(64, 64));
        iconCircle.setMaximumSize(new Dimension(64, 64));
        iconCircle.setOpaque(false);
        iconCircle.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconLabel.setForeground(Color.WHITE);
        iconCircle.add(iconLabel);
        
        // 2. Panel de Texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Crear Cuenta");
        titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Regístrate en Banca Digital");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subtitleLabel);

        iconCircle.setAlignmentY(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createHorizontalGlue()); 
        headerPanel.add(iconCircle);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        headerPanel.add(textPanel);
        headerPanel.add(Box.createHorizontalGlue());
        
        return headerPanel;
    }
    
    private JPanel createStepIndicator() {
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new BoxLayout(stepPanel, BoxLayout.X_AXIS));
        stepPanel.setOpaque(false);
        stepPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        stepPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        step1Indicator = createStepCircle("1", true);
        step1Label = new JLabel("Datos Personales");
        step1Label.setFont(UITheme.FONT_SMALL_BOLD);
        step1Label.setForeground(UITheme.PRIMARY);
        
        JPanel stepLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(currentStep >= 2 ? UITheme.PRIMARY : UITheme.BORDER);
                g2.fillRect(0, getHeight()/2 - 1, getWidth(), 2);
                g2.dispose();
            }
        };
        stepLine.setOpaque(false);
        stepLine.setPreferredSize(new Dimension(50, 2));
        stepLine.setMaximumSize(new Dimension(80, 30));
        
        step2Indicator = createStepCircle("2", false);
        step2Label = new JLabel("Contraseña");
        step2Label.setFont(UITheme.FONT_SMALL_BOLD);
        step2Label.setForeground(UITheme.TEXT_SECONDARY);
        
        JPanel step1Panel = new JPanel();
        step1Panel.setLayout(new BoxLayout(step1Panel, BoxLayout.Y_AXIS));
        step1Panel.setOpaque(false);
        step1Panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        step1Indicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        step1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        step1Panel.add(step1Indicator);
        step1Panel.add(Box.createRigidArea(new Dimension(0, 8)));
        step1Panel.add(step1Label);
        
        JPanel step2Panel = new JPanel();
        step2Panel.setLayout(new BoxLayout(step2Panel, BoxLayout.Y_AXIS));
        step2Panel.setOpaque(false);
        step2Panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        step2Indicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        step2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        step2Panel.add(step2Indicator);
        step2Panel.add(Box.createRigidArea(new Dimension(0, 8)));
        step2Panel.add(step2Label);
        
        stepPanel.add(Box.createHorizontalGlue());
        stepPanel.add(step1Panel);
        stepPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        stepPanel.add(stepLine);
        stepPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        stepPanel.add(step2Panel);
        stepPanel.add(Box.createHorizontalGlue());
        
        return stepPanel;
    }
    
    private JPanel createStepCircle(String number, boolean active) {
        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isActive = (number.equals("1") && currentStep >= 1) || 
                                   (number.equals("2") && currentStep >= 2);
                
                if (isActive) {
                    GradientPaint gradient = new GradientPaint(0, 0, UITheme.PRIMARY, getWidth(), getHeight(), UITheme.SECONDARY);
                    g2.setPaint(gradient);
                } else {
                    g2.setColor(UITheme.BG_CARD);
                }
                g2.fillOval(0, 0, 40, 40);
                
                if (!isActive) {
                    g2.setColor(UITheme.BORDER);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(1, 1, 37, 37);
                }
                
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(40, 40));
        circle.setMaximumSize(new Dimension(40, 40));
        circle.setOpaque(false);
        circle.setLayout(new GridBagLayout());
        
        JLabel numLabel = new JLabel(number);
        numLabel.setFont(UITheme.FONT_BODY_BOLD);
        numLabel.setForeground(active ? Color.WHITE : UITheme.TEXT_SECONDARY);
        circle.add(numLabel);
        
        return circle;
    }
    
    private void updateStepIndicators() {
        step1Indicator.repaint();
        step2Indicator.repaint();
        
        step1Label.setForeground(currentStep >= 1 ? UITheme.PRIMARY : UITheme.TEXT_SECONDARY);
        step2Label.setForeground(currentStep >= 2 ? UITheme.PRIMARY : UITheme.TEXT_SECONDARY);
        
        Component[] step1Comps = step1Indicator.getComponents();
        if (step1Comps.length > 0 && step1Comps[0] instanceof JLabel) {
            ((JLabel)step1Comps[0]).setForeground(Color.WHITE);
        }
        
        Component[] step2Comps = step2Indicator.getComponents();
        if (step2Comps.length > 0 && step2Comps[0] instanceof JLabel) {
            ((JLabel)step2Comps[0]).setForeground(currentStep >= 2 ? Color.WHITE : UITheme.TEXT_SECONDARY);
        }
    }
    
    private JPanel createFormCard() {
        UITheme.RoundedPanel cardPanel = new UITheme.RoundedPanel(24, UITheme.BG_CARD, true);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(35, 35, 35, 35));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardLayout = new CardLayout();
        stepsContainer = new JPanel(cardLayout);
        stepsContainer.setOpaque(false);
        stepsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        stepsContainer.add(createStep1Panel(), "step1");
        stepsContainer.add(createStep2Panel(), "step2");
        
        cardPanel.add(stepsContainer);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(createButtonsPanel());
        
        return cardPanel;
    }
    
    private JPanel createStep1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel stepTitle = new JLabel("📋 Información Personal");
        stepTitle.setFont(UITheme.FONT_SUBTITLE);
        stepTitle.setForeground(UITheme.TEXT_PRIMARY);
        stepTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(stepTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        txtDni = new UITheme.RoundedTextField();
        lblDniStatus = createStatusLabel();
        panel.add(createInputPanel("DNI", txtDni, lblDniStatus, "8 dígitos numéricos"));
        addValidationListener(txtDni, lblDniStatus, this::validateDni);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        txtNombre = new UITheme.RoundedTextField();
        lblNombreStatus = createStatusLabel();
        panel.add(createInputPanel("Nombre", txtNombre, lblNombreStatus, "Requerido"));
        addValidationListener(txtNombre, lblNombreStatus, this::validateNotEmpty);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        txtApellido = new UITheme.RoundedTextField();
        lblApellidoStatus = createStatusLabel();
        panel.add(createInputPanel("Apellido", txtApellido, lblApellidoStatus, "Requerido"));
        addValidationListener(txtApellido, lblApellidoStatus, this::validateNotEmpty);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        txtEmail = new UITheme.RoundedTextField();
        lblEmailStatus = createStatusLabel();
        panel.add(createInputPanel("Email", txtEmail, lblEmailStatus, "ejemplo@correo.com"));
        addValidationListener(txtEmail, lblEmailStatus, this::validateEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        txtTelefono = new UITheme.RoundedTextField();
        panel.add(createInputPanel("Teléfono", txtTelefono, null, "Opcional"));
        
        return panel;
    }
    
    private JPanel createStep2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel stepTitle = new JLabel("Seguridad");
        stepTitle.setFont(UITheme.FONT_SUBTITLE);
        stepTitle.setForeground(UITheme.TEXT_PRIMARY);
        stepTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(stepTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        txtPassword = new UITheme.RoundedPasswordField();
        lblPasswordStatus = createStatusLabel();
        panel.add(createInputPanel("Contraseña", txtPassword, lblPasswordStatus, "Mínimo 4 caracteres"));
        addValidationListener(txtPassword, lblPasswordStatus, this::validatePassword);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        
        txtConfirmPassword = new UITheme.RoundedPasswordField();
        lblConfirmPasswordStatus = createStatusLabel();
        panel.add(createInputPanel("Confirmar Contraseña", txtConfirmPassword, lblConfirmPasswordStatus, "Debe coincidir"));
        addValidationListener(txtConfirmPassword, lblConfirmPasswordStatus, this::validateConfirmPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        panel.add(createInfoBox());
        
        return panel;
    }
    
    private JLabel createStatusLabel() {
        JLabel label = new JLabel("");
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(24, 24));
        return label;
    }
    
    private JPanel createInfoBox() {
        UITheme.RoundedPanel infoBox = new UITheme.RoundedPanel(12, new Color(34, 197, 94, 20), false);
        infoBox.setLayout(new BorderLayout());
        infoBox.setBorder(new EmptyBorder(14, 16, 14, 16));
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel checkIcon = new JLabel("");
        checkIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        checkIcon.setForeground(UITheme.SUCCESS);
        
        JLabel infoText = new JLabel("<html>Se creará automáticamente una cuenta de <b>AHORROS</b> en <b>SOLES</b></html>");
        infoText.setFont(UITheme.FONT_SMALL);
        infoText.setForeground(UITheme.SUCCESS);
        
        contentPanel.add(checkIcon);
        contentPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        contentPanel.add(infoText);
        
        infoBox.add(contentPanel, BorderLayout.CENTER);
        return infoBox;
    }
    
    // --- SECCIÓN CORREGIDA: BOTONES DEL MISMO TAMAÑO ---
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        // Importante para que los elementos hijos se estiren
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Usamos GridLayout(1, 0) para la fila de navegación.
        // El '0' significa que el número de columnas es dinámico.
        // Si hay 1 botón visible, ocupa el 100%. Si hay 2, ocupan 50% cada uno.
        navigationPanel = new JPanel(new GridLayout(1, 0, 15, 0)); 
        navigationPanel.setOpaque(false);
        // Altura fija, ancho máximo para que se estire
        navigationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48)); 
        navigationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnBack = UITheme.createSecondaryButton("← Atrás");
        btnBack.setVisible(false); // Empieza oculto
        btnBack.addActionListener(e -> goToPreviousStep());
        
        btnNext = UITheme.createPrimaryButton("Siguiente →");
        btnNext.addActionListener(e -> goToNextStep());
        
        btnRegister = UITheme.createPrimaryButton("Crear Cuenta", "");
        btnRegister.setVisible(false);
        btnRegister.addActionListener(e -> register());
        
        // Contenedor para alternar entre "Siguiente" y "Registrar"
        JPanel rightSlot = new JPanel(new CardLayout());
        rightSlot.setOpaque(false);
        rightSlot.add(btnNext, "next");
        rightSlot.add(btnRegister, "register");
        
        // Añadimos al GridLayout.
        // Al inicio, btnBack es invisible, así que rightSlot ocupará todo el ancho.
        navigationPanel.add(btnBack);
        navigationPanel.add(rightSlot);
        
        // Botón Cancelar (ancho completo)
        JButton btnCancel = UITheme.createSecondaryButton("Cancelar");
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Ancho máximo para que coincida con los de arriba
        btnCancel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnCancel.addActionListener(e -> cancel());
        
        buttonsPanel.add(navigationPanel);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(btnCancel);
        
        return buttonsPanel;
    }
    
    private void goToNextStep() {
        if (currentStep == 1) {
            if (!validateStep1()) {
                return;
            }
            currentStep = 2;
            cardLayout.show(stepsContainer, "step2");
            
            // Mostrar botón Atrás
            btnBack.setVisible(true);
            
            // Cambiar Siguiente por Registrar
            CardLayout cl = (CardLayout) btnNext.getParent().getLayout();
            cl.show((JPanel)btnNext.getParent(), "register");
            
            // ¡IMPORTANTE! Forzar al layout a recalcular los tamaños ahora que hay 2 botones
            navigationPanel.revalidate();
            navigationPanel.repaint();
            
            updateStepIndicators();
        }
    }
    
    private void goToPreviousStep() {
        if (currentStep == 2) {
            currentStep = 1;
            cardLayout.show(stepsContainer, "step1");
            
            // Ocultar botón Atrás
            btnBack.setVisible(false);
            
            // Cambiar Registrar por Siguiente
            CardLayout cl = (CardLayout) btnNext.getParent().getLayout();
            cl.show((JPanel)btnNext.getParent(), "next");

            // ¡IMPORTANTE! Forzar al layout a recalcular para que "Siguiente" ocupe todo el ancho
            navigationPanel.revalidate();
            navigationPanel.repaint();
            
            updateStepIndicators();
        }
    }
    // --------------------------------------------------
    
    private boolean validateStep1() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
            showError("Complete todos los campos obligatorios");
            return false;
        }
        
        if (dni.length() != 8 || !dni.matches("\\d+")) {
            showError("El DNI debe tener exactamente 8 dígitos");
            return false;
        }
        
        if (!email.contains("@")) {
            showError("Ingrese un email válido");
            return false;
        }
        
        return true;
    }

    private JPanel createInputPanel(String label, JComponent field, JLabel statusLabel, String hint) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel labelRow = new JPanel();
        labelRow.setLayout(new BoxLayout(labelRow, BoxLayout.X_AXIS));
        labelRow.setOpaque(false);
        labelRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblField = new JLabel(label);
        lblField.setFont(UITheme.FONT_BODY_BOLD);
        lblField.setForeground(UITheme.TEXT_SECONDARY);
        
        labelRow.add(lblField);
        labelRow.add(Box.createHorizontalGlue());
        if (statusLabel != null) {
            labelRow.add(statusLabel);
        }

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(UITheme.FONT_CAPTION);
        hintLabel.setForeground(UITheme.TEXT_MUTED);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JTextField) {
            ((JTextField) field).setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        }
        
        panel.add(labelRow);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(hintLabel);

        return panel;
    }
    
    private void addValidationListener(JTextField field, JLabel statusLabel, ValidationFunction validator) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validate(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validate(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validate(); }
            
            private void validate() {
                String text = field.getText().trim();
                if (text.isEmpty()) {
                    statusLabel.setText("");
                    statusLabel.setForeground(UITheme.TEXT_SECONDARY);
                } else if (validator.validate(text)) {
                    statusLabel.setText("✓");
                    statusLabel.setForeground(UITheme.SUCCESS);
                } else {
                    statusLabel.setText("✕");
                    statusLabel.setForeground(UITheme.ERROR);
                }
            }
        });
    }
    
    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate(String text);
    }
    
    private boolean validateDni(String dni) {
        return dni.length() == 8 && dni.matches("\\d+");
    }
    
    private boolean validateNotEmpty(String text) {
        return !text.trim().isEmpty();
    }
    
    private boolean validateEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    private boolean validatePassword(String password) {
        return password.length() >= 4;
    }
    
    private boolean validateConfirmPassword(String confirm) {
        String password = new String(txtPassword.getPassword());
        return !confirm.isEmpty() && confirm.equals(password);
    }

    private void register() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Complete todos los campos obligatorios");
            return;
        }
        
        if (dni.length() != 8 || !dni.matches("\\d+")) {
            showError("El DNI debe tener exactamente 8 dígitos");
            return;
        }
        
        if (!email.contains("@")) {
            showError("Ingrese un email válido");
            return;
        }
        
        if (password.length() < 4) {
            showError("La contraseña debe tener al menos 4 caracteres");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            return;
        }
        
        User existingUser = userDAO.obtenerPorDni(dni);
        if (existingUser != null) {
            showError("Ya existe un usuario registrado con este DNI");
            return;
        }
        
        User newUser = new User();
        newUser.setDni(dni);
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setEmail(email);
        newUser.setTelefono(telefono);
        
        if (userDAO.registrar(newUser, password)) {
            String numeroCuenta = generarNumeroCuenta();
            
            Account nuevaCuenta = new Account();
            nuevaCuenta.setUsuarioId(newUser.getId());
            nuevaCuenta.setNumeroCuenta(numeroCuenta);
            nuevaCuenta.setTipoCuenta(TipoCuenta.AHORROS);
            nuevaCuenta.setSaldo(BigDecimal.ZERO);
            nuevaCuenta.setMoneda(Moneda.PEN);
            
            if (accountDAO.crearCuenta(nuevaCuenta)) {
                showSuccess("¡Registro exitoso!\n\nUsuario: " + newUser.getNombreCompleto() + 
                            "\nCuenta: " + numeroCuenta + "\nTipo: Ahorros en Soles");
                
                List<Account> cuentas = accountDAO.obtenerCuentasPorUsuario(newUser.getId());
                new DashboardFrame(newUser, cuentas); 
                dispose();
            } else {
                showWarning("Usuario creado pero error al crear cuenta.\nContacte al administrador.");
            }
        } else {
            showError("Error al registrar usuario");
        }
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        String banco = "191";
        String cuenta = String.format("%08d", random.nextInt(100000000));
        String verificador = String.valueOf(random.nextInt(10));
        String tipo = "01";
        return banco + "-" + cuenta + "-" + verificador + "-" + tipo;
    }

    private void cancel() {
        loginFrame.setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}