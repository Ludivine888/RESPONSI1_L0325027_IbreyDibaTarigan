/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package responsi1;

/**
 *
 * @author Lenovo
 */
public class BarangBantuan {
    private String kodeBarang;
    private String namaBarang;
    private String kategori;
    private double beratKg;

    // Konstruktor inisialisasi seluruh atribut (Modul 1)
    public BarangBantuan(String kodeBarang, String namaBarang, String kategori, double beratKg) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.kategori = kategori;
        this.beratKg = beratKg;
    }

    // Getter methods
    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public String getKategori() {
        return kategori;
    }

    public double getBeratKg() {
        return beratKg;
    }

    // Representasi teks rapi untuk tampilan konsol dan file teks
    @Override
    public String toString() {
        return String.format("[%-10s] %-28s | Kategori: %-12s | Berat: %6.2f kg",
                kodeBarang, namaBarang, kategori, beratKg);
    }
}