package services;

import com.mongodb.client.model.Filters;
import com.sitardi.sitardi.Form.TambahEdit;
import com.sitardi.sitardi.Panels.DataPemilih;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import object.GenericDAO;
import object.Pemilih;
import org.bson.conversions.Bson;

public class PemilihService {

    private final GenericDAO<Pemilih> DAO;

    public PemilihService() {
        this.DAO = new GenericDAO<>("Pemilih", Pemilih.class);
    }

    public void tambahPemilih(Pemilih pemilihBaru) {
        DAO.save(pemilihBaru);
    }

    public void tambahPemilih(String rfid_uid, String nik, String nama_lengkap, String domisili, Date tanggal_lahir, String status_pemilih, String jenis_kelamin) {
        Pemilih pemilihBaru = new Pemilih(nik, nama_lengkap, domisili, tanggal_lahir, status_pemilih, jenis_kelamin);
        DAO.save(pemilihBaru);
    }

    public void tampilkanDaftarPemilih() {
        List<Pemilih> daftar = DAO.findAll();
        System.out.println("--- Daftar Pemilih KPPS ---");
        for (Pemilih P : daftar) {
            System.out.println(P.toString());
        }
    }

    public void tampilPemilih(JPanel panelTarget, String key) {
        List<Pemilih> daftarPemilih;
        if (key == null || key.isEmpty() || key.equals("Cari.........")) {
            daftarPemilih = DAO.findAll();
        } else {
            daftarPemilih = cariPemilih(key);
        }

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());

        // Membuat panel grid untuk menampung kartu (3 kolom)
        // Background dibuat transparan agar warna maroon pnlData di belakangnya terlihat
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            for (Pemilih P : daftarPemilih) {
                // Membuat Card Putih (Menggunakan 0 baris agar auto-layout kebawah)
                JPanel cardPanel = new JPanel(new GridLayout(0, 1, 0, 8));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));

                // Format Teks sesuai dengan Gambar Mockup Anda
                JLabel lblNama = new JLabel("Nama: " + P.getNama_lengkap());
                lblNama.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                lblNama.setForeground(Color.BLACK);

                JLabel lblNIK = new JLabel("NIK: " + P.getNik());
                lblNIK.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                lblNIK.setForeground(Color.BLACK);

                JLabel lblDomisili = new JLabel("Domisili: " + P.getDomisili());
                lblDomisili.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                lblDomisili.setForeground(Color.BLACK);

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String tanggalFormat = (P.getTanggal_lahir() != null) ? sdf.format(P.getTanggal_lahir()) : "-";

                JLabel lblTTL = new JLabel("Tanggal Lahir: " + tanggalFormat);
                lblTTL.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                lblTTL.setForeground(Color.BLACK);

                JLabel lblStts = new JLabel("Status: " + P.getStatus_pemilih());
                lblStts.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
                lblStts.setForeground(Color.BLACK);

                // Container untuk Tombol Aksi (1 Baris, 2 Kolom)
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 12, 0));
                controlPanel.setOpaque(false);

                // Tombol Edit (Krem / Gold)
                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.decode("#E5B278"));
                tombolEdit.setForeground(Color.WHITE);
                tombolEdit.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
                tombolEdit.setFocusPainted(false);
                tombolEdit.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    TambahEdit popUpEdit = new TambahEdit(null, true);
                    popUpEdit.TxtNIK.setText(P.getNik());
                    popUpEdit.TxtNama.setText(P.getNama_lengkap());
                    popUpEdit.TxtDomisili.setText(P.getDomisili());
                    if (P.getTanggal_lahir() != null) {
                        java.text.SimpleDateFormat sdfEdit = new java.text.SimpleDateFormat("dd-MM-yyyy");
                        popUpEdit.TxtTTL.setText(sdfEdit.format(P.getTanggal_lahir()));
                    } else {
                        popUpEdit.TxtTTL.setText("");
                    }
                    popUpEdit.cmbStatus.setSelectedItem(P.getStatus_pemilih());
                    popUpEdit.cmbJenisKelamin.setSelectedItem(P.getJenis_kelamin());
                    popUpEdit.btnUpdate.setEnabled(true);
                    popUpEdit.btnSave.setEnabled(false);
                    popUpEdit.setTitle("Edit Data Pemilih");
                    popUpEdit.setLocationRelativeTo(null);
                    popUpEdit.setVisible(true);
                });

                // Tombol Hapus (Maroon)
                JButton tombolDelete = new JButton("Hapus");
                tombolDelete.setBackground(Color.decode("#600200"));
                tombolDelete.setForeground(Color.WHITE);
                tombolDelete.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
                tombolDelete.setFocusPainted(false);
                tombolDelete.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Apakah Anda ingin menghapus data " + P.getNama_lengkap() + "?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        hapusPemilih(P.getNik());
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                // Masukkan label ke dalam card putih
                cardPanel.add(lblNama);
                cardPanel.add(lblNIK);
                cardPanel.add(lblDomisili);
                cardPanel.add(lblTTL);
                cardPanel.add(lblStts);

                // Berikan spasi sedikit sebelum tombol jika perlu
                cardPanel.add(Box.createVerticalStrut(5));
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);
            }

            // Bungkus gridPanel menggunakan JScrollPane agar jika card melebihi layar bisa di-scroll
            JPanel pembungkusScroll = new JPanel(new BorderLayout());
            pembungkusScroll.setOpaque(false); // Supaya warna maroon pnlData tetap kelihatan

            // 2. Tempelkan gridPanel di bagian NORTH (Utara/Atas) agar tingginya bebas melar ke bawah
            pembungkusScroll.add(gridPanel, BorderLayout.NORTH);

            // 3. Masukkan panel pembungkus tersebut ke pnlData (panelTarget)
            panelTarget.removeAll();
            panelTarget.add(pembungkusScroll, BorderLayout.CENTER);

            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public List<Pemilih> cariPemilih(String key) {
        List<Bson> filters = new ArrayList<>();

        if (key == null || key.trim().isEmpty()) {
            return DAO.findAll();
        }

        for (Field field : Pemilih.class.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            if (field.getType().equals(String.class)) {
                filters.add(Filters.regex(field.getName(), key, "i"));
            }
        }

        if (filters.isEmpty()) {
            return DAO.findAll();
        }

        return DAO.findMany(Filters.or(filters));
    }

    public void updatePemilih(Pemilih newP) {
        Bson filter = Filters.eq("nik", newP.getNik());
        Pemilih P = DAO.findOne(filter);
        if (P != null) {
            DAO.update(filter, newP);
            DataPemilih.showData("");
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    public void hapusPemilih(String NikP) {
        Bson filter = Filters.eq("nik", NikP);
        DAO.delete(filter);
        DataPemilih.showData("");
        JOptionPane.showMessageDialog(null, "Data Pemilih berhasil dihapus.");
    }
}
