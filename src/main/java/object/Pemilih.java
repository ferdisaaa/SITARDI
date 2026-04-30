/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

/**
 *
 * @author MyBook Hype
 */
public class Pemilih {
    private String uidRfid;
    private String idPemilih;
    private String namaLengkap;
    private String Alamat;

    public Pemilih (){
        
    }
    
    public Pemilih (String uidRfid, String idPemilih, String namaLengkap, String Alamat) {
        this.uidRfid = uidRfid;
        this.idPemilih = idPemilih;
        this.namaLengkap = namaLengkap;
        this.Alamat= Alamat;
    }

    public String getDepartemen() {
        return Alamat;
    }

    public void setDepartemen(String Alamat) {
        this.Alamat = Alamat;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getIdKaryawan() {
        return idPemilih;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idPemilih = idPemilih;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    @Override
    public String toString() {
        return "Pemilih{" + 
                "uidRfid=" + uidRfid + 
                ", idKaryawan=" + idPemilih+ 
                ", namaLengkap=" + namaLengkap + 
                ", Alamat=" + Alamat+ '}';
    }
}
