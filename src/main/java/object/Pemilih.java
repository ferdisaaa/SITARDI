package object;

import java.util.Date;

public class Pemilih {
    
    private String uidRfid;
    private String nik;
    private String nama_lengkap;
    private String domisili;
    private Date tanggal_lahir;
    private String status_pemilih; // Diubah jadi huruf kecil semua
    private String jenis_kelamin;
    
    public Pemilih(){
    }
    
//    public Pemilih(String nik, String nama_lengkap, String domisili, Date tanggal_lahir, String status_pemilih, String jenis_kelamin) {
//        this.nik = nik;
//        this.nama_lengkap = nama_lengkap;
//        this.domisili = domisili;
//        this.tanggal_lahir = tanggal_lahir;
//        this.status_pemilih = status_pemilih;
//        this.jenis_kelamin = jenis_kelamin;
//    }
//    
//    @Override
//    public String toString() {
//        return "Pemilih{" + 
//                "nik=" + nik + 
//                ", nama_lengkap=" + nama_lengkap + 
//                ", domisili=" + domisili + 
//                ", tanggal_lahir=" + tanggal_lahir + 
//                ", status_pemilih=" + status_pemilih + 
//                ", jenis_kelamin=" + jenis_kelamin + '}';
//    }
    
    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama_lengkap() { // Diubah jadi huruf kecil semua
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;// Diubah jadi huruf kecil semua
        }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }

    public Date getTanggal_lahir() { // Diubah jadi huruf kecil semua
        return tanggal_lahir;
    }

    public void setTanggal_lahir(Date tanggal_lahir) { // Diubah jadi huruf kecil semua
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getStatus_pemilih() { // Diubah jadi huruf kecil semua
        return status_pemilih;
    }

    public void setStatus_pemilih(String status_pemilih) { // Diubah jadi huruf kecil semua
        this.status_pemilih = status_pemilih;
    }

    public String getJenis_kelamin() { // Diubah jadi huruf kecil semua
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) { // Diubah jadi huruf kecil semua
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }
}