/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author ASUS
 */
import com.mongodb.client.model.Filters;
import com.sitardi.CustomComponents.DynamicCard;
import com.sitardi.Panels.DataUser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import utils.GenericDAO;
import utils.User;
import org.bson.conversions.Bson;
import utils.Security;

public class UserService {

    private final GenericDAO<User> DAO;

    public UserService() {
        this.DAO = new GenericDAO<>("User", User.class);
    }

    // --- LOGIKA DATA ---
    public void tambahUser(User u) {
        try {
            // 1. Proses Hashing Password
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                String hashedPass = Security.getHash(u.getPassword(), Security.SHA_256);
                u.setPassword(hashedPass);
            }

            DAO.save(u);
            DataUser.showData("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
        }
    }

    public void updateUser(User newUser) {
        try {
            // 1. Cari data lama di database berdasarkan NIK
            Bson filter = Filters.eq("nik", newUser.getNik());
            User userLama = DAO.findOne(filter);

            if (userLama != null) {
                // 2. Logika Proteksi Password
                // Jika password di form kosong, gunakan password lama (yang sudah ter-hash)
                if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
                    newUser.setPassword(userLama.getPassword());
                } else {
                    // Jika user menginput password baru, cek apakah itu password baru (plain text) 
                    // atau hash lama. Jika panjangnya bukan 64 char (standar SHA-256), maka itu plain text.
                    if (newUser.getPassword().length() != 64) {
                        String hashedPass = Security.getHash(newUser.getPassword(), Security.SHA_256);
                        newUser.setPassword(hashedPass);
                    }
                }

                // 3. Eksekusi Update
                DAO.update(filter, newUser);

                // 4. Refresh UI
                DataUser.showData("");
            } else {
                JOptionPane.showMessageDialog(null, "Data dengan NIK: " + newUser.getNik() + " tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat update: " + e.getMessage());
            System.err.println("Update Error: " + e.getMessage());
        }
    }

    public void hapusUser(String id) {
        // Samakan kunci filter dengan yang ada di database
        Bson filter = Filters.eq("nik", id);

        DAO.delete(filter);
        DataUser.showData("");
        JOptionPane.showMessageDialog(null, "User berhasil dihapus.");
    }

    public List<User> cariUser(String key) {
        if (key == null || key.trim().isEmpty() || key.equals("Cari.........")) {
            return DAO.findAll();
        }

        List<Bson> filters = new ArrayList<>();
        for (Field f : User.class.getDeclaredFields()) {
            if (!f.isSynthetic() && f.getType().equals(String.class)) {
                String fieldName = f.getName();
                if (fieldName.toLowerCase().contains("password")) {
                    continue;
                }
                filters.add(Filters.regex(fieldName, key, "i"));
            }
        }

        return filters.isEmpty() ? DAO.findAll() : DAO.findMany(Filters.or(filters));
    }

    // --- LOGIKA TAMPILAN ---
    public void tampilUser(JPanel panelTarget, String key) {
        List<User> daftar = cariUser(key);

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());

        // Gunakan wrapper agar grid tidak "melar" ke bawah jika data sedikit
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (User u : daftar) {
            gridPanel.add(DynamicCard.createCard(u, this));
        }

        container.add(gridPanel, BorderLayout.NORTH);
        panelTarget.add(container, BorderLayout.CENTER);

        panelTarget.revalidate();
        panelTarget.repaint();
    }

    public User login(String username, String plainPassword) {
        try {
            // 1. Hash password yang diinput user untuk dibandingkan
            String hashedInput = Security.getHash(plainPassword, Security.SHA_256);

            // 2. Cari user di database berdasarkan username menggunakan DAO
            Bson filter = Filters.eq("username", username);
            User user = DAO.findOne(filter); // Menggunakan DAO, bukan userCollection

            // 3. Validasi: Cek apakah user ada dan password match
            if (user != null && user.getPassword().equals(hashedInput)) {
                return user; // Login Sukses, kembalikan objek User
            }
        } catch (Exception e) {
            System.err.println("Login Error: " + e.getMessage());
        }
        return null; // Login Gagal
    }
}
