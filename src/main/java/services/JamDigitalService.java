/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;

/**
 *
 * @author ASUS
 */
public class JamDigitalService {
    
    private final JLabel targetLabel;
    private final String pattern;

    public JamDigitalService(JLabel targetLabel, String pattern) {
        this.targetLabel = targetLabel;
        this.pattern = pattern;
    }

    /**
     * Menyiapkan objek Thread tanpa langsung menjalankannya.
     * @return Objek Thread dalam fase 'New' [3].
     */
    public Thread getThread() {
        Runnable clockTask = () -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.of("id", "ID"));
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LocalDateTime now = LocalDateTime.now();
                    String timeFormatted = now.format(formatter);
                    
                    // Update label secara asinkron
                    targetLabel.setText(timeFormatted);
                    
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
