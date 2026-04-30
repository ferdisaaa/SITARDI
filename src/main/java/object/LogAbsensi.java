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
    private String UserID;
    private LocalDateTime Masuk;
    private LocalDateTime Keluar;

    public String getLogId() {
        return LogID;
    }

    public void setLogId(String logId) {
        this.LogID = logId;
    }

    public String getUidrfid() {
        return UserID;
    }

    public void setUidrfid(String uidrfid) {
        this.UserID = uidrfid;
    }

    public LocalDateTime getMasuk() {
        return Masuk;
    }

    public void setMasuk(LocalDateTime masuk) {
        this.Masuk = masuk;
    }

    public LocalDateTime getKeluar() {
        return Keluar;
    }

    public void setKeluar(LocalDateTime keluar) {
        this.Keluar = keluar;
    }

}
