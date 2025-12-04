package com.bancamovil.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.UserDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.User;

public class LoginFrame extends JFrame {
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private JTextField txtDni;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private Timer animationTimer;
    private float animationProgress = 0f;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        this.accountDAO = new AccountDAO();
        initComponents();
        startAnimation();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Banca Fisi");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = Math.min(1100, (int)(screenSize.width * 0.75));
        int windowHeight = Math.min(720, (int)(screenSize.height * 0.85));
        
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Panel izquierdo - Branding con animación
        JPanel leftPanel = createBrandingPanel();
        
        // Panel derecho - Login form
        JPanel rightPanel = createLoginPanel();
        
        // Usar JSplitPane para mejor responsividad
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(windowWidth / 2);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Enter para login
        txtPassword.addActionListener(e -> login());
        
        // Responsive
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                splitPane.setDividerLocation(getWidth() / 2);
                revalidate();
                repaint();
            }
        });
    }
    
    private void startAnimation() {
        animationTimer = new Timer(50, e -> {
            animationProgress += 0.02f;
            if (animationProgress > 2 * Math.PI) {
                animationProgress = 0f;
            }
            repaint();
        });
        animationTimer.start();
    }

    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                
                // Fondo con gradiente animado
                GradientPaint gradient = new GradientPaint(
                    0, 0, UITheme.BG_DARK,
                    w, h, new Color(30, 41, 59)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, w, h);
                
                // Círculos decorativos animados
                drawAnimatedCircles(g2, w, h);
                
                // Patrón de puntos
                drawDotPattern(g2, w, h);
                
                g2.dispose();
            }
            
            private void drawAnimatedCircles(Graphics2D g2, int w, int h) {
                // Círculo grande 1
                float offset1 = (float) Math.sin(animationProgress) * 20;
                float offset2 = (float) Math.cos(animationProgress) * 15;
                
                g2.setColor(new Color(99, 102, 241, 30));
                g2.fill(new Ellipse2D.Float(
                    w * 0.6f + offset1, h * 0.1f + offset2, 
                    300, 300
                ));
                
                // Círculo grande 2
                g2.setColor(new Color(139, 92, 246, 25));
                g2.fill(new Ellipse2D.Float(
                    w * 0.1f - offset2, h * 0.5f - offset1, 
                    250, 250
                ));
                
                // Círculo pequeño
                g2.setColor(new Color(6, 182, 212, 35));
                g2.fill(new Ellipse2D.Float(
                    w * 0.7f + offset2, h * 0.7f + offset1, 
                    100, 100
                ));
                
                // Anillo decorativo
                g2.setColor(new Color(99, 102, 241, 40));
                g2.setStroke(new BasicStroke(2));
                g2.draw(new Ellipse2D.Float(
                    w * 0.3f - offset1, h * 0.2f + offset2, 
                    150, 150
                ));
            }
            
            private void drawDotPattern(Graphics2D g2, int w, int h) {
                g2.setColor(new Color(255, 255, 255, 10));
                for (int x = 0; x < w; x += 30) {
                    for (int y = 0; y < h; y += 30) {
                        g2.fillOval(x, y, 2, 2);
                    }
                }
            }
        };
        
        panel.setLayout(new GridBagLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Logo animado
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 100;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new Ellipse2D.Float(x + 4, y + 4, size, size));
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    x, y, UITheme.PRIMARY,
                    x + size, y + size, UITheme.SECONDARY
                );
                g2.setPaint(gradient);
                g2.fill(new Ellipse2D.Float(x, y, size, size));
                
                // Letra B
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 50));
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (size - fm.stringWidth("B")) / 2;
                int textY = y + (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString("B", textX, textY);
                
                g2.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(120, 120));
        logoPanel.setMaximumSize(new Dimension(120, 120));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título
        JLabel titleLabel = new JLabel("Banca Digital");
        titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Tu dinero, siempre contigo");
        subtitleLabel.setFont(UITheme.FONT_SUBTITLE);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Features
        JPanel featuresPanel = new JPanel();
        featuresPanel.setOpaque(false);
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        featuresPanel.setBorder(new EmptyBorder(40, 0, 0, 0));
        
        String[] features = {
            "Seguridad bancaria avanzada",
            "Transferencias instantáneas",
            "Control total de finanzas",
            "Gestión de tarjetas"
        };
        
        for (String feature : features) {
            JPanel featureRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            featureRow.setOpaque(false);
            featureRow.setMaximumSize(new Dimension(250, 35));
            
            JLabel bulletLabel = new JLabel("•");
            bulletLabel.setFont(UITheme.FONT_BODY_BOLD);
            bulletLabel.setForeground(UITheme.PRIMARY);
            
            JLabel textLabel = new JLabel(feature);
            textLabel.setFont(UITheme.FONT_BODY);
            textLabel.setForeground(UITheme.TEXT_SECONDARY);
            
            featureRow.add(bulletLabel);
            featureRow.add(textLabel);
            featuresPanel.add(featureRow);
        }
        
        contentPanel.add(logoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(featuresPanel);
        
        panel.add(contentPanel);
        
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        
        JPanel formContainer = new JPanel();
        formContainer.setOpaque(false);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(new EmptyBorder(40, 60, 40, 60)); 
        formContainer.setPreferredSize(new Dimension(600, 700)); 
        formContainer.setMinimumSize(new Dimension(500, 600));
        
        // Header
        JLabel welcomeLabel = new JLabel("Bienvenido de vuelta");
        welcomeLabel.setFont(UITheme.FONT_TITLE);
        welcomeLabel.setForeground(UITheme.TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Ingresa tus credenciales para continuar");
        instructionLabel.setFont(UITheme.FONT_BODY);
        instructionLabel.setForeground(UITheme.TEXT_MUTED);
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Card de login
        UITheme.RoundedPanel cardPanel = new UITheme.RoundedPanel(24, UITheme.BG_CARD, true);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); 
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 550)); 
        
        // Campo DNI
        JPanel dniPanel = createInputPanel("DNI", "Ingresa tu DNI de 8 dígitos");
        txtDni = (JTextField) ((JPanel)dniPanel.getComponent(2)).getComponent(0);
        
        // Campo Contraseña
        JPanel passPanel = createPasswordPanel("Contraseña", "Ingresa tu contraseña");
        txtPassword = (JPasswordField) ((JPanel)passPanel.getComponent(2)).getComponent(0);
        
        // Botón olvidé contraseña
        JButton btnForgot = new JButton("¿Olvidaste tu contraseña?");
        btnForgot.setBorder(null);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setForeground(UITheme.PRIMARY);
        btnForgot.setFont(UITheme.FONT_SMALL);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnForgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnForgot.setForeground(UITheme.PRIMARY_DARK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnForgot.setForeground(UITheme.PRIMARY);
            }
        });
        btnForgot.addActionListener(e -> new RecoverPasswordFrame());
        
        // Botón Login
        btnLogin = UITheme.createPrimaryButton("Iniciar Sesión", null);
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setPreferredSize(new Dimension(380, 55)); 
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnLogin.addActionListener(e -> login());
        
        // Separador
        JPanel separatorPanel = createSeparator();
        
        // Botón Registro
        JButton btnRegister = UITheme.createSecondaryButton("Crear nueva cuenta", null);
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.setPreferredSize(new Dimension(380, 55));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnRegister.addActionListener(e -> openRegister());
        
        cardPanel.add(dniPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        cardPanel.add(btnForgot);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(btnLogin);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(separatorPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(btnRegister);
        
        // Footer - MODIFICADO: Usamos un panel con FlowLayout center para forzar el centrado visual perfecto
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Alineación en el contenedor padre

        JLabel footerLabel = new JLabel("Conexión segura y encriptada");
        footerLabel.setFont(UITheme.FONT_CAPTION);
        footerLabel.setForeground(UITheme.TEXT_MUTED);
        
        footerPanel.add(footerLabel);
        
        formContainer.add(welcomeLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        formContainer.add(instructionLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        formContainer.add(cardPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        formContainer.add(footerPanel); // Añadimos el panel centrado
        
        panel.add(formContainer);
        
        return panel;
    }
    
    private JPanel createInputPanel(String label, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblField = new JLabel(label);
        lblField.setFont(UITheme.FONT_BODY_BOLD);
        lblField.setForeground(UITheme.TEXT_SECONDARY);
        lblField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        UITheme.RoundedTextField field = new UITheme.RoundedTextField();
        field.setPlaceholder(placeholder);
        field.setPreferredSize(new Dimension(380, 50)); 
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel fieldWrapper = new JPanel(new BorderLayout());
        fieldWrapper.setOpaque(false);
        fieldWrapper.setPreferredSize(new Dimension(380, 50)); 
        fieldWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        fieldWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldWrapper.add(field, BorderLayout.CENTER);
        
        panel.add(lblField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldWrapper);
        
        return panel;
    }
    
    private JPanel createPasswordPanel(String label, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblField = new JLabel(label);
        lblField.setFont(UITheme.FONT_BODY_BOLD);
        lblField.setForeground(UITheme.TEXT_SECONDARY);
        lblField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        UITheme.RoundedPasswordField field = new UITheme.RoundedPasswordField();
        field.setPreferredSize(new Dimension(380, 50)); 
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel fieldWrapper = new JPanel(new BorderLayout());
        fieldWrapper.setOpaque(false);
        fieldWrapper.setPreferredSize(new Dimension(380, 50)); 
        fieldWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        fieldWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldWrapper.add(field, BorderLayout.CENTER);
        
        panel.add(lblField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fieldWrapper);
        
        return panel;
    }

    private JPanel createSeparator() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JSeparator leftLine = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BORDER);
                g2.fillRect(0, getHeight()/2, getWidth(), 1);
                g2.dispose();
            }
        };
        leftLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        JLabel orLabel = new JLabel("  o  ");
        orLabel.setForeground(UITheme.TEXT_MUTED);
        orLabel.setFont(UITheme.FONT_SMALL);
        
        JSeparator rightLine = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BORDER);
                g2.fillRect(0, getHeight()/2, getWidth(), 1);
                g2.dispose();
            }
        };
        rightLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        panel.add(leftLine);
        panel.add(orLabel);
        panel.add(rightLine);
        
        return panel;
    }

    private void login() {
        String dni = txtDni.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (dni.isEmpty() || password.isEmpty()) {
            showError("Complete todos los campos");
            return;
        }

        if (dni.length() != 8 || !dni.matches("\\d+")) {
            showError("El DNI debe tener 8 dígitos numéricos");
            return;
        }

        // Efecto de carga
        btnLogin.setEnabled(false);
        btnLogin.setText("Verificando...");
        
        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() {
                return userDAO.autenticar(dni, password);
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        List<Account> cuentas = accountDAO.obtenerCuentasPorUsuario(user.getId());

                        if (cuentas.isEmpty()) {
                            showWarning("No tienes cuentas asociadas. Contacta con un administrador.");
                            resetLoginButton();
                            return;
                        }

                        showSuccess("¡Bienvenido, " + user.getNombreCompleto() + "!");
                        if (animationTimer != null) {
                            animationTimer.stop();
                        }
                        new DashboardFrame(user, cuentas);
                        dispose();
                    } else {
                        showError("DNI o contraseña incorrectos");
                        txtPassword.setText("");
                        resetLoginButton();
                    }
                } catch (Exception e) {
                    showError("Error de conexión");
                    resetLoginButton();
                }
            }
        };
        worker.execute();
    }
    
    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Iniciar Sesión");
    }

    private void openRegister() {
        new RegisterFrame(this);
        setVisible(false);
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