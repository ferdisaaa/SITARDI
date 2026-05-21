/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.time.LocalDateTime;

/**
 *
 * @author 
 */
public class LogAbsensi {

    private String log_id;
    private String nik;
    private LocalDateTime masuk;
    private LocalDateTime keluar;
    private String status;
    
    public LogAbsensi(){
    }

     public LogAbsensi(String log_id, String nik, LocalDateTime masuk, LocalDateTime keluar, String status) {
        this.log_id = log_id;
        this.nik = nik;
        this.masuk = masuk;
        this.keluar = keluar;
        this.status = status;
    }
    public String getlog_id() {
        return log_id;
    }

    public void setlog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getnik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getLog_ID() {
        return log_id;
    }

    public void setLog_ID(String Log_id) {
        this.log_id = Log_id;
    }

    public String getNIK() {
        return nik;
    }

    public void setNIK(String nik) {
        this.nik = nik;
    }

    public LocalDateTime getmasuk() {
        return masuk;
    }

    public void setMasuk(LocalDateTime masuk) {
        this.masuk = masuk;
    }

    public LocalDateTime getkeluar() {
        return keluar;
    }

    public void setKeluar(LocalDateTime Keluar) {
        this.keluar = Keluar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String Status) {
        this.status = Status;
    }


}
