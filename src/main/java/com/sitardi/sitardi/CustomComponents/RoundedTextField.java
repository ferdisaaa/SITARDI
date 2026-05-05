/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.sitardi.CustomComponents;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author ASUS
 */
public class RoundedTextField extends JTextField {

    private int radius = 25;

    public RoundedTextField() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//        setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        setForeground(new Color(50, 50, 50));
        setCaretColor(Color.BLACK);
        setBackground(new Color(0, 0, 0, 0));
        ;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint(); // Memperbarui tampilan secara otomatis saat radius diubah
    }

    public int getRadius() {
        return radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // SEBELUMNYA: g2.setColor(new Color(204, 255, 255)); 
        // SEKARANG: Mengambil warna dari properti Background yang diset di NetBeans
        g2.setColor(getBackground());

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // border (gunakan warna foreground atau warna custom dengan transparansi)
        g2.setColor(new Color(0, 0, 0, 40));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);
    }
}
