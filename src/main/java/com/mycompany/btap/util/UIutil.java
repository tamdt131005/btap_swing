package com.mycompany.btap.util;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.JTextComponent;
public class UIutil {
    public UIutil()
    {

    }

    /**
     * Helper to load an image from resources (or fallback to project resources directory),
     * resize it to a square and return a rounded {@link ImageIcon}.
     *
     * @param contextClass class used to resolve the resource path
     * @param resourcePath path to the image in resources (e.g. "/static/img/user_2.jpg")
     * @param targetSize   target width/height (square) of the resulting icon
     * @return {@link ImageIcon} for the rounded image, or {@code null} if the image cannot be loaded
     */
    public static ImageIcon loadRoundedImage(Class<?> contextClass, String resourcePath, int targetSize) {
        if (contextClass == null || resourcePath == null || resourcePath.isEmpty() || targetSize <= 0) {
            throw new IllegalArgumentException("Invalid arguments to loadRoundedImage");
        }
        
        ImageIcon icon;
        try {
            URL imgURL = contextClass.getResource(resourcePath);
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            } else {
                String normalizedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                icon = new ImageIcon(Paths.get("src", "main", "resources", normalizedPath).toString());
            }
        } catch (Exception e) {
            System.err.println("Không thể load ảnh: " + resourcePath + " - " + e.getMessage());
            return null;
        }
        
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }
        
        BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, targetSize, targetSize);
            g2d.setClip(circle);
            g2d.drawImage(icon.getImage(), 0, 0, targetSize, targetSize, null);
        } finally {
            g2d.dispose();
        }
        
        return new ImageIcon(resizedImage);
    }

    public static void setRadius(JComponent comp, int radius) {
        setRadius(comp, radius, false);
    }

    public static void setRadius(JComponent comp, int radius, boolean transparentBackground) {
        if (comp == null) {
            throw new IllegalArgumentException("Component must not be null");
        }
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be >= 0");
        }

        comp.setOpaque(!transparentBackground);

        if (comp instanceof JButton btn) {
            Color baseBackground = btn.getBackground();
            Color baseForeground = btn.getForeground();
            btn.setUI(new RoundedButtonUI(radius, baseBackground, baseForeground));
            btn.setContentAreaFilled(!transparentBackground);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
        }
        if (comp instanceof JTextComponent textComponent) {
            Color borderColor = new Color(220, 225, 230);
            textComponent.setBorder(BorderFactory.createCompoundBorder(
                    createRoundedBorder(radius, borderColor, 1f),
                    new EmptyBorder(8, 12, 8, 12)
            ));
            textComponent.setOpaque(true);
            if (textComponent.getBackground() == null || textComponent.getBackground().getAlpha() == 0) {
                textComponent.setBackground(Color.WHITE);
            }
        }
    }

    private static final class RoundedButtonUI extends BasicButtonUI {
        private final int radius;
        private final Color background;
        private final Color foreground;
        private final Color hoverColor;
        private final Color pressedColor;
        private final Color disabledColor;

        private RoundedButtonUI(int radius, Color background, Color foreground) {
            this.radius = Math.max(0, radius);
            this.background = background != null ? background : new Color(0, 123, 255);
            this.foreground = foreground != null ? foreground : Color.WHITE;
            this.hoverColor = brighten(this.background, 0.12f);
            this.pressedColor = darken(this.background, 0.12f);
            this.disabledColor = new Color(this.background.getRed(), this.background.getGreen(), this.background.getBlue(), 120);
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            if (c instanceof JButton btn) {
                btn.setOpaque(false);
                btn.setRolloverEnabled(true);
                btn.setForeground(foreground);
                Insets margin = btn.getMargin();
                if (margin == null || margin.top < 10 || margin.bottom < 10) {
                    btn.setMargin(new Insets(12, 20, 12, 20));
                }
                if (!(btn.getBorder() instanceof EmptyBorder)) {
                    btn.setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            if (!(c instanceof JButton btn)) {
                super.paint(g, c);
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = c.getWidth();
                int height = c.getHeight();

                Color fill;
                if (!c.isEnabled()) {
                    fill = disabledColor;
                } else if (btn.getModel().isPressed()) {
                    fill = pressedColor;
                } else if (btn.getModel().isRollover()) {
                    fill = hoverColor;
                } else {
                    fill = background;
                }

                g2.setColor(fill);
                g2.fillRoundRect(0, 0, width, height, radius * 2, radius * 2);

                g2.setColor(new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 180));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, width - 1, height - 1, radius * 2, radius * 2);
            } finally {
                g2.dispose();
            }

            c.setForeground(foreground);
            super.paint(g, c);
        }

        private static Color brighten(Color color, float fraction) {
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();

            int i = (int) (1.0 / (1.0 - fraction));
            if (r == 0 && g == 0 && b == 0) {
                return new Color(i, i, i);
            }
            if (r > 0 && r < i) r = i;
            if (g > 0 && g < i) g = i;
            if (b > 0 && b < i) b = i;

            return new Color(Math.min((int) (r / (1.0 - fraction)), 255),
                    Math.min((int) (g / (1.0 - fraction)), 255),
                    Math.min((int) (b / (1.0 - fraction)), 255));
        }

        private static Color darken(Color color, float fraction) {
            return new Color(Math.max((int) (color.getRed() * (1.0 - fraction)), 0),
                    Math.max((int) (color.getGreen() * (1.0 - fraction)), 0),
                    Math.max((int) (color.getBlue() * (1.0 - fraction)), 0),
                    color.getAlpha());
        }
    }

    public static AbstractBorder createRoundedBorder(int radius, Color borderColor, float strokeWidth) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be >= 0");
        }
        if (borderColor == null) {
            throw new IllegalArgumentException("Border color must not be null");
        }
        if (strokeWidth <= 0f) {
            throw new IllegalArgumentException("Stroke width must be > 0");
        }
        return new RoundedBorder(radius, borderColor, strokeWidth);
    }

    public static final class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final float strokeWidth;
        private final Insets insets;

        private RoundedBorder(int radius, Color color, float strokeWidth) {
            this.radius = radius;
            this.color = color;
            this.strokeWidth = strokeWidth;
            int pad = Math.max(0, radius / 2);
            this.insets = new Insets(pad, pad, pad, pad);
        }

        @Override
        public void paintBorder(java.awt.Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(strokeWidth));

                int arc = Math.max(0, radius * 2);
                int offset = Math.round(strokeWidth / 2f);
                g2.drawRoundRect(x + offset, y + offset, width - offset * 2 - 1, height - offset * 2 - 1, arc, arc);
            } finally {
                g2.dispose();
            }
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(insets.top, insets.left, insets.bottom, insets.right);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c, Insets insets) {
            insets.top = this.insets.top;
            insets.left = this.insets.left;
            insets.bottom = this.insets.bottom;
            insets.right = this.insets.right;
            return insets;
        }
    }
}
