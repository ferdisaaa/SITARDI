/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.sitardi.CustomComponents;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author ASUS
 */
public class RoundedButton extends JButton {

    private int radius = 25;
    private Color borderColor = new Color(0, 0, 0, 50); // Warna border default
    private int borderThickness = 2; // Ketebalan border

    public RoundedButton() {
        setOpaque(false);
        setContentAreaFilled(false); // Agar background asli tombol tidak digambar
        setFocusPainted(false);      // Menghilangkan kotak fokus saat diklik
        setBorder(new EmptyBorder(10, 20, 10, 20)); // Margin teks
        setCursor(new Cursor(Cursor.HAND_CURSOR));  // Mengubah kursor saat hover
    }

    // Getter dan Setter agar bisa diatur di NetBeans Properties
    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; repaint(); }

    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; repaint(); }

    public int getBorderThickness() { return borderThickness; }
    public void setBorderThickness(int borderThickness) { this.borderThickness = borderThickness; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Efek saat tombol ditekan (sedikit lebih gelap)
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        // Gambar background bulat
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Gambar custom border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRoundRect(borderThickness / 2, borderThickness / 2, 
                         getWidth() - borderThickness, getHeight() - borderThickness, 
                         radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}