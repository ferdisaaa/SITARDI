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

    public PemilihService() {
        this.DAO = new GenericDAO<>("Pemilih", Pemilih.class);
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
        if (key == null || key.trim().isEmpty() || key.equals("Cari.........")) return DAO.findAll();

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
        List<Pemilih> daftar = cariPemilih(key);

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());

        // Kontainer Grid (3 Kolom)
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Pemilih p : daftar) {
            // Memanggil Factory Otomatis
            gridPanel.add(DynamicCard.createCard(p, this));
        }

        // Agar scrollable
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(gridPanel, BorderLayout.NORTH);
        
        panelTarget.add(wrapper, BorderLayout.CENTER);
        panelTarget.revalidate();
        panelTarget.repaint();
    }
}
