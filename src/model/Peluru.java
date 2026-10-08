package model;

/**
 * Merepresentasikan proyektil atau peluru yang ditembakkan di dalam permainan.
 * Kelas ini menangani vektor pergerakan peluru dan identifikasi sumber tembakan
 * (apakah berasal dari musuh atau pemain).
 */
public class Peluru extends Entitas {

    /**
     * Kecepatan pergerakan peluru pada sumbu horizontal (sumbu X).
     * Nilai positif bergerak ke kanan, nilai negatif bergerak ke kiri.
     */
    public double horizontalVelocity;

    /**
     * Kecepatan pergerakan peluru pada sumbu vertikal (sumbu Y).
     * Nilai positif bergerak ke bawah, nilai negatif bergerak ke atas.
     */
    public double verticalVelocity;

    /**
     * Menandakan sumber asal peluru untuk logika deteksi tabrakan (friendly fire).
     * Bernilai true jika ditembakkan oleh Alien (musuh), dan false jika oleh pemain.
     */
    public boolean fromAlien;

    /**
     * Mengonstruksi objek Peluru baru dengan posisi awal, kecepatan vektor, dan sumber penembak.
     *
     * @param xPosition          posisi awal horizontal peluru saat muncul
     * @param yPosition          posisi awal vertikal peluru saat muncul
     * @param horizontalVelocity kecepatan gerak peluru pada arah horizontal
     * @param verticalVelocity   kecepatan gerak peluru pada arah vertikal
     * @param fromAlien          status yang menentukan apakah peluru ini milik musuh (true) atau pemain (false)
     */
    public Peluru(int xPosition, int yPosition, double horizontalVelocity, double verticalVelocity, boolean fromAlien) {
        // Memanggil konstruktor superclass (Entitas)
        // Menetapkan ukuran standar peluru menjadi 10x10 piksel
        super(xPosition, yPosition, 10, 10);

        this.horizontalVelocity = horizontalVelocity;
        this.verticalVelocity = verticalVelocity;
        this.fromAlien = fromAlien;
    }
}