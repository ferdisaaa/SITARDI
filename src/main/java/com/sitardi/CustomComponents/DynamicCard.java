/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sitardi.CustomComponents;

/**
 *
 * @author ASUS
 */
import java.awt.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import utils.Pemilih;
import services.PemilihService;
import com.sitardi.Form.TambahEditPemilih;
import com.sitardi.Form.TambahEditUser;
import java.net.URL;
import javax.imageio.ImageIO;
import utils.User;
import services.UserService;

public class DynamicCard {

    // Konfigurasi Warna & Font
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_EDIT = Color.decode("#E5B278");
    private static final Color COLOR_DELETE = Color.decode("#600200");
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 15);

    public static JPanel createCard(Object obj, Object service) {
        // Layout Utama: BorderLayout agar bisa membagi Atas (Gambar), Tengah (Data), Bawah (Tombol)
        JPanel cardPanel = new JPanel(new BorderLayout(0, 15));
        cardPanel.setBackground(COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 235, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // --- 1. BAGIAN ATAS (NORTH): GAMBAR USER ---
        if (obj instanceof User) {
            User u = (User) obj;
            RoundImage imgHeader = new RoundImage();
            imgHeader.setIsCircle(true);
            imgHeader.setPreferredSize(new Dimension(100, 100));
            imgHeader.setBorderThickness(3); // Menambah estetika border

            // Logika Load Gambar
            if (u.getUrl_img() != null && !u.getUrl_img().isEmpty()) {
                try {
                    // Cek apakah ini link online atau file lokal
                    if (u.getUrl_img().startsWith("http")) {
                        // JIKA LINK ONLINE
                        URL url = new URL(u.getUrl_img());
                        Image image = ImageIO.read(url); // Mengunduh gambar
                        if (image != null) {
                            Image scaledImg = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            imgHeader.setIcon(new ImageIcon(scaledImg));
                        }
                    } else {
                        // JIKA FILE LOKAL
                        ImageIcon icon = new ImageIcon(u.getUrl_img());
                        Image scaledImg = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        imgHeader.setIcon(new ImageIcon(scaledImg));
                    }
                } catch (Exception e) {
                    System.out.println("Gagal memuat gambar: " + e.getMessage());
                    imgHeader.setText("Error");
                }
            } else {
                imgHeader.setText("No Image");
            }

            // Wrapper agar gambar berada di tengah secara horizontal
            JPanel imgWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            imgWrapper.setOpaque(false);
            imgWrapper.add(imgHeader);
            cardPanel.add(imgWrapper, BorderLayout.NORTH);
        }

        // --- 2. BAGIAN TENGAH (CENTER): DATA OTOMATIS ---
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        infoPanel.setOpaque(false);

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.isSynthetic()) {
                    continue;
                }
                field.setAccessible(true);

                String name = field.getName();
                Object value = field.get(obj);

                // Filter: Field yang TIDAK boleh muncul sebagai teks
                if (name.toLowerCase().contains("password")
                        || name.equals("_id")
                        || name.equals("url_img")) { // url_img sudah jadi gambar di atas
                    continue;
                }

                String labelName = formatLabelName(name);
                String displayValue = formatValue(value);

                JLabel label = new JLabel("<html><b>" + labelName + ":</b> " + displayValue + "</html>");
                label.setFont(name.toLowerCase().contains("name") || name.toLowerCase().contains("nama") ? FONT_TITLE : FONT_LABEL);
                infoPanel.add(label);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cardPanel.add(infoPanel, BorderLayout.CENTER);

        // --- 3. BAGIAN BAWAH (SOUTH): TOMBOL AKSI ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        if (obj instanceof Pemilih) {
            buttonPanel = createPemilihButtons((Pemilih) obj, (PemilihService) service);
        } else if (obj instanceof User) {
            buttonPanel = createUserButtons((User) obj, (UserService) service);
        }
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private static JPanel createUserButtons(User u, UserService service) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setOpaque(false);

        JButton btnEdit = createStyledButton("Edit", COLOR_EDIT);
        btnEdit.addActionListener(e -> {
            // Logika buka form edit user di sini
            TambahEditUser popUp = new TambahEditUser(null, true);
            popUp.txtUrl.setText(u.getUrl_img());
            popUp.TxtNIK.setText(u.getNik());
            popUp.TxtNama.setText(u.getName());
            popUp.TxtUsername.setText(u.getUsername());
            popUp.cmbRole.setSelectedItem(u.getRole());
            popUp.Txtemail.setText(u.getEmail());
            popUp.TxtPassword.setText(u.getPassword());
            popUp.btnUpdate.setEnabled(true);
            popUp.btnSave.setEnabled(false);
            popUp.loadProfileImage(u.getUrl_img());
            popUp.setLocationRelativeTo(null);
            popUp.setVisible(true);
        });

        JButton btnDel = createStyledButton("Hapus", COLOR_DELETE);
        btnDel.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Hapus data " + u.getName() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.hapusUser(u.getNik());
            }
        });

        panel.add(btnEdit);
        panel.add(btnDel);
        return panel;
    }

    private static JPanel createPemilihButtons(Pemilih p, PemilihService service) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);

        JButton btnEdit = createStyledButton("Edit", COLOR_EDIT);
        btnEdit.addActionListener(e -> {
            TambahEditPemilih popUp = new TambahEditPemilih(null, true);
            popUp.txtUIDRFID.setText(p.getUidRfid());
            popUp.TxtNIK.setText(p.getNik());
            popUp.TxtNama.setText(p.getNama_lengkap());
            popUp.TxtDomisili.setText(p.getDomisili());
            if (p.getTanggal_lahir() != null) {
                popUp.TxtTTL.setText(new SimpleDateFormat("dd-MM-yyyy").format(p.getTanggal_lahir()));
            }
            popUp.cmbStatus.setSelectedItem(p.getStatus_pemilih());
            popUp.cmbJenisKelamin.setSelectedItem(p.getJenis_kelamin());
            popUp.btnUpdate.setEnabled(true);
            popUp.btnSave.setEnabled(false);
            popUp.setLocationRelativeTo(null);
            popUp.setVisible(true);
        });

        JButton btnDel = createStyledButton("Hapus", COLOR_DELETE);
        btnDel.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Hapus data " + p.getNama_lengkap() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.hapusPemilih(p.getNik());
            }
        });

        panel.add(btnEdit);
        panel.add(btnDel);
        return panel;
    }

    private static String formatLabelName(String name) {
        String spaced = name.replace("_", " ").replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        return spaced.substring(0, 1).toUpperCase() + spaced.substring(1);
    }

    private static String formatValue(Object value) {
        if (value == null || value.toString().isEmpty()) {
            return "-";
        }
        if (value instanceof Date) {
            return new SimpleDateFormat("dd-MM-yyyy").format((Date) value);
        }
        return value.toString();
    }

    private static JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        return b;
    }
}
