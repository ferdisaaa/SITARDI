/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.sitardi.CustomComponents;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author ASUS
 */
public class ComboBoxCustom<E> extends JComboBox<E> {

    private Color borderColor = new Color(200, 200, 200);
    private Color arrowColor = new Color(150, 150, 150);

    public ComboBoxCustom() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // MENGHILANGKAN GARIS ABU-ABU (FOCUS BORDER)
        setFocusable(false);
        setRequestFocusEnabled(false);
        
        updateBorder();
        setUI(new CustomComboBoxUI());
    }

    private void updateBorder() {
        // Border luar kustom dengan padding internal agar teks tidak menempel
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                new EmptyBorder(5, 10, 5, 5)
        ));
    }

    // Getter & Setter agar bisa diubah via Properties NetBeans
    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { 
        this.borderColor = borderColor; 
        updateBorder();
        repaint(); 
    }

    public Color getArrowColor() { return arrowColor; }
    public void setArrowColor(Color arrowColor) { 
        this.arrowColor = arrowColor; 
        repaint(); 
    }

    private class CustomComboBoxUI extends BasicComboBoxUI {
        
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton();
            button.setContentAreaFilled(false);
            button.setBorder(null);
            button.setFocusable(false);
            button.setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(arrowColor);
                    
                    // Menggambar chevron tipis (V)
                    int[] xPoints = {0, 5, 10};
                    int[] yPoints = {0, 5, 0};
                    g2.translate(x, y + 7);
                    g2.setStroke(new BasicStroke(1.3f));
                    g2.drawPolyline(xPoints, yPoints, 3);
                    g2.dispose();
                }
                @Override public int getIconWidth() { return 12; }
                @Override public int getIconHeight() { return 12; }
            });
            return button;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Gambar Background Solid (Tanpa Gradasi Metalik)
            g2.setColor(c.getBackground());
            g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            
            g2.dispose();
            super.paint(g, c);
        }

        protected void paintFocus(Graphics g, Rectangle bounds, Dimension size) {
            // DIKOSONGKAN: Mencegah gambar kotak abu-abu saat dipilih (Masalah image_63a81b.png)
        }

        @Override
        protected ListCellRenderer createRenderer() {
            return new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBorder(new EmptyBorder(8, 10, 8, 10)); // Padding dropdown items
                    
                    if (isSelected) {
                        setBackground(new Color(245, 245, 245)); // Warna hover item
                        setForeground(ComboBoxCustom.this.getForeground());
                    } else {
                        setBackground(Color.WHITE);
                        setForeground(ComboBoxCustom.this.getForeground());
                    }
                    return this;
                }
            };
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = (BasicComboPopup) super.createPopup();
            // Menyesuaikan border dropdown list dengan warna border utama
            popup.setBorder(BorderFactory.createLineBorder(borderColor));
            return popup;
        }
    }
}