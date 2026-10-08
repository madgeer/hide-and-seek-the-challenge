package model;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Kelas Helper (Utility) untuk menangani pemutaran audio (BGM dan SFX).
 * Kelas ini menggunakan library standar Java Sound API (javax.sound.sampled).
 * * Syarat: File audio HARUS berformat .wav
 */
public class AudioHelper {

    // Lokasi folder aset audio (Pastikan folder 'assets' ada di root project)
    private static final String PATH = "assets/";

    /**
     * Memutar Sound Effect (SFX) sekali main (One-shot).
     * Contoh penggunaan: Suara tembakan, ledakan, tombol klik.
     *
     * @param filename Nama file audio (contoh: "shoot.wav").
     */
    public static void playSound(String filename) {
        try {
            File f = new File(PATH + filename);
            // Cek apakah file ada
            if (!f.exists()) {
                System.err.println("File audio tidak ditemukan: " + filename);
                return;
            }

            // Membuka stream audio
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Mulai memutar
            clip.start();

        } catch (Exception e) {
            System.err.println("Gagal memutar sound effect: " + e.getMessage());
        }
    }

    /**
     * Memutar Musik Latar (BGM) secara berulang (Looping).
     * Metode ini mengembalikan objek Clip agar musik bisa dihentikan nanti.
     *
     * @param filename Nama file audio (contoh: "bgm.wav").
     * @return Objek Clip yang sedang berjalan (atau null jika gagal).
     */
    public static Clip playMusic(String filename) {
        try {
            File f = new File(PATH + filename);
            if (!f.exists()) {
                System.err.println("File musik tidak ditemukan: " + filename);
                return null;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Atur agar looping terus menerus
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Mulai memutar
            clip.start();

            return clip; // Kembalikan referensi clip agar bisa distop

        } catch (Exception e) {
            System.err.println("Gagal memutar musik: " + e.getMessage());
            return null;
        }
    }

    /**
     * Menghentikan musik yang sedang berjalan.
     *
     * @param clip Objek Clip yang ingin dimatikan (didapat dari return playMusic).
     */
    public static void stopMusic(Clip clip) {
        if (clip != null) {
            // Jika sedang main, stop dulu
            if (clip.isRunning()) {
                clip.stop();
            }
            // Tutup resource untuk menghemat memori
            clip.close();
        }
    }
}