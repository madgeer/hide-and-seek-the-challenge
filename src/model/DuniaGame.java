package model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Kelas utama yang merepresentasikan "Dunia" atau status permainan saat ini.
 * Kelas ini bertindak sebagai wadah (container) untuk semua objek permainan
 * seperti pemain, musuh, rintangan, dan proyektil, serta menyimpan data sesi
 * pengguna.
 */
public class DuniaGame {

    /**
     * Objek pemain utama yang dikendalikan oleh pengguna.
     */
    public Player player;

    /**
     * Daftar yang menampung semua objek Alien (musuh) yang masih hidup.
     * Digunakan untuk iterasi saat rendering dan deteksi tabrakan.
     */
    public List<Alien> alienList;

    /**
     * Daftar yang menampung semua objek Batu (rintangan) di level ini.
     * Objek dalam daftar ini bersifat statis atau penghalang.
     */
    public List<Batu> batuList;

    /**
     * Daftar proyektil (peluru) yang sedang aktif bergerak di layar.
     * Termasuk peluru dari pemain maupun musuh.
     */
    public List<Peluru> peluruList;

    /**
     * Penghitung jumlah tembakan pemain yang gagal mengenai sasaran.
     * Variabel ini dapat digunakan untuk kalkulasi akurasi di akhir permainan.
     */
    public int missedShotCount = 0;

    /**
     * Nama pengguna (username) dari pemain yang sedang aktif.
     * Data ini diambil dari input pengguna atau database saat inisialisasi.
     */
    public String currentUsername;

    /**
     * Mengonstruksi dunia permainan baru dan mempersiapkan level awal.
     *
     * @param username     nama pengguna yang akan diasosiasikan dengan sesi ini
     * @param startingAmmo jumlah amunisi awal yang dimuat dari database atau
     *                     konfigurasi
     */
    public DuniaGame(String username, int startingAmmo, int initislScore, int initialMissed) {
        this.currentUsername = username;
        this.missedShotCount = initialMissed;
        // Inisialisasi pemain dan tetapkan amunisi sesuai data persisten (DB)
        player = new Player();
        player.ammo = startingAmmo;
        player.score = initislScore;

        // Inisialisasi daftar kosong untuk mencegah NullPointerException saat akses
        // pertama
        alienList = new ArrayList<>();
        batuList = new ArrayList<>();
        peluruList = new ArrayList<>();

        // Memuat objek-objek level (musuh dan rintangan)
        setupLevel();
    }

    /**
     * Mengatur tata letak level.
     * MODIFIKASI: Posisi Batu di-generate secara acak (Random).
     */
    public void setupLevel() {
        // Bersihkan daftar lama (jika ada reset)
        batuList.clear();
        alienList.clear();

        Random rand = new Random();

        // --- 1. GENERATE BATU ACAK ---
        // Tentukan jumlah batu, misalnya antara 4 sampai 6 buah
        int jumlahBatu = 4 + rand.nextInt(3);

        // Tentukan area aman pemain (Player biasanya start di 380, 500)
        // Kita buat area di sekitar player agar batu tidak spawn di sana
        Rectangle playerSafeZone = new Rectangle(300, 450, 200, 150);

        for (int i = 0; i < jumlahBatu; i++) {
            int x, y;
            boolean posisiAman;
            int attempts = 0;

            do {
                posisiAman = true;
                // Random X antara 50 - 700 (agar tidak terlalu pinggir)
                x = rand.nextInt(650) + 50;

                // Random Y antara 100 - 400 (Area tengah, jangan terlalu atas/bawah)
                y = rand.nextInt(200) + 100;

                Rectangle calonBatu = new Rectangle(x, y, 50, 50);

                // Cek: Jangan menimpa Player
                if (calonBatu.intersects(playerSafeZone)) {
                    posisiAman = false;
                }

                // Cek: Jangan menumpuk dengan batu lain yang sudah ada
                for (Batu b : batuList) {
                    if (calonBatu.intersects(b.getBounds())) {
                        posisiAman = false;
                        break;
                    }
                }

                attempts++;
            } while (!posisiAman && attempts < 100); // Coba max 100x agar tidak infinite loop

            if (posisiAman) {
                batuList.add(new Batu(x, y));
            }
        }

        // --- 2. GENERATE ALIEN ---
        // Alien bisa tetap statis atau juga dirandom jika diinginkan.
        // Di sini kita pastikan Alien tidak menabrak batu yang baru dibuat.
        spawnSafeAlien(50, 500);
        spawnSafeAlien(700, 500);
        spawnSafeAlien(375, 480);
    }

    /**
     * Helper untuk menambahkan alien hanya jika posisinya tidak tertutup batu.
     * Jika tertutup batu, geser sedikit atau batalkan.
     */
    private void spawnSafeAlien(int x, int y) {
        Rectangle calonAlien = new Rectangle(x, y, 45, 45);
        boolean kenaBatu = false;

        // Cek tabrakan dengan semua batu
        for (Batu b : batuList) {
            if (calonAlien.intersects(b.getBounds())) {
                kenaBatu = true;
                break;
            }
        }

        // Jika kena batu, geser alien ke atas sedikit
        if (kenaBatu) {
            y -= 60;
        }

        alienList.add(new Alien(x, y));
    }
}