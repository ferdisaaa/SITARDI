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
public class LanguageToggle extends JPanel {

    private final String[] labelLangs = {"ID", "EN", "日本語"};
    private final String[] fullLangs = {"Bahasa Indonesia", "English", "日本語"};
    
    private int selectedIndex = 0; // Default: 0 (ID)
    
    // Konfigurasi Warna
    private final Color COLOR_BG = new Color(235, 235, 235);
    private final Color COLOR_SLIDER = new Color(96, 2, 0); // Menyesuaikan warna tema aplikasi
    private final Color COLOR_TEXT_UNSELECTED = new Color(100, 100, 100);
    private final Color COLOR_TEXT_SELECTED = Color.WHITE;

    public LanguageToggle() {
        setOpaque(false);
        setPreferredSize(new Dimension(180, 35));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listener untuk mendeteksi area klik dan menggeser slider
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int sectionWidth = getWidth() / 3;
                int newIndex = e.getX() / sectionWidth;
                
                // Batasi index agar tidak out of bounds jika diklik di ujung border
                if (newIndex > 2) newIndex = 2;
                
                if (selectedIndex != newIndex) {
                    selectedIndex = newIndex;
                    repaint(); // Gambar ulang slider ke posisi baru
                    
                    // Memicu event agar form lain bisa mendengarkan perubahan bahasa
                    firePropertyChange("language", "", fullLangs[selectedIndex]);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Anti-aliasing agar sudut melengkung terlihat halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int sectionWidth = width / 3;

        // 1. Gambar Background Pill
        g2.setColor(COLOR_BG);
        g2.fillRoundRect(0, 0, width, height, height, height);

        // 2. Gambar Indikator Slider Aktif
        g2.setColor(COLOR_SLIDER);
        int padding = 3;
        int sliderX = (selectedIndex * sectionWidth) + padding;
        int sliderY = padding;
        int sliderW = sectionWidth - (padding * 2);
        int sliderH = height - (padding * 2);
        g2.fillRoundRect(sliderX, sliderY, sliderW, sliderH, sliderH, sliderH);

        // 3. Gambar Teks Bahasa
        FontMetrics fm = g2.getFontMetrics(getFont());
        for (int i = 0; i < 3; i++) {
            String text = labelLangs[i];
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            
            // Kalkulasi posisi X & Y agar teks tepat di tengah masing-masing seksi
            int x = (i * sectionWidth) + (sectionWidth - textWidth) / 2;
            int y = (height + textHeight) / 2 - 2;

            if (i == selectedIndex) {
                g2.setColor(COLOR_TEXT_SELECTED);
                g2.setFont(getFont().deriveFont(Font.BOLD));
            } else {
                g2.setColor(COLOR_TEXT_UNSELECTED);
                g2.setFont(getFont().deriveFont(Font.PLAIN));
            }
            g2.drawString(text, x, y);
        }
    }

    // Method untuk mengambil bahasa yang sedang dipilih
    public String getSelectedLanguage() {
        return fullLangs[selectedIndex];
    }
}
