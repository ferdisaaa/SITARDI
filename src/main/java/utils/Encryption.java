/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ASUS
 */
public class Encryption {
    private static final String ALGORITHM = "AES";
    
    private static final String KEY = "SitardiApp222026";
    private static final byte[] SECRET_KEY = KEY.getBytes();
    
    
    /**
     * Enskripsi Ubah teks biasa ke sandi
     * @param value
     * @return
     */
    public static String encrypt(String value) {
        try {
            SecretKeySpec spec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, spec);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | 
                BadPaddingException | IllegalBlockSizeException | 
                NoSuchPaddingException e) {
            System.err.println("Error saat enkripsi: " + e.getMessage());
            return null;
        }
    }
    /**
     * Deskripsi Ubah sandi ke teks biasa
     * @param encryptedValue
     * @return
     */
    public static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec spec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, spec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | NoSuchPaddingException e) {
            System.err.println("Error saat dekripsi: " + e.getMessage());
            return null;
        }
    }  
}
