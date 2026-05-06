/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.sitardi.CustomComponents;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 *
 * @author ASUS
 */
public class RoundImage extends JLabel {
private int radius = 30;
    private boolean isCircle = false;
    private Color borderColor = new Color(229, 178, 120); // Default #E5B278
    private int borderThickness = 2;

    public RoundImage() {
        // Memastikan label memiliki ukuran default agar tidak hilang saat ditarik
        setPreferredSize(new Dimension(100, 100));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    // Getter dan Setter agar muncul di Properties NetBeans
    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; repaint(); }

    public boolean isIsCircle() { return isCircle; }
    public void setIsCircle(boolean isCircle) { this.isCircle = isCircle; repaint(); }

    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; repaint(); }

    public int getBorderThickness() { return borderThickness; }
    public void setBorderThickness(int borderThickness) { this.borderThickness = borderThickness; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Tentukan Bentuk (Shape)
        Shape shape;
        if (isCircle) {
            shape = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
        } else {
            shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius);
        }

        // 2. Potong Gambar (Clip)
        g2.setClip(shape);
        super.paintComponent(g2); // Gambar icon/gambar di sini
        
        // 3. Matikan Clip untuk menggambar Border di atasnya
        g2.setClip(null);
        
        // 4. Gambar Border
        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            
            // Offset sedikit agar border tidak terpotong pinggiran komponen
            double offset = borderThickness / 2.0;
            Shape borderShape;
            if (isCircle) {
                borderShape = new Ellipse2D.Double(offset, offset, getWidth() - borderThickness, getHeight() - borderThickness);
            } else {
                borderShape = new RoundRectangle2D.Double(offset, offset, getWidth() - borderThickness, getHeight() - borderThickness, radius, radius);
            }
            g2.draw(borderShape);
        }

        g2.dispose();
    }
}
