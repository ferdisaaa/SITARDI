/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import object.GenericDAO;
import object.Pemilih;
import org.bson.conversions.Bson;



/**
 *
 * @author Asuss
 */
public class PemilihService {
    private final GenericDAO<Pemilih> DAO;
    
    
    
    
    public PemilihService() {
        this.DAO = new GenericDAO<>("Pemilih", Pemilih.class);
    }
    
    /**
     * 1. CREATE : fungsi untuk simpan data Pemilih baru ke Mongo
     * 
     * @param pemilihBaru
     */
    
    public void tambahPemilih(Pemilih pemilihBaru) {
        DAO.save(pemilihBaru);
    }
    
    public void tambahPemilih(String rfid_uid, String nik, String nama_lengkap, String domisili, Date tanggal_lahir, String status_pemilih, String jenis_kelamin){
        Pemilih pemilihBaru = new Pemilih ( nik, nama_lengkap, domisili, tanggal_lahir, status_pemilih, jenis_kelamin);
        DAO.save(pemilihBaru);
    }
    
    /**
     * 2. Read (All) : mengambil semua data Pemilih
     */
    
    public void tampilkanDaftarPemilih() {
        List<Pemilih> daftar = DAO.findAll();
        System.out.println("--- Daftar Pemilih KPPS ---");
        for (Pemilih P : daftar) {
            System.out.println(P.toString());
        }
    }
  
    /**
     * READ (ALL) ambil semua data pemilih
     * @param panelTarget
     * @param key
     */
    
