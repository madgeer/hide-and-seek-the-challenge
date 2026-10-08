package view;

/**
 * Interface yang mendefinisikan kontrak perilaku untuk tampilan (View) permainan.
 * Interface ini diimplementasikan oleh kelas GUI (misalnya JFrame/JPanel) agar
 * Presenter dapat mengontrol visibilitas jendela permainan tanpa bergantung
 * pada komponen Swing secara langsung.
 */
public interface KontrakViewGame {

    /**
     * Menampilkan jendela permainan ke layar pengguna.
     * Metode ini biasanya mengimplementasikan logika `setVisible(true)` 
     * atau inisialisasi komponen grafis awal.
     */
    void tampil();

    /**
     * Menutup jendela permainan saat ini (dispose).
     * Metode ini dipanggil oleh Presenter saat pemain memilih untuk kembali ke menu
     * atau saat permainan di-reset total.
     */
    void tutupGame();
}