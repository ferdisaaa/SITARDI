/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.sitardi.Panels.InfoTerkini;
import com.sitardi.Serial.SerialDtHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class SerialService {
    private static SerialService instance;
    private SerialPort activePort;
    private final List<SerialDtHandler<String>> handlers = new ArrayList<>();
    
    private SerialService() {}
    
    public static synchronized SerialService getInstance() {
        if (instance == null) {
            instance = new SerialService();
        }
        return instance;
    }
    
    /**
     * Tambah handler baru ke dalam daftar observer
     * @param handler
     */
    public void addHandler(SerialDtHandler<String> handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }
    
    /**
     * Hapus handler (untuk mencegah memory leak saat frame ditutup)
     * @param handler
     */
    public void removeHandler(SerialDtHandler<String> handler) {
        handlers.remove(handler);
    }
    
    /**
     * Buka koneksi ke port serial
     * @param portName
     * @param baudRate
     * @return
     */
    public boolean connect(String portName, int baudRate) {
        //Jika port sudah terbuka, tidak perlu buka lagi
        if (activePort != null && activePort.isOpen()) {
            return true;
        }
        
        activePort = SerialPort.getCommPort(portName);
        activePort.setBaudRate(baudRate);
        
        //Setting Timeout agar pembacaan tidak memblokir thread utama
        // TIMEOUT_READ_SEMI_BLOCKING cocok untuk scanner.nextLine()
        activePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
        
        if (activePort.openPort()) {
            System.out.println("INFO: Port " + portName + " terbuka.");
            setupListener();
            return true;
        } else {
            System.err.println("ERROR: Gagal membuka port " + portName);
            return false;
        }
    }
    /**
     * Mengatur listener event untuk mendeteksi data masuk secara otomatis.
     */
    private void setupListener() {
        activePort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
                
                //Menggunakan Scanner untuk menangkap satu baris utuh (ID RFID)
                try (Scanner scanner = new Scanner(activePort.getInputStream())) {
                    if (scanner.hasNextLine()) {
                        String data = scanner.nextLine().trim();
                        if (!data.isEmpty()) {
                            broadcast(data);
                        }
                    }
                } catch (Exception e) {
                    //System.err.println("Error saat membaca data: " + e.getMessage());
                }
            }
        });
    }
    /**
     * Mengirimkan data ke semua handler yang terdaftar
     */
    
    public void broadcast(String data) {
        for (SerialDtHandler<String> handler : handlers) {
            handler.onDataReceived(data);
        }
    }

    public void disconnect() {
        if (activePort != null && activePort.isOpen()) {
            activePort.removeDataListener();
            activePort.closePort();
            System.out.println("INFO: Port ditutup.");
        }
    }

    public boolean isConnected() {
        return activePort != null && activePort.isOpen();
    }
    
    public void onNikDiterima(String nikYangDitangkap) {
    
    // 1. Ambil data dari MongoDB berdasarkan NIK (Gunakan GenericDAO/PemilihDAO kamu)
    // String nama = dao.getNamaByNik(nikYangDitangkap);
    // Dummy tes:
    String nama = "Budi Santoso"; 
    String status = "Hadir (DPT)";

    // 2. Kirim ke Dashboard InfoTerkini
    // [PENTING]: Gunakan objek infoTerkini yang sudah aktif di layar
    InfoTerkini.tambahKartuPemilih(nikYangDitangkap, nama, status);
}
}
