package services;

import com.mongodb.client.model.Filters;
import com.sitardi.Panels.DataPemilih;
import com.sitardi.CustomComponents.DynamicCard;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import utils.GenericDAO;
import utils.Pemilih;
import utils.Security;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class PemilihService {

    private final GenericDAO<Pemilih> DAO;
    String collectionName = System.getProperty("COLLP");

    public PemilihService() {
        this.DAO = new GenericDAO<>(collectionName, Pemilih.class);
    }

    // --- LOGIKA DATA ---
    public void tambahPemilih(Pemilih p) {
        try {
            if (p.getUidRfid() != null && !p.getUidRfid().isEmpty()) {
                String hashUidRfid = Security.getHash(p.getUidRfid(), Security.SHA_256);
                p.setUidRfid(hashUidRfid);
            }
            if (p.getNik() != null && !p.getNik().isEmpty()) {
                p.setNik(utils.Encryptions.encrypt(p.getNik()));
            }

            DAO.save(p);
            DataPemilih.showData("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
        }
    }

    public void updatePemilih(Pemilih newP) {
        try {
            // 1. Cari data lama di database berdasarkan _id bawaan MongoDB
            Bson filter = Filters.eq("_id", newP.getId());
            Pemilih pemilihLama = DAO.findOne(filter);

            if (pemilihLama != null) {
                // 2. KONDISIONAL UPDATE RFID: Jika kosong, gunakan data lama dari DB
                if (newP.getUidRfid() == null || newP.getUidRfid().trim().isEmpty()) {
                    newP.setUidRfid(pemilihLama.getUidRfid());
                } else {
                    if (newP.getUidRfid().length() != 64) {
                        String hashUidRfid = Security.getHash(newP.getUidRfid(), Security.SHA_256);
                        newP.setUidRfid(hashUidRfid);
                    }
                }

                // 3. Proses Enkripsi NIK baru yang diinput dari form sebelum disimpan
                if (newP.getNik() != null && !newP.getNik().isEmpty()) {
                    String encryptedNik = utils.Encryptions.encrypt(newP.getNik());
                    newP.setNik(encryptedNik);
                }

                // 4. Eksekusi Update ke Database berdasarkan _id
                DAO.update(filter, newP);
                DataPemilih.showData(""); // Refresh UI
                JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
            } else {
                JOptionPane.showMessageDialog(null, "Data Pemilih tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat update: " + e.getMessage());
        }
    }

    public void hapusPemilih(String id) { // <-- Ubah parameter dari 'String nik' menjadi 'String id'
        try {
            // Konversi String ID Hexadecimal menjadi ObjectId bawaan MongoDB
            Bson filter = Filters.eq("_id", new ObjectId(id));
            DAO.delete(filter);

            DataPemilih.showData(""); // Refresh UI
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Format ID tidak valid: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus: " + e.getMessage());
        }
    }

    public List<Pemilih> cariPemilih(String key) {
        if (key == null || key.trim().isEmpty() || key.equals("Cari.........")) {
            return DAO.findAll();
        }

        List<Bson> filters = new ArrayList<>();
        
        try {
            String encryptedKey = utils.Encryptions.encrypt(key.trim());
            filters.add(Filters.eq("nik", encryptedKey));
        } catch (StringIndexOutOfBoundsException e) {
        }

        for (Field f : Pemilih.class.getDeclaredFields()) {
            if (!f.isSynthetic() && f.getType().equals(String.class)) {
                String fieldName = f.getName();
                
                // Lewati nik dan uidRfid dari fungsi regex pencarian biasa
                if (fieldName.equals("nik")) {
                    continue;
                }
                filters.add(Filters.regex(fieldName, key, "i"));
            }
        }
        return filters.isEmpty() ? DAO.findAll() : DAO.findMany(Filters.or(filters));
    }

//    public Pemilih cariBerdasarkanUid(String uid) {
//        if (uid == null || uid.trim().isEmpty()) {
//            return null;
//        }
//        return DAO.findOne(Filters.eq("uidRfid", uid));
//    }

    // --- LOGIKA TAMPILAN (MENGGUNAKAN FACTORY) ---
    public void tampilPemilih(JPanel panelTarget, String key) {
        // 1. Tampilkan teks loading ringan di awal
        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());
        JLabel lblLoading = new JLabel("Memuat data dari database...", SwingConstants.CENTER);
        lblLoading.setFont(new Font("SansSerif", Font.ITALIC, 14));
        panelTarget.add(lblLoading, BorderLayout.CENTER);
        panelTarget.revalidate();
        panelTarget.repaint();

        new Thread(() -> {
            try {
                // Jalankan query MongoDB di background thread
                List<Pemilih> daftar = cariPemilih(key);
                
                for (Pemilih p : daftar) {
                    // Dekripsi NIK
                    if (p.getNik() != null && !p.getNik().isEmpty()) {
                        try {
                            String decryptedNik = utils.Encryptions.decrypt(p.getNik());
                            if (decryptedNik != null) {
                                p.setNik(decryptedNik);
                            }
                        } catch (Exception ex) {
                            System.err.println("Gagal dekripsi NIK: " + ex.getMessage());
                        }
                    }
                    
                    // Dekripsi UID RFID
                }

                // Setelah data siap dan terdekripsi, kirim proses rendering ke EDT
                SwingUtilities.invokeLater(() -> {
                    panelTarget.removeAll();

                    // Wadah kartu: Tetap gunakan susunan 3 kolom biar rapi
                    JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
                    gridPanel.setOpaque(false);
                    gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    // Masukkan semua kartu pemilih ke grid (kartu sekarang akan otomatis menampilkan UID asli)
                    for (Pemilih p : daftar) {
                        gridPanel.add(DynamicCard.createCard(p, PemilihService.this));
                    }

                    // Pasang gridPanel LANGSUNG ke panelTarget NetBeans
                    panelTarget.setLayout(new BorderLayout());
                    panelTarget.add(gridPanel, BorderLayout.NORTH);

                    // Paksa NetBeans ScrollPane untuk menghitung ulang tinggi layar baru
                    panelTarget.revalidate();
                    panelTarget.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    panelTarget.removeAll();
                    panelTarget.add(new JLabel("Gagal memuat data: " + e.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
                    panelTarget.revalidate();
                    panelTarget.repaint();
                });
            }
        }).start();
    }
    
    
    public Pemilih cariBerdasarkanIdentitas(String nomorIdentitas) {
        if (nomorIdentitas == null || nomorIdentitas.trim().isEmpty()) {
            return null;
        }
        
        try {
            String inputMentah = nomorIdentitas.trim();

            // 1. Konversi ke SHA-256 (jika input adalah UID RFID mentah)
            String hashUidRfid = Security.getHash(inputMentah, Security.SHA_256);

            // 2. Konversi ke Enkripsi AES (jika input adalah NIK mentah)
            String encryptedNik = utils.Encryptions.encrypt(inputMentah);

            // 3. Buat query OR: Cari yang uidRfid-nya cocok ATAU nik-nya cocok
            Bson filter = Filters.or(
                Filters.eq("uidRfid", hashUidRfid),
                Filters.eq("nik", encryptedNik)
            );

            // 4. Eksekusi ke MongoDB
            return DAO.findOne(filter);
            
        } catch (Exception e) {
            System.err.println("Gagal melakukan enkripsi/pencarian validasi: " + e.getMessage());
            return null;
        }
    }
}
