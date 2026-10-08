package model;

/**
 * Merepresentasikan karakter pemain (Player) yang dikendalikan oleh pengguna.
 * Kelas ini memperluas kelas Entitas dan menyimpan status permainan spesifik
 * seperti jumlah amunisi, skor, dan status kehidupan pemain.
 */
public class Player extends Entitas {

    /**
     * Jumlah amunisi yang tersedia bagi pemain saat ini.
     * Diinisialisasi dengan nilai 20 sebagai modal awal permainan.
     */
    public int ammo = 0;

    /**
     * Skor akumulatif yang diperoleh pemain selama permainan berlangsung.
     * Nilai awal diatur ke 0.
     */
    public int score = 0;

    /**
     * Menandakan apakah pemain masih hidup atau sudah mati.
     * Bernilai true jika pemain aktif, dan false jika pemain telah kalah.
     */
    public boolean alive = true;

    /**
     * Mengonstruksi objek Player baru.
     * Konstruktor ini menetapkan posisi awal pemain pada koordinat tertentu
     * dan mendefinisikan dimensi (lebar dan tinggi) dari karakter pemain.
     */
    public Player() {
        // Memanggil konstruktor superclass (Entitas)
        // Menetapkan posisi awal di x=380, y=500
        // Menetapkan ukuran pemain dengan lebar=30 dan tinggi=30 piksel
        super(380, 280, 45, 45);
    }
}