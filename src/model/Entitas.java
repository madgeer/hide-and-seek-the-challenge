package model;

import java.awt.Rectangle;

/**
 * Merepresentasikan entitas umum di dalam sistem.
 * Kelas ini menyimpan posisi dan dimensi suatu objek serta menyediakan
 * metode utilitas untuk kebutuhan geometri dan deteksi tabrakan (collision).
 */

/**
 * superclass Entitas yang merepresentasikan entitas umum di dalam sistem.
 */
public class Entitas {

    /** * Koordinat horizontal dari posisi entitas.
     */
    public int x;

    /** * Koordinat vertikal dari posisi entitas.
     */
    public int y;

    /** * Lebar entitas dalam satuan piksel.
     */
    public int width;

    /** * Tinggi entitas dalam satuan piksel.
     */
    public int height;

    /**
     * Mengonstruksi Entitas baru dengan koordinat dan dimensi tertentu.
     * * @param xPosition posisi awal horizontal dari entitas
     * @param yPosition posisi awal vertikal dari entitas
     * @param width     lebar dari entitas
     * @param height    tinggi dari entitas
     */
    public Entitas(int xPosition, int yPosition, int width, int height) {
        // Menginisialisasi posisi dan ukuran berdasarkan argumen konstruktor
        this.x = xPosition;
        this.y = yPosition;
        this.width = width;
        this.height = height;
    }

    /**
     * Membuat dan mengembalikan objek Rectangle yang mewakili batas luar entitas.
     * Metode ini digunakan untuk logika deteksi tabrakan.
     *
     * @return objek Rectangle baru yang memuat data x, y, lebar, dan tinggi entitas
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}