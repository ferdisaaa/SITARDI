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
import org.bson.types.ObjectId;
import utils.Security;

public class UserService {

    private final GenericDAO<User> DAO;

    String collectionName = System.getProperty("COLLU");

    public UserService() {
        this.DAO = new GenericDAO<>(collectionName, User.class);
    }

    // --- LOGIKA DATA ---
    public void tambahUser(User u) {
        try {
            // 1. Proses Hashing Password
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                String hashedPass = Security.getHash(u.getPassword(), Security.SHA_256);
                u.setPassword(hashedPass);
            }

            if (u.getNik() != null && !u.getNik().isEmpty()) {
                u.setNik(utils.Encryptions.encrypt(u.getNik()));
            }
            if (u.getEmail() != null && !u.getEmail().isEmpty()) {
                u.setEmail(utils.Encryptions.encrypt(u.getEmail()));
            }

            DAO.save(u);
            DataUser.showData("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
        }
    }

    public void updateUser(User newUser) {
        try {
            // 1. Cari data lama di database berdasarkan _id bawaan MongoDB
            Bson filter = Filters.eq("_id", newUser.getId());
            User userLama = DAO.findOne(filter);

            if (userLama != null) {
                // 2. Logika Proteksi Password
                if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
                    newUser.setPassword(userLama.getPassword());
                } else {
                    if (newUser.getPassword().length() != 64) {
                        String hashedPass = Security.getHash(newUser.getPassword(), Security.SHA_256);
                        newUser.setPassword(hashedPass);
                    }
                }

                if (newUser.getNik() != null && !newUser.getNik().isEmpty()) {
                    newUser.setNik(utils.Encryptions.encrypt(newUser.getNik()));
                }
                if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
                    newUser.setEmail(utils.Encryptions.encrypt(newUser.getEmail()));
                }

                // 3. Eksekusi Update berdasarkan _id
                DAO.update(filter, newUser);

                // 4. Refresh UI
                DataUser.showData("");
            } else {
                JOptionPane.showMessageDialog(null, "Data User tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat update: " + e.getMessage());
            System.err.println("Update Error: " + e.getMessage());
        }
    }

    public void hapusUser(String id) {
        try {
            // Konversi String ID Hexadecimal menjadi ObjectId bawaan MongoDB
            Bson filter = Filters.eq("_id", new ObjectId(id));

            DAO.delete(filter);
            DataUser.showData("");
            JOptionPane.showMessageDialog(null, "User berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            // Antisipasi jika format string id yang dikirim dari UI tidak valid/bukan format ObjectId
            JOptionPane.showMessageDialog(null, "Format ID tidak valid: " + e.getMessage());
        }
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

        for (User u : daftar) {
            // Dekripsi NIK
            if (u.getNik() != null && !u.getNik().isEmpty()) {
                try {
                    String decryptedNik = utils.Encryptions.decrypt(u.getNik());
                    if (decryptedNik != null) {
                        u.setNik(decryptedNik);
                    }
                } catch (Exception ex) {
                    System.err.println("Gagal dekripsi NIK User: " + ex.getMessage());
                }
            }

            // Dekripsi Email
            if (u.getEmail() != null && !u.getEmail().isEmpty()) {
                try {
                    String decryptedEmail = utils.Encryptions.decrypt(u.getEmail());
                    if (decryptedEmail != null) {
                        u.setEmail(decryptedEmail);
                    }
                } catch (Exception ex) {
                    System.err.println("Gagal dekripsi Email User: " + ex.getMessage());
                }
            }
        }

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
