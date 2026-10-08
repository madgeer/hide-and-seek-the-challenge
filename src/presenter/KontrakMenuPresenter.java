package presenter;

/**
 * Interface Kontrak untuk Presenter Menu.
 * Interface ini mendefinisikan metode-metode yang HARUS diimplementasikan oleh MenuPresenter.
 * View (TampilMenu) akan menggunakan interface ini untuk memerintah Presenter
 * tanpa perlu tahu detail implementasi logikanya.
 */
public interface KontrakMenuPresenter {

    /**
     * Method method yang harus di implementasikan di MenuPresenter
     */

    /**
     * Memerintahkan Presenter untuk mengambil data High Score dari database
     * dan mengirimkannya kembali ke View agar bisa ditampilkan di tabel.
     */
    void muatDataSkor();

    /**
     * Memerintahkan Presenter untuk memulai permainan baru.
     * Presenter akan melakukan:
     * 1. Validasi data user.
     * 2. Mengambil data sisa peluru (jika user lama).
     * 3. Membuka jendela Game dan menutup Menu.
     *
     * @param username Nama pemain yang diinputkan di menu.
     */
    void mulaiGame(String username);
}