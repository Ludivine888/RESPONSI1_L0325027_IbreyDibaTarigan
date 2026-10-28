/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package responsi1;

/**
 *
 * @author Lenovo
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainLogistik3T {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Modul 4: Koleksi dinamis ArrayList untuk menampung daftar muatan
        ArrayList<BarangBantuan> daftarBarang = new ArrayList<>();
        boolean berjalan = true;

        System.out.println("=====================================================");
        System.out.println("   SISTEM LOGISTIK PENDIDIKAN DAERAH 3T (RESPONSI 1) ");
        System.out.println("   Pengembang: Ibrey Diba Tarigan (L0325027)         ");
        System.out.println("=====================================================");

        // Modul 2: Perulangan while untuk menu interaktif
        while (berjalan) {
            System.out.println("\n--- MENU OPERASIONAL LOGISTIK ---");
            System.out.println("1. Tambah Data Dus Bantuan");
            System.out.println("2. Lihat Daftar Muatan Sementara");
            System.out.println("3. Selesai dan Cetak Manifes Pengiriman");
            System.out.print("Pilih opsi menu (1-3): ");

            int pilihan = 0;
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine(); // Konsumsi newline
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Input pilihan menu harus berupa angka integer!");
                scanner.nextLine(); // Bersihkan buffer
                continue;
            }

            switch (pilihan) {
                case 1:
                    tambahBarang(scanner, daftarBarang);
                    break;
                case 2:
                    tampilkanDaftar(daftarBarang);
                    break;
                case 3:
                    if (daftarBarang.isEmpty()) {
                        System.out.println("[PERINGATAN] Muatan masih kosong! Masukkan minimal 1 barang sebelum mencetak manifes.");
                    } else {
                        prosesCetakManifes(daftarBarang);
                        berjalan = false; // Menghentikan perulangan (looping tidak infinite)
                    }
                    break;
                default:
                    System.out.println("[PERINGATAN] Opsi menu tidak valid. Silakan pilih 1, 2, atau 3.");
            }
        }

        scanner.close();
        System.out.println("\nProgram logistik selesai. Truk siap diberangkatkan ke lokasi 3T!");
    }

    // Method pembantu input data dengan penanganan Exception (Modul 3)
    private static void tambahBarang(Scanner scanner, ArrayList<BarangBantuan> daftarBarang) {
        boolean inputValid = false;

        while (!inputValid) {
            try {
                System.out.println("\n--- Form Pendataan Barang Bantuan ---");
                
                System.out.print("Masukkan Kode Barang (Wajib diawali 'EDX-'): ");
                String kodeBarang = scanner.nextLine().trim();

                // Validasi format kode barang diawali "EDX-" (Modul 3)
                if (!kodeBarang.startsWith("EDX-")) {
                    throw new IllegalArgumentException("Kode barang harus diawali dengan 'EDX-'!");
                }

                System.out.print("Masukkan Nama Barang (misal: Laptop Edge OS): ");
                String namaBarang = scanner.nextLine().trim();
                if (namaBarang.isEmpty()) {
                    throw new IllegalArgumentException("Nama barang tidak boleh dibiarkan kosong!");
                }

                System.out.print("Masukkan Kategori (Elektronik / Buku / Modul / Alat Peraga): ");
                String kategori = scanner.nextLine().trim();

                System.out.print("Masukkan Berat Barang dalam Kg: ");
                double beratKg = scanner.nextDouble();
                scanner.nextLine(); // Konsumsi newline

                // Validasi nilai berat barang tidak boleh <= 0 (Modul 3)
                if (beratKg <= 0) {
                    throw new BeratInvalidException("Berat barang tidak valid! Nilai harus lebih besar dari 0 kg (Ditemukan: " + beratKg + " kg).");
                }

                // Jika seluruh validasi lolos, buat objek dan masukkan ke ArrayList
                BarangBantuan barangBaru = new BarangBantuan(kodeBarang, namaBarang, kategori, beratKg);
                daftarBarang.add(barangBaru);
                System.out.println("[SUKSES] Barang \"" + namaBarang + "\" berhasil didaftarkan ke manifest muatan!");
                inputValid = true;

            } catch (IllegalArgumentException e) {
                System.out.println("[GAGAL INPUT] " + e.getMessage());
                System.out.println("-> Silakan ulangi pengisian data barang.");
            } catch (BeratInvalidException e) {
                System.out.println("[GAGAL INPUT] " + e.getMessage());
                System.out.println("-> Silakan ulangi pengisian data barang.");
            } catch (InputMismatchException e) {
                System.out.println("[GAGAL INPUT] Format input berat harus berupa angka numerik desimal/bulat!");
                scanner.nextLine(); // Bersihkan buffer input
                System.out.println("-> Silakan ulangi pengisian data barang.");
            } finally {
                // Blok finally menjamin kepastian eksekusi status pencatatan
                System.out.println("[STATUS] Validasi record logistik selesai diproses.");
            }
        }
    }

    // Method menampilkan isi ArrayList di konsol
    private static void tampilkanDaftar(ArrayList<BarangBantuan> daftarBarang) {
        System.out.println("\n================ DAFTAR MUATAN SAAT INI ================");
        if (daftarBarang.isEmpty()) {
            System.out.println("Belum ada barang bantuan yang didata di dalam posko.");
        } else {
            double totalSementara = 0;
            for (int i = 0; i < daftarBarang.size(); i++) {
                System.out.println((i + 1) + ". " + daftarBarang.get(i).toString());
                totalSementara += daftarBarang.get(i).getBeratKg();
            }
            System.out.printf("Total Muatan Terdata: %d unit | Akumulasi Berat: %.2f kg\n",
                    daftarBarang.size(), totalSementara);
        }
        System.out.println("========================================================");
    }

    // Method Persistensi File I/O & Logika Armada (Modul 2 & Modul 5)
    private static void prosesCetakManifes(ArrayList<BarangBantuan> daftarBarang) {
        int totalBarang = daftarBarang.size();
        double totalBerat = 0;

        for (BarangBantuan b : daftarBarang) {
            totalBerat += b.getBeratKg();
        }

        // Modul 2: Percabangan penentuan label armada ekspedisi
        String labelArmada;
        if (totalBerat <= 50.0) {
            labelArmada = "Mobil Pickup";
        } else {
            labelArmada = "Truk Ekspedisi";
        }

        // Tampilkan ringkasan di konsol
        System.out.println("\n================ REKAPITULASI PENGIRIMAN ================");
        System.out.println("Total Unit Bantuan : " + totalBarang + " dus");
        System.out.printf("Total Akumulasi Berat: %.2f kg\n", totalBerat);
        System.out.println("Rekomendasi Armada : " + labelArmada);
        System.out.println("=========================================================");

        // Modul 5: Menulis data ke file manifest_pengiriman.txt menggunakan BufferedWriter
        String namaFile = "manifest_pengiriman.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            writer.write("========================================================================\n");
            writer.write("           MANIFES RESMI PENGIRIMAN BANTUAN LOGISTIK PENDIDIKAN 3T      \n");
            writer.write("                 PROGRAM PENGABDIAN MASYARAKAT INFORMATIKA UNS          \n");
            writer.write("========================================================================\n");
            writer.write(String.format("Waktu Pembuatan Dokumen : Manifes Otomatis Sistem Logistik\n"));
            writer.write(String.format("Penanggung Jawab Lapangan: Ibrey Diba Tarigan (NIM: L0325027)\n"));
            writer.write("------------------------------------------------------------------------\n");
            writer.write(String.format("%-5s | %-12s | %-26s | %-12s | %-10s\n", 
                    "NO", "KODE", "NAMA BARANG", "KATEGORI", "BERAT (KG)"));
            writer.write("------------------------------------------------------------------------\n");

            int no = 1;
            for (BarangBantuan item : daftarBarang) {
                writer.write(String.format("%-5d | %-12s | %-26s | %-12s | %8.2f\n",
                        no++,
                        item.getKodeBarang(),
                        item.getNamaBarang(),
                        item.getKategori(),
                        item.getBeratKg()));
            }

            writer.write("========================================================================\n");
            writer.write(String.format("TOTAL BARANG TERDAFTAR : %d Dus Bantuan\n", totalBarang));
            writer.write(String.format("TOTAL BERAT AKUMULASI  : %.2f Kg\n", totalBerat));
            writer.write(String.format("LABEL ARMADA ANGKUT    : %s\n", labelArmada));
            writer.write("STATUS VERIFIKASI      : SIAP DIBERANGKATKAN KE DAERAH 3T\n");
            writer.write("========================================================================\n");

            // Tampilkan notifikasi persis seperti spesifikasi modul
            System.out.println("[INFORMASI] Data manifes berhasil diekspor ke manifest_pengiriman.txt");

        } catch (IOException e) {
            System.out.println("[ERROR FILE I/O] Terjadi kesalahan saat menulis file: " + e.getMessage());
        }
    }
}