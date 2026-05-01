/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.time.LocalDateTime;

/**
 *
 * @author MyBook Hype
 */
public class LogAbsensi {

    private String LogID;
    private String NIK;
    private LocalDateTime Masuk;
    private LocalDateTime Keluar;

    public String getLogID() {
        return LogID;
    }

    public void setLogID(String LogID) {
        this.LogID = LogID;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public LocalDateTime getMasuk() {
        return Masuk;
    }

    public void setMasuk(LocalDateTime Masuk) {
        this.Masuk = Masuk;
    }

    public LocalDateTime getKeluar() {
        return Keluar;
    }

    public void setKeluar(LocalDateTime Keluar) {
        this.Keluar = Keluar;
    }


}
