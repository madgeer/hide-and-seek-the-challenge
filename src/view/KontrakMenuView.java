package view;

/**
 * Interface Kontrak untuk View Menu (TampilMenu).
 * Interface ini mendefinisikan metode-metode yang HARUS diimplementasikan oleh TampilMenu
 * agar bisa dikontrol oleh MenuPresenter.
 * * Tujuannya adalah memisahkan logic tampilan dari logic bisnis (Decoupling).
 */
public interface KontrakMenuView {

    /**
     * Method method yang harus di implementasikan di MenuView
     */

    /**
     * Meminta View untuk memperbarui isi tabel High Score dengan data terbaru.
     * Data diterima dalam bentuk Array 2D (Object[][]) agar View tidak perlu 
     * berurusan dengan objek database kompleks seperti ResultSet.
     *
     * @param dataSkor Array 2D berisi baris data (username, skor, meleset, sisa peluru).
     */
    void updateTabelSkor(Object[][] dataSkor);

    /**
     * Menampilkan pesan error atau peringatan kepada pengguna.
     * Biasanya diimplementasikan menggunakan JOptionPane.
     *
     * @param pesan Isi pesan error yang ingin ditampilkan.
     */
    void tampilkanError(String pesan);

    /**
     * Menutup jendela Menu Utama.
     * Metode ini dipanggil oleh Presenter saat permainan berhasil dimulai
     * dan transisi ke jendela Game dilakukan.
     */
    void tutupMenu();
}