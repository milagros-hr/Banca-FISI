package com.bancamovil.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class UITheme {
    
    // ═══════════════════════════════════════════════════════════════
    // 🎨 PALETA DE COLORES MODERNA - GLASSMORPHISM + GRADIENTES
    // ═══════════════════════════════════════════════════════════════
    
    // Colores principales - Tema moderno azul/morado con glassmorphism
    public static final Color PRIMARY = new Color(99, 102, 241);        // Indigo vibrante
    public static final Color PRIMARY_DARK = new Color(79, 70, 229);    // Indigo oscuro
    public static final Color PRIMARY_LIGHT = new Color(129, 140, 248); // Indigo claro
    public static final Color SECONDARY = new Color(139, 92, 246);      // Violeta
    public static final Color ACCENT = new Color(34, 197, 94);          // Verde éxito
    public static final Color ACCENT_CYAN = new Color(6, 182, 212);     // Cyan moderno
    public static final Color ACCENT_PINK = new Color(236, 72, 153);    // Rosa vibrante
    public static final Color ACCENT_ORANGE = new Color(249, 115, 22);  // Naranja cálido
    
    // Fondos con glassmorphism
    public static final Color BG_DARK = new Color(15, 23, 42);           // Fondo principal oscuro
    public static final Color BG_DARKER = new Color(10, 15, 30);         // Fondo más oscuro
    public static final Color BG_CARD = new Color(30, 41, 59);           // Fondo de tarjetas
    public static final Color BG_CARD_HOVER = new Color(40, 52, 72);     // Card hover
    public static final Color BG_LIGHT = new Color(241, 245, 249);       // Fondo claro
    public static final Color BG_WHITE = new Color(255, 255, 255);       // Blanco puro
    public static final Color BG_GLASS = new Color(255, 255, 255, 10);   // Efecto glass
    
    // Textos
    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);   // Texto principal claro
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Texto secundario
    public static final Color TEXT_DARK = new Color(30, 41, 59);         // Texto oscuro
    public static final Color TEXT_MUTED = new Color(100, 116, 139);     // Texto apagado
    public static final Color TEXT_ACCENT = new Color(165, 180, 252);    // Texto con acento
    
    // Bordes y separadores
    public static final Color BORDER = new Color(51, 65, 85);
    public static final Color BORDER_LIGHT = new Color(226, 232, 240);
    public static final Color BORDER_GLOW = new Color(99, 102, 241, 50); // Borde con glow
    
    // Estados
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color SUCCESS_LIGHT = new Color(34, 197, 94, 30);
    public static final Color ERROR = new Color(239, 68, 68);
    public static final Color ERROR_LIGHT = new Color(239, 68, 68, 30);
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color WARNING_LIGHT = new Color(245, 158, 11, 30);
    public static final Color INFO = new Color(59, 130, 246);
    public static final Color INFO_LIGHT = new Color(59, 130, 246, 30);
    
    // Hover y efectos
    public static final Color HOVER_DARK = new Color(51, 65, 85);
    public static final Color HOVER_LIGHT = new Color(241, 245, 249);
    public static final Color SHADOW = new Color(0, 0, 0, 40);
    public static final Color GLOW = new Color(99, 102, 241, 60);
    
    // ═══════════════════════════════════════════════════════════════
    // 🔤 FUENTES MODERNAS
    // ═══════════════════════════════════════════════════════════════
    
    public static final Font FONT_TITLE_LARGE = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONEY = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_MONEY_SMALL = new Font("Segoe UI", Font.BOLD, 20);
    
    // ═══════════════════════════════════════════════════════════════
    // 🖼️ ICONOS UNICODE MODERNOS
    // ═══════════════════════════════════════════════════════════════
    
    public static final String ICON_HOME = "🏠";
    public static final String ICON_TRANSFER = "↔️";
    public static final String ICON_PAY = "💳";
    public static final String ICON_RECHARGE = "💰";
    public static final String ICON_HISTORY = "📋";
    public static final String ICON_PROFILE = "👤";
    public static final String ICON_CARD = "💳";
    public static final String ICON_BANK = "🏦";
    public static final String ICON_LOGOUT = "🚪";
    public static final String ICON_SETTINGS = "⚙️";
    public static final String ICON_SECURITY = "🔒";
    public static final String ICON_SUCCESS = "✓";
    public static final String ICON_ERROR = "✕";
    public static final String ICON_WARNING = "⚠️";
    public static final String ICON_INFO = "ℹ️";
    public static final String ICON_ARROW_RIGHT = "→";
    public static final String ICON_ARROW_LEFT = "←";
    public static final String ICON_PLUS = "+";
    public static final String ICON_MINUS = "-";
    public static final String ICON_STAR = "★";
    public static final String ICON_CHART = "📊";
    public static final String ICON_NOTIFICATION = "🔔";
    
    // ═══════════════════════════════════════════════════════════════
    // 🎴 PANEL REDONDEADO CON SOMBRA Y GLASSMORPHISM
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private boolean hasShadow;
        private boolean hasGlow;
        private Color glowColor;
        private int shadowOffset = 4;
        
        public RoundedPanel(int radius, Color bgColor) {
            this(radius, bgColor, false, false);
        }
        
        public RoundedPanel(int radius, Color bgColor, boolean hasShadow) {
            this(radius, bgColor, hasShadow, false);
        }
        
        public RoundedPanel(int radius, Color bgColor, boolean hasShadow, boolean hasGlow) {
            super();
            this.radius = radius;
            this.bgColor = bgColor;
            this.hasShadow = hasShadow;
            this.hasGlow = hasGlow;
            this.glowColor = GLOW;
            setOpaque(false);
        }
        
        public void setGlowColor(Color color) {
            this.glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 60);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int x = hasShadow ? 0 : 0;
            int y = hasShadow ? 0 : 0;
            int w = hasShadow ? getWidth() - shadowOffset : getWidth();
            int h = hasShadow ? getHeight() - shadowOffset : getHeight();
            
            // Sombra
            if (hasShadow) {
                for (int i = shadowOffset; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 15 - i * 3));
                    g2.fillRoundRect(i, i, w, h, radius, radius);
                }
            }
            
            // Glow effect
            if (hasGlow) {
                g2.setColor(glowColor);
                g2.fillRoundRect(x - 2, y - 2, w + 4, h + 4, radius + 2, radius + 2);
            }
            
            // Fondo principal
            g2.setColor(bgColor);
            g2.fillRoundRect(x, y, w, h, radius, radius);
            
            // Efecto glassmorphism - línea superior brillante
            GradientPaint glassEffect = new GradientPaint(
                0, 0, new Color(255, 255, 255, 15),
                0, h/3, new Color(255, 255, 255, 0)
            );
            g2.setPaint(glassEffect);
            g2.fillRoundRect(x, y, w, h/3, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
        }
        
        public void setBackgroundColor(Color color) {
            this.bgColor = color;
            repaint();
        }
        
        public void setShadow(boolean shadow) {
            this.hasShadow = shadow;
            repaint();
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔘 BOTÓN PRIMARIO CON GRADIENTE Y ANIMACIÓN
    // ═══════════════════════════════════════════════════════════════
    
    public static JButton createPrimaryButton(String text) {
        return createPrimaryButton(text, null);
    }
    
    public static JButton createPrimaryButton(String text, String icon) {
        String displayText = icon != null ? icon + "  " + text : text;
        
        JButton btn = new JButton(displayText) {
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
                
                int w = getWidth();
                int h = getHeight();
                
                // Sombra con glow animado
                int glowSize = (int)(4 + hoverProgress * 4);
                for (int i = glowSize; i > 0; i--) {
                    int alpha = (int)((20 + hoverProgress * 30) - i * 5);
                    g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), Math.max(0, alpha)));
                    g2.fillRoundRect(-i, -i, w + i*2, h + i*2, 14 + i, 14 + i);
                }
                
                // Gradiente animado
                Color startColor = PRIMARY;
                Color endColor = SECONDARY;
                if (getModel().isPressed()) {
                    startColor = PRIMARY_DARK;
                    endColor = new Color(110, 70, 200);
                } else {
                    // Interpolación de colores en hover
                    startColor = interpolateColor(PRIMARY, PRIMARY_LIGHT, hoverProgress * 0.3f);
                }
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, startColor, 
                    w, h, endColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                
                // Efecto brillo superior
                GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40 + (int)(hoverProgress * 20)),
                    0, h/2, new Color(255, 255, 255, 0)
                );
                g2.setPaint(shine);
                g2.fillRoundRect(0, 0, w, h/2, 14, 14);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        styleButton(btn, Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 48));
        return btn;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔘 BOTÓN SECUNDARIO
    // ═══════════════════════════════════════════════════════════════
    
    public static JButton createSecondaryButton(String text) {
        return createSecondaryButton(text, null);
    }
    
    public static JButton createSecondaryButton(String text, String icon) {
        String displayText = icon != null ? icon + "  " + text : text;
        
        JButton btn = new JButton(displayText) {
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
                
                Color bgColor = interpolateColor(BG_CARD, HOVER_DARK, hoverProgress);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                // Borde
                Color borderColor = interpolateColor(BORDER, PRIMARY, hoverProgress * 0.5f);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        styleButton(btn, TEXT_SECONDARY);
        btn.setPreferredSize(new Dimension(140, 48));
        return btn;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔘 BOTÓN DE MENÚ LATERAL CON INDICADOR ACTIVO
    // ═══════════════════════════════════════════════════════════════
    
    public static JButton createMenuButton(String text) {
        return createMenuButton(text, null, false);
    }
    
    public static JButton createMenuButton(String text, String icon) {
        return createMenuButton(text, icon, false);
    }
    
    public static JButton createMenuButton(String text, String icon, boolean isActive) {
        String displayText = icon != null ? icon + "   " + text : text;
        
        JButton btn = new JButton(displayText) {
            private float hoverProgress = 0f;
            private Timer hoverTimer;
            private boolean hovering = false;
            private boolean active = isActive;
            
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
            
            public void setActive(boolean active) {
                this.active = active;
                repaint();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (active || hoverProgress > 0) {
                    float progress = active ? 1f : hoverProgress;
                    
                    // Fondo con gradiente sutil
                    Color bgColor = new Color(
                        PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(),
                        (int)(progress * 25)
                    );
                    g2.setColor(bgColor);
                    g2.fillRoundRect(4, 0, getWidth() - 8, getHeight(), 12, 12);
                    
                    // Indicador lateral
                    g2.setColor(PRIMARY);
                    int indicatorHeight = (int)(getHeight() * 0.6 * progress);
                    int indicatorY = (getHeight() - indicatorHeight) / 2;
                    g2.fillRoundRect(0, indicatorY, 4, indicatorHeight, 2, 2);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(FONT_BODY);
        btn.setForeground(isActive ? TEXT_PRIMARY : TEXT_SECONDARY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(14, 20, 14, 16));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 52));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return btn;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔘 BOTÓN DE ACCIÓN RÁPIDA (COLOREADO)
    // ═══════════════════════════════════════════════════════════════
    
    public static JButton createActionButton(String text, Color bgColor) {
        return createActionButton(text, bgColor, null);
    }
    
    public static JButton createActionButton(String text, Color bgColor, String icon) {
        String displayText = icon != null ? icon + "  " + text : text;
        
        JButton btn = new JButton(displayText) {
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
                
                // Sombra
                int shadowSize = (int)(2 + hoverProgress * 2);
                for (int i = shadowSize; i > 0; i--) {
                    g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 20 - i * 4));
                    g2.fillRoundRect(-i, -i + 2, getWidth() + i*2, getHeight() + i*2, 14, 14);
                }
                
                Color currentColor;
                if (getModel().isPressed()) {
                    currentColor = bgColor.darker();
                } else {
                    currentColor = interpolateColor(bgColor, bgColor.brighter(), hoverProgress * 0.2f);
                }
                
                g2.setColor(currentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                // Brillo
                GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 30),
                    0, getHeight()/2, new Color(255, 255, 255, 0)
                );
                g2.setPaint(shine);
                g2.fillRoundRect(0, 0, getWidth(), getHeight()/2, 14, 14);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        styleButton(btn, Color.WHITE);
        return btn;
    }
    
    private static void styleButton(JButton btn, Color textColor) {
        btn.setFont(FONT_BUTTON);
        btn.setForeground(textColor);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📝 CAMPO DE TEXTO MODERNO CON FOCUS ANIMATION
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedTextField extends JTextField {
        private int radius = 14;
        private float focusProgress = 0f;
        private Timer focusTimer;
        private boolean isFocused = false;
        private String placeholder = "";
        
        public RoundedTextField() {
            super();
            setOpaque(false);
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setCaretColor(PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
            
            focusTimer = new Timer(16, e -> {
                if (isFocused && focusProgress < 1f) {
                    focusProgress = Math.min(1f, focusProgress + 0.12f);
                    repaint();
                } else if (!isFocused && focusProgress > 0f) {
                    focusProgress = Math.max(0f, focusProgress - 0.12f);
                    repaint();
                } else {
                    focusTimer.stop();
                }
            });
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    focusTimer.start();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    focusTimer.start();
                }
            });
        }
        
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Glow en focus
            if (focusProgress > 0) {
                int glowSize = (int)(focusProgress * 4);
                g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), (int)(focusProgress * 30)));
                g2.fillRoundRect(-glowSize, -glowSize, getWidth() + glowSize*2, getHeight() + glowSize*2, radius + glowSize, radius + glowSize);
            }
            
            // Fondo
            g2.setColor(BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            
            // Borde animado
            Color borderColor = interpolateColor(BORDER, PRIMARY, focusProgress);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f + focusProgress * 0.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
            
            // Placeholder
            if (getText().isEmpty() && !isFocused && !placeholder.isEmpty()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setColor(TEXT_MUTED);
                g3.setFont(getFont());
                g3.drawString(placeholder, getInsets().left, getHeight()/2 + g3.getFontMetrics().getAscent()/2 - 2);
                g3.dispose();
            }
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔐 CAMPO DE CONTRASEÑA MODERNO
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedPasswordField extends JPasswordField {
        private int radius = 14;
        private float focusProgress = 0f;
        private Timer focusTimer;
        private boolean isFocused = false;
        
        public RoundedPasswordField() {
            super();
            setOpaque(false);
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setCaretColor(PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
            
            focusTimer = new Timer(16, e -> {
                if (isFocused && focusProgress < 1f) {
                    focusProgress = Math.min(1f, focusProgress + 0.12f);
                    repaint();
                } else if (!isFocused && focusProgress > 0f) {
                    focusProgress = Math.max(0f, focusProgress - 0.12f);
                    repaint();
                } else {
                    focusTimer.stop();
                }
            });
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    focusTimer.start();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    focusTimer.start();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (focusProgress > 0) {
                int glowSize = (int)(focusProgress * 4);
                g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), (int)(focusProgress * 30)));
                g2.fillRoundRect(-glowSize, -glowSize, getWidth() + glowSize*2, getHeight() + glowSize*2, radius + glowSize, radius + glowSize);
            }
            
            g2.setColor(BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            
            Color borderColor = interpolateColor(BORDER, PRIMARY, focusProgress);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f + focusProgress * 0.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📊 TARJETA DE BALANCE MODERNA
    // ═══════════════════════════════════════════════════════════════
    
    public static RoundedPanel createBalanceCard() {
        RoundedPanel card = new RoundedPanel(24, BG_CARD, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));
        return card;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 💳 TARJETA DE MOVIMIENTO
    // ═══════════════════════════════════════════════════════════════
    
    public static RoundedPanel createMovementCard(boolean isDebit) {
        RoundedPanel card = new RoundedPanel(16, BG_WHITE, true);
        card.setBorder(new EmptyBorder(18, 22, 18, 22));
        return card;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🏷️ BADGE / ETIQUETA
    // ═══════════════════════════════════════════════════════════════
    
    public static JLabel createBadge(String text, Color bgColor) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(FONT_SMALL_BOLD);
        badge.setForeground(bgColor);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(new EmptyBorder(4, 12, 4, 12));
        return badge;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📊 TABLA ESTILIZADA
    // ═══════════════════════════════════════════════════════════════
    
    public static JScrollPane createStyledTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(50);
        table.setGridColor(new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 50));
        table.setSelectionBackground(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 40));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_DARK);
        header.setForeground(TEXT_SECONDARY);
        header.setFont(FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(BG_DARK);
                label.setForeground(TEXT_SECONDARY);
                label.setBorder(new EmptyBorder(0, 14, 0, 14));
                label.setFont(FONT_BODY_BOLD);
                return label;
            }
        };
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : new Color(37, 50, 69));
                }
                c.setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                return c;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JScrollPane scrollPane = new RoundedScrollPane(table);
        return scrollPane;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📜 SCROLLPANE REDONDEADO
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedScrollPane extends JScrollPane {
        private int radius = 16;
        
        public RoundedScrollPane(Component view) {
            super(view);
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            
            // Estilizar scrollbar
            getVerticalScrollBar().setUI(new ModernScrollBarUI());
            getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BG_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📌 SCROLLBAR MODERNO
    // ═══════════════════════════════════════════════════════════════
    
    public static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(100, 116, 139);
            this.trackColor = BG_DARK;
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                           thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📦 COMBOBOX REDONDEADO
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedComboBox<E> extends JComboBox<E> {
        private int radius = 14;
        
        public RoundedComboBox() {
            super();
            setupStyle();
        }
        
        public RoundedComboBox(E[] items) {
            super(items);
            setupStyle();
        }
        
        private void setupStyle() {
            setOpaque(false);
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setBackground(BG_DARK);
            setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            
            setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton() {
                        @Override
                        public void paint(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(BG_DARK);
                            g2.fillRect(0, 0, getWidth(), getHeight());
                            g2.setColor(TEXT_SECONDARY);
                            int w = getWidth();
                            int h = getHeight();
                            int[] xPoints = {w/2 - 5, w/2 + 5, w/2};
                            int[] yPoints = {h/2 - 2, h/2 - 2, h/2 + 4};
                            g2.fillPolygon(xPoints, yPoints, 3);
                            g2.dispose();
                        }
                    };
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setPreferredSize(new Dimension(30, 30));
                    return btn;
                }
            });
            
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    lbl.setFont(FONT_BODY);
                    lbl.setBorder(new EmptyBorder(10, 14, 10, 14));
                    lbl.setOpaque(true);
                    if (isSelected) {
                        lbl.setBackground(PRIMARY);
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(BG_CARD);
                        lbl.setForeground(TEXT_PRIMARY);
                    }
                    return lbl;
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📝 TEXTAREA REDONDEADO
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedTextArea extends JTextArea {
        private int radius = 14;
        
        public RoundedTextArea() {
            super();
            setOpaque(false);
            setFont(FONT_BODY);
            setForeground(TEXT_PRIMARY);
            setCaretColor(PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
            setLineWrap(true);
            setWrapStyleWord(true);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BG_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔲 BORDE REDONDEADO REUTILIZABLE
    // ═══════════════════════════════════════════════════════════════
    
    public static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness;
        
        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2 + 8, radius/2 + 14, radius/2 + 8, radius/2 + 14);
        }
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🛠️ MÉTODOS UTILITARIOS
    // ═══════════════════════════════════════════════════════════════
    
    public static JTextField createTextField() {
        return new RoundedTextField();
    }
    
    public static JTextField createRoundedTextField() {
        return new RoundedTextField();
    }
    
    public static JPasswordField createPasswordField() {
        return new RoundedPasswordField();
    }
    
    public static JPasswordField createRoundedPasswordField() {
        return new RoundedPasswordField();
    }
    
    private static Color interpolateColor(Color c1, Color c2, float progress) {
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * progress);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * progress);
        int a = (int)(c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * progress);
        return new Color(r, g, b, a);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🎯 SEPARADOR MODERNO
    // ═══════════════════════════════════════════════════════════════
    
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gradient = new GradientPaint(
                    0, h/2, new Color(BORDER.getRed(), BORDER.getGreen(), BORDER.getBlue(), 0),
                    w/2, h/2, BORDER,
                    true
                );
                g2.setPaint(gradient);
                g2.fillRect(0, h/2 - 1, w, 1);
                
                g2.dispose();
            }
        };
        sep.setPreferredSize(new Dimension(1, 1));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 📍 AVATAR / ICONO DE USUARIO
    // ═══════════════════════════════════════════════════════════════
    
    public static JPanel createAvatar(String initials, int size, Color bgColor) {
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Fondo circular con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor.brighter(),
                    size, size, bgColor.darker()
                );
                g2.setPaint(gradient);
                g2.fillOval(0, 0, size, size);
                
                // Texto
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (size - fm.stringWidth(initials)) / 2;
                int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, textX, textY);
                
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setOpaque(false);
        return avatar;
    }
    
    // ═══════════════════════════════════════════════════════════════
    // 🔔 NOTIFICACIÓN / TOAST
    // ═══════════════════════════════════════════════════════════════
    
    public static void showToast(Component parent, String message, Color color) {
        JWindow toast = new JWindow();
        toast.setBackground(new Color(0, 0, 0, 0));
        
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(16, 24, 16, 24));
        
        JLabel label = new JLabel(message);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
        content.add(label);
        
        toast.setContentPane(content);
        toast.pack();
        
        Point parentLocation = parent.getLocationOnScreen();
        int x = parentLocation.x + (parent.getWidth() - toast.getWidth()) / 2;
        int y = parentLocation.y + parent.getHeight() - toast.getHeight() - 30;
        toast.setLocation(x, y);
        
        toast.setVisible(true);
        
        Timer timer = new Timer(3000, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }
}
