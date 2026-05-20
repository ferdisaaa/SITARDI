/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.util.Date;

public class Pemilih {
    
    private String nik;
    private String nama_lengkap;
    private String domisili;
    private Date tanggal_lahir;
    private String status_pemilih;
    private String jenis_kelamin;
  
  
    
    
    public Pemilih(){
    }
    
    public Pemilih( String nik, String nama_lengkap, String domisili, Date tanggal_lahir, String status_pemilih, String jenis_kelamin) {
        this.nik = nik;
        this.nama_lengkap = nama_lengkap;
        this.domisili = domisili;
        this.tanggal_lahir = tanggal_lahir;
        this.status_pemilih = status_pemilih;
        this.jenis_kelamin = jenis_kelamin;
    }
    
    @Override
    public String toString() {
        return "Pemilih{" + 
                ", nik=" + nik + 
                ", nama_lengkap=" + nama_lengkap + 
                ", domisili=" + domisili + 
                ", tanggal_lahir=" + tanggal_lahir + 
                ", status_pemilih=" + status_pemilih + 
                ", jenis_kelamin=" + jenis_kelamin + '}';
    }
    
    
    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama_Lengkap() {
        return nama_lengkap;
    }

    public void setNama_Lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }

    public Date getTanggal_Lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_Lahir(java.util.Date tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getStatusPemilih() {
        return status_pemilih;
    }

    public void setStatusPemilih(String status_pemilih) {
        this.status_pemilih = status_pemilih;
    }

    public String getJenisKelamin() {
        return jenis_kelamin;
    }

    public void setJenisKelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }
}

