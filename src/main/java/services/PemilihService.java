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

public class PemilihService {
    private final GenericDAO<Pemilih> DAO;
    
    public PemilihService() {
        this.DAO = new GenericDAO<>("Pemilih", Pemilih.class);
    }
    
    public void tambahPemilih(Pemilih pemilihBaru) {
        DAO.save(pemilihBaru);
    }
    
    public void tambahPemilih(String rfid_uid, String nik, String nama_lengkap, String domisili, Date tanggal_lahir, String status_pemilih, String jenis_kelamin){
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
        if (key == null || key.isEmpty()) {
            daftarPemilih = DAO.findAll();
        } else {
            daftarPemilih = cariPemilih(key);
        }
        
        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        try {
            for (Pemilih P : daftarPemilih) {
                JPanel cardPanel = new JPanel(new GridLayout(7, 1, 0, 0));
                cardPanel.setBackground(Color.white);
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel lblNIK = new JLabel("nik : " + P.getNik());
                lblNIK.setForeground(Color.BLACK);
                
                JLabel lblNama = new JLabel("nama_lengkap: " + P.getNama_lengkap());
                lblNama.setForeground(Color.BLACK);

                JLabel lblDomisili = new JLabel("domisili: " + P.getDomisili());
                lblDomisili.setForeground(Color.BLACK);
                
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String tanggalFormat = (P.getTanggal_lahir() != null) ? sdf.format(P.getTanggal_lahir()) : "-";

                JLabel lblTTL = new JLabel("tanggal_lahir: " + tanggalFormat);
                lblTTL.setForeground(Color.BLACK);
                
                JLabel lblStts = new JLabel("status_pemilih: " + P.getStatus_pemilih());
                lblStts.setForeground(Color.BLACK);
                
                JLabel lblJK = new JLabel("jenis_kelamin: " + P.getJenis_kelamin());
                lblJK.setForeground(Color.BLACK);

                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));

                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    TambahEdit popUpEdit = new TambahEdit(null, true);
                
                    popUpEdit.TxtNIK.setText(P.getNik());
                    popUpEdit.TxtNama.setText(P.getNama_lengkap());
                    popUpEdit.TxtDomisili.setText(P.getDomisili());
                    if (P.getTanggal_lahir() != null){
                        java.text.SimpleDateFormat sdfEdit = new java.text.SimpleDateFormat("dd-MM-yyyy");
                        popUpEdit.TxtTTL.setText(sdfEdit.format(P.getTanggal_lahir()));
                        } else {
                                popUpEdit.TxtTTL.setText("");
                                }
                    popUpEdit.TxtStatus.setText(P.getStatus_pemilih());
                    popUpEdit.cmbJenisKelamin.setSelectedItem(P.getJenis_kelamin());
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
                            null, 
                            "Apakah Anda ingin menghapus data " + P.getNama_lengkap() + "?", 
                            "Konfirmasi Pengelolaan", 
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

                cardPanel.add(lblNIK);
                cardPanel.add(lblNama);
                cardPanel.add(lblDomisili);
                cardPanel.add(lblTTL);
                cardPanel.add(lblStts);
                cardPanel.add(lblJK);
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);
            }

            panelTarget.add(gridPanel, BorderLayout.NORTH);
            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pemilih> cariPemilih(String key) {
        List<Bson> filters = new ArrayList<>();
        
        for (Field field : Pemilih.class.getDeclaredFields()) {
            // FIX: Diubah ke nama_lengkap sesuai variabel baru di Pemilih.java
            if (field.getName().equals("nama_lengkap")) {
                continue;
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
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