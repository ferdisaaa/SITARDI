/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.CustomComponents;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


/**
 *
 * @author ASUS
 */
public class CustomPasswordField extends JPasswordField {

    private int radius = 25;
    private boolean hide = true;
    private Image eyeOpen;
    private Image eyeClose;

    public CustomPasswordField() {
        setOpaque(false);
        // Memberi ruang di kanan (40) untuk ikon mata
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 40));
        setBackground(Color.WHITE);
        setEchoChar('●'); // Karakter default password

        // Load Ikon (Pastikan path gambar benar atau gunakan gambar default)
        // Jika tidak ada gambar, kita akan menggambarnya secara manual di paintComponent
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Deteksi klik pada area ikon (sisi kanan)
                int iconPos = getWidth() - 35;
                if (e.getX() >= iconPos) {
                    togglePassword();
                }
            }
        });
    }

    private void togglePassword() {
        if (hide) {
            setEchoChar((char) 0); // Show password
            hide = false;
        } else {
            setEchoChar('●'); // Hide password
            hide = true;
        }
        repaint();
    }

    // Getter & Setter Radius
    public void setRadius(int radius) { this.radius = radius; repaint(); }
    public int getRadius() { return radius; }

    @Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 1. Gambar Background Custom Terlebih Dahulu
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

    // 2. Gambar Border Tipis
    g2.setColor(new Color(0, 0, 0, 40));
    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

    // 3. Panggil super.paintComponent SEBELUM menggambar ikon
    // Ini agar teks password tidak menimpa ikon, tapi latar belakang bawaan tidak menimpa gambar kita
    super.paintComponent(g); 

    // 4. Gambar Ikon Mata (Di urutan terakhir agar selalu di paling atas)
    int x = getWidth() - 30;
    int y = (getHeight() / 2) - 5;
    
    g2.setColor(new Color(150, 150, 150));
    if (hide) {
        g2.drawOval(x, y, 15, 10);
        g2.drawLine(x, y, x + 15, y + 10);
    } else {
        g2.drawOval(x, y, 15, 10);
        g2.fillOval(x + 5, y + 2, 5, 5);
    }

    g2.dispose();
}
}
