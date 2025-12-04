package com.bancamovil.ui;

import com.bancamovil.dao.UserDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import static com.bancamovil.ui.UITheme.*;

public class ResetPasswordDialog extends JDialog {

    private int userId;
    private UserDAO userDAO = new UserDAO();

    private JPasswordField txtNew;
    private JPasswordField txtConfirm;
    private boolean updated = false;
    private JLabel strengthLabel;
    private JPanel strengthBar;

    public ResetPasswordDialog(Window owner, int userId) {
        super(owner, "Restablecer contraseña", ModalityType.APPLICATION_MODAL);
        this.userId = userId;
        setResizable(false);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(25, 25, 25, 25));

        RoundedPanel card = new RoundedPanel(24, BG_CARD, true);
        card.setBorder(new EmptyBorder(35, 35, 35, 35));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 450));

        // Header con icono
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Icono de llave
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 70;
                int x = (getWidth() - size) / 2;
                int y = 0;
                
                // Glow
                for (int i = 8; i > 0; i--) {
                    g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 10 - i));
                    g2.fillOval(x - i, y - i, size + i*2, size + i*2);
                }
                
                // Gradiente
                GradientPaint gradient = new GradientPaint(x, y, PRIMARY, x + size, y + size, SECONDARY);
                g2.setPaint(gradient);
                g2.fillOval(x, y, size, size);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(100, 75));
        iconPanel.setMaximumSize(new Dimension(100, 75));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel keyIcon = new JLabel("🔑");
        keyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconPanel.add(keyIcon);
        
        JLabel title = new JLabel("Nueva Contraseña");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Crea una contraseña segura");
        subtitle.setFont(FONT_BODY);
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconPanel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        headerPanel.add(subtitle);
        
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Campos
        txtNew = new RoundedPasswordField();
        txtNew.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        txtNew.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength(new String(txtNew.getPassword()));
            }
        });
        
        txtConfirm = new RoundedPasswordField();
        txtConfirm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Panel de fortaleza
        JPanel strengthPanel = createStrengthPanel();

        card.add(createInputPanel("🔒 Nueva contraseña", txtNew));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(strengthPanel);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(createInputPanel("🔐 Confirmar contraseña", txtConfirm));
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botones
        JButton btnChange = UITheme.createPrimaryButton("Cambiar Contraseña", "✓");
        btnChange.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnChange.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnChange.addActionListener(e -> cambiar());
        
        JButton btnCancel = UITheme.createSecondaryButton("Cancelar");
        btnCancel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCancel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnCancel.addActionListener(e -> dispose());

        card.add(btnChange);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(btnCancel);

        content.add(card, BorderLayout.CENTER);
        add(content);
    }
    
    private JPanel createStrengthPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        // Barra de fortaleza
        strengthBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 100));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        strengthBar.setOpaque(false);
        strengthBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 6));
        strengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        strengthBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Label de fortaleza
        strengthLabel = new JLabel("Fortaleza: --");
        strengthLabel.setFont(FONT_CAPTION);
        strengthLabel.setForeground(TEXT_MUTED);
        strengthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(strengthBar);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(strengthLabel);
        
        return panel;
    }
    
    private void updatePasswordStrength(String password) {
        int strength = calculateStrength(password);
        String text;
        Color color;
        
        if (password.isEmpty()) {
            text = "Fortaleza: --";
            color = UITheme.TEXT_MUTED;
            strength = 0;
        } else if (strength < 2) {
            text = "Fortaleza: Débil";
            color = UITheme.ERROR;
        } else if (strength < 4) {
            text = "Fortaleza: Media";
            color = UITheme.WARNING;
        } else {
            text = "Fortaleza: Fuerte";
            color = UITheme.SUCCESS;
        }
        
        strengthLabel.setText(text);
        strengthLabel.setForeground(color);
        
        final int finalStrength = strength;
        final Color finalColor = color;
        
        strengthBar.removeAll();
        JPanel bar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 100));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                
                // Barra de progreso
                int fillWidth = (int)(getWidth() * (finalStrength / 5.0));
                if (fillWidth > 0) {
                    g2.setColor(finalColor);
                    g2.fillRoundRect(0, 0, fillWidth, getHeight(), 6, 6);
                }
                
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 6));
        strengthBar.setLayout(new BorderLayout());
        strengthBar.add(bar, BorderLayout.CENTER);
        strengthBar.revalidate();
        strengthBar.repaint();
    }
    
    private int calculateStrength(String password) {
        int strength = 0;
        if (password.length() >= 6) strength++;
        if (password.length() >= 10) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        return strength;
    }

    private void cambiar() {
        String p1 = new String(txtNew.getPassword());
        String p2 = new String(txtConfirm.getPassword());

        if (p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (p1.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo 6 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDAO.actualizarPassword(userId, p1)) {
            updated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error de conexión al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isPasswordUpdated() {
        return updated;
    }

    private JPanel createInputPanel(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_BODY_BOLD);
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(field);

        return panel;
    }
}
