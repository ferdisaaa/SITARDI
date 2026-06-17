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
import org.bson.conversions.Bson;

public class PemilihService {

    private final GenericDAO<Pemilih> DAO;
    String collectionName = System.getProperty("COLLP");

    public PemilihService() {
        this.DAO = new GenericDAO<>(collectionName, Pemilih.class);
    }

    // --- LOGIKA DATA ---
    public void tambahPemilih(Pemilih p) {
        DAO.save(p);
    }

    public void updatePemilih(Pemilih newP) {
        Bson filter = Filters.eq("nik", newP.getNik());
        if (DAO.findOne(filter) != null) {
            DAO.update(filter, newP);
            DataPemilih.showData(""); // Refresh UI
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    public void hapusPemilih(String nik) {
        DAO.delete(Filters.eq("nik", nik));
        DataPemilih.showData(""); // Refresh UI
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
    }

    public List<Pemilih> cariPemilih(String key) {
        if (key == null || key.trim().isEmpty() || key.equals("Cari.........")) {
            return DAO.findAll();
        }

        List<Bson> filters = new ArrayList<>();
        for (Field f : Pemilih.class.getDeclaredFields()) {
            if (!f.isSynthetic() && f.getType().equals(String.class)) {
                filters.add(Filters.regex(f.getName(), key, "i"));
            }
        }
        return filters.isEmpty() ? DAO.findAll() : DAO.findMany(Filters.or(filters));
    }

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

        // 2. Jalankan SwingWorker agar UI tidak freeze/lag
        SwingWorker<List<Pemilih>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Pemilih> doInBackground() throws Exception {
                // Tetap ambil data di background thread
                return cariPemilih(key);
            }

            @Override
            protected void done() {
                try {
                    List<Pemilih> daftar = get();
                    panelTarget.removeAll();

                    // Wadah kartu: Tetap gunakan susunan 3 kolom biar rapi
                    JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
                    gridPanel.setOpaque(false);
                    gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    // Masukkan semua kartu pemilih ke grid
                    for (Pemilih p : daftar) {
                        gridPanel.add(DynamicCard.createCard(p, PemilihService.this));
                    }

                    // 🛠️ FIX UTAMA: Pasang gridPanel LANGSUNG ke panelTarget NetBeans
                    // Gunakan BorderLayout.NORTH agar tinggi panel fleksibel mengikuti jumlah kartu
                    panelTarget.setLayout(new BorderLayout());
                    panelTarget.add(gridPanel, BorderLayout.NORTH);

                    // Paksa NetBeans ScrollPane untuk menghitung ulang tinggi layar baru
                    panelTarget.revalidate();
                    panelTarget.repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                    panelTarget.removeAll();
                    panelTarget.add(new JLabel("Gagal memuat data: " + e.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
                    panelTarget.revalidate();
                    panelTarget.repaint();
                }
            }
        };

        worker.execute();
    }
}