    public void tampilPemilih(JPanel panelTarget, String key) {
         //1. 
        // Menampilkan data berdasarkan request
        // key "null/kosong" = get all data
        // key "filled" = get specific data

        List<Pemilih> daftarPemilih;
        if (key.isEmpty()) {
            //Mengambil data dari database menggunakan GenericDAO
            daftarPemilih = DAO.findAll();
        } else {
            //Mengambil data dari database menggunakan GenericDAO
            //berdasarkan kata kunci yang diketik
            daftarPemilih = cariPemilih(key);
        }
        // 2. Membersihkan panel target utama sebelum memuat data baru
        panelTarget.removeAll();

        // Mengubah layout panel target menjadi BorderLayout
        panelTarget.setLayout(new BorderLayout());
        

        // Membuat panel grid khusus untuk menampung kotak/card
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false); // Transparan agar warna biru panelTarget terlihat
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Memberi jarak dari tepi layar
        
        
        // 3. Iterasi data dan menambahkannya ke panel grid
        try {
            for (Pemilih P : daftarPemilih) {
                // Membuat panel 'Card' (box cream) untuk 1 Pemilih
                // Layout 4 baris 1 kolom agar kolor berisi Nama,ID, Departemen, panel control 
                JPanel cardPanel = new JPanel(new GridLayout(7, 1, 0, 0));
                cardPanel.setBackground(Color.white); // Warna background putih

                // Memberikan garis tepi tipis membulat (rounded) dan padding/jarak ke dalam
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // Membuat Label Nama & Set warna teks jadi Putih
                JLabel lblNIK = new JLabel("nik : " + P.getNik());
                lblNIK.setForeground(Color.BLACK);
                
                JLabel lblNama = new JLabel("nama_lengkap: " + P.getNama_Lengkap());
                lblNama.setForeground(Color.BLACK);

                // Membuat Label ID Karyawan & Set warna teks jadi Putih
                JLabel lblDomisili = new JLabel("domisili: " + P.getDomisili());
                lblDomisili.setForeground(Color.BLACK);

                // Membuat Label Departemen & Set warna teks jadi Putih
                JLabel lblTTL = new JLabel("tanggal_lahir: " + P.getTanggal_Lahir());
                lblTTL.setForeground(Color.BLACK);
                
                JLabel lblStts = new JLabel("status_pemilih: " + P.getStatusPemilih());
                lblStts.setForeground(Color.BLACK);
                
                JLabel lblJK = new JLabel("jenis_kelamin: " + P.getJenisKelamin());
                lblJK.setForeground(Color.BLACK);

                // Membuat panel kontrol 1 baris 2 kolom, berisi tombol edit dan hapus
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                

                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    TambahEdit popUpEdit = new TambahEdit(null, true);
                
                    popUpEdit.TxtNIK.setText(P.getNik());
                    popUpEdit.TxtNama.setText(P.getNama_Lengkap());
                    popUpEdit.TxtDomisili.setText(P.getDomisili());
                    if (P.getTanggal_Lahir() != null){
                        popUpEdit.TxtTTL.setText(P.getTanggal_Lahir().toString());
                    } else {
                        popUpEdit.TxtTTL.setText(""); // Atau isi teks kosong
                    }
                    popUpEdit.TxtStatus.setText(P.getStatusPemilih());
                    popUpEdit.cmbJenisKelamin.setSelectedItem(P.getJenisKelamin());
                    popUpEdit.btnUpdate.setEnabled(true);
                    popUpEdit.btnSave.setEnabled(false); 
                    popUpEdit.setTitle("Edit Data Pemilih");
                    popUpEdit.setLocationRelativeTo(null); 
                    popUpEdit.setVisible(true);
                });
                JButton tombolDelete = new JButton("Delete");
                tombolDelete.setBackground(Color.RED);
                tombolDelete.setForeground(Color.WHITE);
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null, // Parent component
                            "Apakah Anda ingin menghapus data "+P.getNama_Lengkap()+"?", // Message
                            "Konfirmasi Pengelolaan", // Title
                            JOptionPane.YES_NO_OPTION, // Option type
                            JOptionPane.QUESTION_MESSAGE, // Message type
                            null, // Custom icon (null uses default)
                            options, // The array of custom button text
                            options[0] // Default button focused
                    );

                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusPemilih(P.getNik());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {
                        }
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                // Memasukkan label ke dalam c
                cardPanel.add(lblNIK);
                cardPanel.add(lblNama);
                cardPanel.add(lblDomisili);
                cardPanel.add(lblTTL);
                cardPanel.add(lblStts);
                cardPanel.add(lblJK);
                cardPanel.add(controlPanel);

                // Memasukkan cardPanel utuh ke dalam gridPanel
                gridPanel.add(cardPanel);
            }

            // Memasukkan gridPanel ke bagian ATAS (NORTH) dari panel target.
            panelTarget.add(gridPanel, BorderLayout.NORTH);

            // 4. Me-refresh panel agar perubahan muncul di GUI
            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
        }
    }

    
    /**
     * 3.READ (One): Mencari satu karyawan spesifik berdasarkan UID RFID [5],
     * [6] Sangat krusial untuk alur Tap Kartu pada Pertemuan 14 [8].
     *
     * @param key
     * @return
     */
    public List<Pemilih> cariPemilih(String key) {
        List<Bson> filters = new ArrayList<>();
        
        for (Field field : Pemilih.class.getDeclaredFields()) {
            // Skip the uidRfid field and non-string fields if necessary
            if (field.getName().equals("nama")) {
                continue;
            } else {
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
        }
        // Search and return Karyawan objects directly
        List<Pemilih> results = DAO.findMany(Filters.or(filters));
        return results;
    }

    /**
     * 4.UPDATE: Memperbarui data karyawan menggunakan filter Bson [5], [6]
     *
     * @param newP
     */
    public void updatePemilih(Pemilih newP) {
        Bson filter = Filters.eq("nik", newP.getNik());
        Pemilih P = DAO.findOne(filter);
        if (P != null) {
            DAO.update(filter, newP);
            DataPemilih.showData("");
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    /**
     * 5.DELETE: Menghapus data karyawan dari database [5], [6]
     *
     * @param NikP
     */
    public void hapusPemilih(String NikP) {
        Bson filter = Filters.eq("nik", NikP);
        DAO.delete(filter); // Menggunakan deleteOne [6]
        DataPemilih.showData("");
        JOptionPane.showMessageDialog(null, "Data Pemilih berhasil dihapus.");
    }
}

