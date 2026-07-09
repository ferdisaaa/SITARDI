/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.SwingUtilities; // Ditambahkan untuk kestabilan UI Thread Swing

/**
 *
 * @author ASUS
 */
public class JamDigitalService {
    
    private final JLabel targetLabel;
    private final String pattern;
    private final Locale locale; // 1. Tambahkan field Locale agar dinamis

    // 2. Sesuaikan konstruktor untuk menerima objek Locale
    public JamDigitalService(JLabel targetLabel, String pattern, Locale locale) {
        this.targetLabel = targetLabel;
        this.pattern = pattern;
        this.locale = locale;
    }

    /**
     * Menyiapkan objek Thread tanpa langsung menjalankannya.
     * @return Objek Thread dalam fase 'New'.
     */
    public Thread getThread() {
        Runnable clockTask = () -> {
            // 3. Gunakan variabel locale dari konstruktor, bukan hardcoded lagi
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LocalDateTime now = LocalDateTime.now();
                    String timeFormatted = now.format(formatter);
                    
                    // 4. PENTING: Update UI Swing disarankan dibungkus dengan invokeLater
                    // agar tidak terjadi tabrakan thread (Thread-safe)
                    SwingUtilities.invokeLater(() -> targetLabel.setText(timeFormatted));
                    
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // Penanganan saat thread di-track dan dihentikan sengaja
                System.out.println(Thread.currentThread().getName() + " dihentikan.");
            }
        };

        return new Thread(clockTask);
    }
}