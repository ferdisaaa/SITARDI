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
public class RoundPanel extends JPanel {

    private int radius = 30;
    private boolean roundImage = false;

    public RoundPanel() {
        setOpaque(false); // Penting agar sudut yang terpotong tidak berwarna kotak
    }

    // Getter dan Setter agar muncul di Properties NetBeans
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
    
    
    public boolean isRoundImage() { return roundImage; }
    public void setRoundImage(boolean roundImage) { this.roundImage = roundImage; repaint(); }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Mengaktifkan Antialiasing agar pinggiran halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gambar bayangan (opsional) jika showShadow = true
        int r = radius;
        if (roundImage) {
            r = Math.max(getWidth(), getHeight());
        }

        // Gambar background panel
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);

        g2.dispose();
    }
}
