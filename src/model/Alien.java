package model;

/**
 * Merepresentasikan musuh atau "Alien" yang akan dilawan oleh pemain.
 */
public class Alien extends Entitas {

    /**
     * Kecepatan pergerakan alien pada sumbu horizontal (X).
     * Nilai default adalah 2 piksel per frame.
     */
    public int horizontalVelocity = 2;

    /**
     * Kecepatan pergerakan alien pada sumbu vertikal (Y).
     * Nilai default adalah 2 piksel per frame.
     * Variabel ini memungkinkan alien bergerak naik-turun atau berpindah baris.
     */
    public int verticalVelocity = 2;

    /**
     * Mengonstruksi objek Alien baru pada posisi tertentu.
     * Ukuran alien ditetapkan secara standar menjadi 30x30 piksel.
     *
     * @param xPosition posisi horizontal awal (koordinat X) dari alien
     * @param yPosition posisi vertikal awal (koordinat Y) dari alien
     */
    public Alien(int xPosition, int yPosition) {
        // Memanggil konstruktor superclass (Entitas)
        // Menetapkan dimensi alien: lebar=30, tinggi=30
        super(xPosition, yPosition, 45, 45);
    }
}