/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ASUS
 */
public class Database {
    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(".env")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("File .env tidak ditemukan di root!");
        }
    }

    public static String getMongoUrl() {
        return properties.getProperty("MONGODB_URL");
    }
}
