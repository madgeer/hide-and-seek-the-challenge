package model;

/**
 * Merepresentasikan objek rintangan berupa batu di dalam permainan.
 * Objek ini bersifat statis (tidak bergerak) dan berfungsi sebagai penghalang
 * yang dapat melindungi pemain atau musuh dari tembakan.
 */
public class Batu extends Entitas {

    /**
     * Mengonstruksi objek Batu baru pada lokasi tertentu.
     * Ukuran batu ditetapkan secara standar menjadi 50x50 piksel.
     *
     * @param xPosition posisi horizontal (koordinat X) tempat batu diletakkan
     * @param yPosition posisi vertikal (koordinat Y) tempat batu diletakkan
     */
    public Batu(int xPosition, int yPosition) {
        // Memanggil konstruktor superclass (Entitas)
        // Menetapkan dimensi batu: lebar=50, tinggi=50
        super(xPosition, yPosition, 50, 50);
    }
}