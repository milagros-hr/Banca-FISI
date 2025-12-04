package com.bancamovil.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.UserDAO;
import com.bancamovil.models.User;

public class RecoverPasswordFrame extends JFrame {

    private UserDAO userDAO = new UserDAO();
    private JTextField txtDni;
    private JTextField txtEmail;

    public RecoverPasswordFrame() {
        setTitle("Recuperar Contraseña - Banca Digital");
        setSize(500, 750); // Un poquitín más alto para que sobre espacio
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.BG_DARK);
        
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(24, UITheme.BG_CARD, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        // Aumenté el borde superior a 50 para darle más aire desde el principio
        card.setBorder(new EmptyBorder(50, 40, 40, 40));
        
        card.setPreferredSize(new Dimension(420, 650));
        card.setMinimumSize(new Dimension(420, 650));

        // --- Agregar componentes ---

        // 1. EL TRUCO: Un empujón invisible antes de poner el ícono
        card.add(Box.createRigidArea(new Dimension(0, 10))); 

        card.add(createIconPanel());
        
        JLabel title = new JLabel("Recuperar Contraseña");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitle = new JLabel("Verifica tu identidad para continuar");
        subtitle.setFont(UITheme.FONT_BODY);
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel infoPanel = createInfoPanel();
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(infoPanel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        txtDni = UITheme.createRoundedTextField();
        txtEmail = UITheme.createRoundedTextField();

        JPanel dniPanel = createInputPanel("DNI registrado", txtDni);
        dniPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(dniPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel emailPanel = createInputPanel("Correo electrónico", txtEmail);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailPanel);
        
        card.add(Box.createVerticalGlue()); 
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnValidate = UITheme.createPrimaryButton("Validar datos", null);
        btnValidate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValidate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnValidate.setPreferredSize(new Dimension(300, 55));
        btnValidate.addActionListener(e -> validarDatos());
        card.add(btnValidate);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnCancel = UITheme.createSecondaryButton("Cancelar", null);
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnCancel.setPreferredSize(new Dimension(300, 55));
        btnCancel.addActionListener(e -> dispose());
        card.add(btnCancel);

        content.add(card);
        add(content);
    }

    private JPanel createIconPanel() {
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 70;
                int x = (getWidth() - size) / 2;
                
               
                int y = 40; 

                GradientPaint gradient = new GradientPaint(
                        x, y, UITheme.PRIMARY,
                        x + size, y + size, UITheme.SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillOval(x, y, size, size);

                // Dibujar el candado
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(3));
                int lockX = x + size/2 - 10;
                int lockY = y + size/2 - 8;
                g2.drawRoundRect(lockX, lockY + 8, 20, 16, 4, 4);
                g2.drawArc(lockX + 3, lockY, 14, 16, 0, 180);

                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        // 3. TAMAÑO: Panel mucho más alto (140) para que quepa el círculo bajado
        iconPanel.setPreferredSize(new Dimension(100, 140)); 
        iconPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return iconPanel;
    }

    private JPanel createInfoPanel() {
        UITheme.RoundedPanel panel = new UITheme.RoundedPanel(12, new Color(UITheme.INFO.getRed(), UITheme.INFO.getGreen(), UITheme.INFO.getBlue(), 20));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel infoText = new JLabel("<html><center>Ingresa tu DNI y correo electrónico registrados para restablecer tu contraseña.</center></html>");
        infoText.setFont(UITheme.FONT_SMALL);
        infoText.setForeground(UITheme.TEXT_SECONDARY);
        infoText.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(infoText, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputPanel(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setPreferredSize(new Dimension(300, 50));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(field);
        return p;
    }

    private void validarDatos() {
        String dni = txtDni.getText().trim();
        String email = txtEmail.getText().trim();

        if (dni.isEmpty() || email.isEmpty()) {
            showError("Complete todos los campos.");
            return;
        }

        if (!dni.matches("\\d{8}")) {
            showError("El DNI debe tener 8 dígitos numéricos.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Ingrese un email válido.");
            return;
        }

        User u1 = userDAO.obtenerPorDni(dni);
        if (u1 == null) {
            showError("No existe un usuario con ese DNI.");
            return;
        }

        User u2 = userDAO.obtenerPorEmail(email);
        if (u2 == null) {
            showError("No existe un usuario con ese email.");
            return;
        }

        if (u1.getId() != u2.getId()) {
            showError("El DNI y el email no corresponden al mismo usuario.");
            return;
        }

        ResetPasswordDialog dialog = new ResetPasswordDialog(this, u1.getId());
        dialog.setVisible(true);

        if (dialog.isPasswordUpdated()) {
            JOptionPane.showMessageDialog(this,
                    "Contraseña restablecida correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}