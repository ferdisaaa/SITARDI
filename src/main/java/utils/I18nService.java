/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;




import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 *
 * @author MyBook Hype
 */
public class I18nService {
    private static ResourceBundle bundle;
    private static Locale currentLocale;

    static {
        // Set default bahasa awal saat aplikasi pertama kali dijalankan (Bahasa Indonesia)
        setLocale(new Locale("id", "ID"));
    }

    // Fungsi untuk mengubah bahasa secara real-time saat aplikasi berjalan (Halaman 6)
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        // Membaca file messages di dalam folder i18n (i18n/messages_xx.properties)
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
    }

    // Fungsi mengambil teks terjemahan dengan proteksi Try-Catch Shield (Halaman 10)
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            // Fallback aman: Mengembalikan !key! jika kunci belum terdaftar di .properties
            // Melindungi aplikasi dari crash dan memberikan sinyal visual ke QA (Halaman 10)
            return "!" + key + "!";
        }
    }
    
}
