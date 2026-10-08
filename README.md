# HIDE AND SEEK THE CHALLENGE  - TMD DPBO 2024/2025

**HIDE AND SEEK THE CHALLENGE** adalah permainan tembak-menembak 2D Dimana Pemain harus bertahan hidup dari serangan alien, menghindari rintangan batu, dan mengumpulkan skor tertinggi.

Proyek ini dibuat sebagai pemenuhan **Tugas Masa Depan (TMD)** mata kuliah **Desain Pemrograman Berorientasi Objek (DPBO)**.

## Janji
Saya Muhammad Rizkiana Pratama dengan NIM 2404421 mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah 
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya 
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

---

## Arsitektur Program (MVP)
Game ini dibangun menggunakan pola desain **Model-View-Presenter (MVP)**.

1.  **Model:** Mengurus data dan logika bisnis (Database, Entity Player, Alien, Logika Fisika).
2.  **View:** Mengurus tampilan antarmuka (GUI) dan input user (Keyboard). Tidak mengandung logika bisnis.
3.  **Presenter:** Bertindak sebagai perantara (jembatan). Menerima input dari View, memprosesnya di Model, dan mengembalikan hasilnya ke View.

---

## 🎮 Fitur Utama
* **Sistem Login/Username:** Menyimpan progres permainan berdasarkan nama pengguna.
* **Database Integration:** Menyimpan skor tertinggi, jumlah peluru meleset, dan sisa peluru (MySQL).
* **Gameplay Mekanik:**
    * Pergerakan 4 arah (Atas, Bawah, Kiri, Kanan).
    * Menembak ke 4 arah.
    * Musuh (Alien) dengan pergerakan dan tembakan otomatis.
* **Sistem Survival:**
    * Peluru terbatas.
    * Peluru bertambah jika alien menembak dan meleset.
    * Simpan posisi amunisi terakhir (Survival Mode).
* **Audio:** Efek suara (SFX) dan Musik Latar (BGM).

---

## Prasyarat (Requirements)
Sebelum menjalankan program, pastikan komputer Anda memiliki:
1.  **Java Development Kit (JDK)** (minimal versi 8).
2.  **MySQL Server** (XAMPP/WAMP/MAMP). (pastikan sudah jalan)
3.  **Library:** `mysql-connector-j-9.4.0.jar` 

---

## Konfigurasi Database
1.  Buka phpMyAdmin atau MySQL Console.
2.  Buat database baru dengan nama `hide_and_seek_the_challenge` (atau sesuaikan dengan konfigurasi di `DB.java`).
3.  Import atau jalankan query SQL berikut:

## Cara Menjalankan
```
javac -d out src/model/*.java src/view/*.java src/presenter/*.java src/main/*.java
java -cp "out;src/lib/mysql-connector-j-9.4.0.jar" main.Main
```

```sql
CREATE TABLE tbenefit (
    username VARCHAR(255) PRIMARY KEY,
    skor INT DEFAULT 0,
    peluru_meleset INT DEFAULT 0,
    sisa_peluru INT DEFAULT 50
);
```

## Kredit
- Alien(208 x 208).png : OpenGameArt.ORG Korba
- explosionCrunch_000.wav: kenney.nl
- laserSmall_001.wav: kenney.nl
- Mars(512 x 512).png : OpenGameArt.ORG  FunwithPixels
- Rock(160 x 160).png : OpenGameArt.ORG  mafon2
- Soldier(100 x 100).png : OpenGameArt.ORG z11z11
- spaceEngine_000.wav : kenney.nl
