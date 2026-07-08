package com.sitardi.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// Tambahkan 2 import baru ini
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * @author ASUS
 */
public class SliderModeButton extends JComponent {
    private boolean masukMode = true; // true = MASUK, false = KELUAR
    private float animationProgress = 0.0f; // 0.0 (Masuk) sampai 1.0 (Keluar)
    private Timer timer;

    // Konfigurasi Warna & Desain
    private final Color COLOR_BG = Color.decode("#EAEAEA");
    private final Color COLOR_MASUK = Color.decode("#2ECC71");  // Hijau
    private final Color COLOR_KELUAR = Color.decode("#E74C3C"); // Merah
    private final Color COLOR_TEXT_INACTIVE = Color.decode("#95A5A6");

    public SliderModeButton() {
        // Ukuran default slider
        setPreferredSize(new Dimension(160, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Event ketika slider diklik
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toggleMode();
            }
        });

        // Timer untuk handle animasi sliding yang smooth
        timer = new Timer(15, e -> {
            if (masukMode) {
                animationProgress -= 0.1f;
                if (animationProgress <= 0.0f) {
                    animationProgress = 0.0f;
                    timer.stop();
                }
            } else {
                animationProgress += 0.1f;
                if (animationProgress >= 1.0f) {
                    animationProgress = 1.0f;
                    timer.stop();
                }
            }
            repaint();
        });
    }

    /**
     * MEKANISME AGAR NETBEANS MENGENALI ACTION PERFORMED
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    protected void fireActionPerformed() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            // Membuat event tiruan seolah-olah tombol ditekan
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, masukMode ? "MASUK" : "KELUAR");
            for (ActionListener listener : listeners) {
                listener.actionPerformed(evt);
            }
        }
    }

    public boolean isMasukMode() {
        return masukMode;
    }

    public void setMasukMode(boolean masukMode) {
        this.masukMode = masukMode;
        this.animationProgress = masukMode ? 0.0f : 1.0f;
        repaint();
    }

    private void toggleMode() {
        boolean oldVal = masukMode;
        masukMode = !masukMode;
        timer.start();
        
        // 1. Picu Property Change
        firePropertyChange("mode", oldVal, masukMode);
        
        // 2. Picu Action Performed (Fungsi baru)
        fireActionPerformed();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 4;

        // 1. Gambar Background Track
        g2.setColor(COLOR_BG);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, h, h));

        // 2. Hitung Posisi & Warna Slider
        int sliderW = (w / 2) - margin;
        int sliderH = h - (margin * 2);
        float startX = margin;
        float endX = (w / 2);
        float sliderX = startX + (endX - startX) * animationProgress;

        int r = (int) (COLOR_MASUK.getRed() + (COLOR_KELUAR.getRed() - COLOR_MASUK.getRed()) * animationProgress);
        int gr = (int) (COLOR_MASUK.getGreen() + (COLOR_KELUAR.getGreen() - COLOR_MASUK.getGreen()) * animationProgress);
        int b = (int) (COLOR_MASUK.getBlue() + (COLOR_KELUAR.getBlue() - COLOR_MASUK.getBlue()) * animationProgress);
        Color currentSliderColor = new Color(r, gr, b);

        g2.setColor(currentSliderColor);
        g2.fill(new RoundRectangle2D.Float(sliderX, margin, sliderW, sliderH, sliderH, sliderH));

        // 3. Cetak Teks
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        
        String txtMasuk = "MASUK";
        String txtKeluar = "KELUAR";

        int masukX = (w / 4) - (fm.stringWidth(txtMasuk) / 2);
        int keluarX = (3 * w / 4) - (fm.stringWidth(txtKeluar) / 2);
        int textY = ((h - fm.getHeight()) / 2) + fm.getAscent();

        Color colorTxtMasuk = interpolateColor(Color.WHITE, COLOR_TEXT_INACTIVE, animationProgress);
        Color colorTxtKeluar = interpolateColor(COLOR_TEXT_INACTIVE, Color.WHITE, animationProgress);

        g2.setColor(colorTxtMasuk);
        g2.drawString(txtMasuk, masukX, textY);

        g2.setColor(colorTxtKeluar);
        g2.drawString(txtKeluar, keluarX, textY);

        g2.dispose();
    }

    private Color interpolateColor(Color color1, Color color2, float fraction) {
        int r = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * fraction);
        int gr = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * fraction);
        int b = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * fraction);
        return new Color(r, gr, b);
    }
}