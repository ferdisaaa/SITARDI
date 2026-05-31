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
import com.sitardi.sitardi.CustomComponents.DynamicCard;
import com.sitardi.sitardi.Panels.DataUser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import object.GenericDAO;
import object.User;
import org.bson.conversions.Bson;

public class UserService {

    private final GenericDAO<User> DAO;

    public UserService() {
        this.DAO = new GenericDAO<>("User", User.class);
    }

    // --- LOGIKA DATA ---
    public void tambahUser(User u) {
        try {
            DAO.save(u);
            JOptionPane.showMessageDialog(null, "User berhasil disimpan!");
            DataUser.showData("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
        }
    }

    public void updateUser(User newUser) {
        // PASTIKAN: "UserId" adalah nama field di MongoDB. 
        // Jika di MongoDB fieldnya bernama "nik", ganti string di bawah menjadi "nik"
        Bson filter = Filters.eq("nik", newUser.getNik());

        if (DAO.findOne(filter) != null) {
            DAO.update(filter, newUser);
            DataUser.showData("");
            JOptionPane.showMessageDialog(null, "Data User berhasil diperbarui!");
        } else {
            JOptionPane.showMessageDialog(null, "Data tidak ditemukan untuk diupdate!");
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

    public User login(String username, String password) {
        // Mencari user yang username DAN password-nya cocok
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password)
        );

        return DAO.findOne(filter);
    }
}
