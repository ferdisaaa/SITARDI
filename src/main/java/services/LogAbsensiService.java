/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.mongodb.client.model.Filters;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import org.bson.conversions.Bson;
import utils.GenericDAO;
import utils.LogAbsensi;
import utils.Security;

/**
 *
 * @author ASUS
 */
public class LogAbsensiService {

    // Inisialisasi DAO menuju koleksi "log_absensi" di MongoDB
   private final GenericDAO<LogAbsensi> DAO;
    // Mengambil nama koleksi log absensi dari system property (misal: COLLL)
    String collectionName = System.getProperty("COLLLA") != null ? System.getProperty("COLLLA") : "log_absensi";

    public LogAbsensiService() {
        this.DAO = new GenericDAO<>(collectionName, LogAbsensi.class);
    }

    /**
     * Menyimpan log absensi baru ke MongoDB
     */
    public void catatLog(String nomorIdentitas, String status) {
        try {
            LogAbsensi log = new LogAbsensi();
            
            // Jika nomorIdentitas diproses sebagai RFID, lakukan HASH SHA-256 terlebih dahulu
            // agar sinkron dengan data yang tersimpan di koleksi Pemilih
            String hashedIdentitas = nomorIdentitas.trim();
            if (hashedIdentitas.length() != 64) { // Jika belum berbentuk Hash SHA-256
                hashedIdentitas = Security.getHash(hashedIdentitas, Security.SHA_256);
            }

            log.setUidRfid(hashedIdentitas);
            log.setWaktuTap(LocalDateTime.now()); // Menggunakan LocalDateTime sesuai model
            log.setStatus(status);

            DAO.save(log);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencatat log absensi: " + e.getMessage());
        }
    }

    /**
     * Memeriksa apakah UID RFID tertentu sudah melakukan absensi dengan status tertentu HARI INI
     */
    public boolean cekStatusAbsenHariIni(String nomorIdentitas, String statusYgDicari) {
        try {
            String hashedIdentitas = nomorIdentitas.trim();
            if (hashedIdentitas.length() != 64) {
                hashedIdentitas = Security.getHash(hashedIdentitas, Security.SHA_256);
            }

            // Menentukan batas awal hari ini (00:00:00.000) menggunakan LocalDateTime
            LocalDateTime awalHari = LocalDateTime.now().with(LocalTime.MIN);
            
            // Menentukan batas akhir hari ini (23:59:59.999)
            LocalDateTime akhirHari = LocalDateTime.now().with(LocalTime.MAX);

            // Filter MongoDB disesuaikan dengan properti di kelas LogAbsensi: uidRfid dan waktuTap
            Bson filter = Filters.and(
                Filters.eq("uidRfid", hashedIdentitas),
                Filters.eq("status", statusYgDicari),
                Filters.gte("waktuTap", awalHari),
                Filters.lte("waktuTap", akhirHari)
            );

            // Melakukan pencarian menggunakan GenericDAO
            LogAbsensi logKetemu = DAO.findOne(filter);
            
            return logKetemu != null;

        } catch (Exception e) {
            System.err.println("Gagal mengecek status log absensi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tambahan: Mengambil semua data log absensi jika diperlukan untuk tabel
     */
    public List<LogAbsensi> ambilSemuaLog() {
        return DAO.findAll();
    }
}
