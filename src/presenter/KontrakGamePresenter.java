package presenter;

import model.Alien;
import model.Batu;
import model.Peluru;
import model.Player;
import view.KontrakViewGame;
import java.util.List;

/**
 * Interface yang mendefinisikan kontrak komunikasi antara View (Tampilan) dan Presenter.
 * Interface ini memastikan bahwa View hanya dapat memanggil metode yang telah disepakati
 * untuk mengontrol logika permainan, tanpa memanipulasi data Model secara langsung.
 */
public interface KontrakGamePresenter {

    /**
     * Method method yang harus di implementasikan di GamePresenter
     */

    /**
     * Menghubungkan objek View ke Presenter.
     * Metode ini memungkinkan Presenter untuk mengirim perintah balik ke View,
     * seperti menutup jendela permainan atau menampilkan pesan.
     *
     * @param view objek yang mengimplementasikan KontrakView (Tampilan Game)
     */
    void setView(KontrakViewGame view);

    /**
     * Memproses satu putaran logika permainan (Game Loop).
     * Metode ini akan memperbarui posisi semua entitas (Player, Alien, Peluru),
     * mendeteksi tabrakan, dan menangani aturan fisika permainan.
     */
    void prosesDataGame();

    // -----------------------------------------------------------
    // BAGIAN: INPUT PENGGUNA (GERAKAN)
    // -----------------------------------------------------------

    /**
     * Mengatur status pergerakan pemain ke arah kiri.
     *
     * @param isMoving bernilai true jika tombol ditekan (gerak), false jika dilepas (berhenti)
     */
    void setGerakKiri(boolean isMoving);

    /**
     * Mengatur status pergerakan pemain ke arah kanan.
     *
     * @param isMoving bernilai true jika tombol ditekan (gerak), false jika dilepas (berhenti)
     */
    void setGerakKanan(boolean isMoving);

    /**
     * Mengatur status pergerakan pemain ke arah atas.
     *
     * @param isMoving bernilai true jika tombol ditekan (gerak), false jika dilepas (berhenti)
     */
    void setGerakAtas(boolean isMoving);

    /**
     * Mengatur status pergerakan pemain ke arah bawah.
     *
     * @param isMoving bernilai true jika tombol ditekan (gerak), false jika dilepas (berhenti)
     */
    void setGerakBawah(boolean isMoving);

    // -----------------------------------------------------------
    // BAGIAN: AKSI PERMAINAN
    // -----------------------------------------------------------

    /**
     * Memicu aksi penembakan peluru oleh pemain.
     * Logika ini akan mengecek ketersediaan amunisi sebelum membuat objek peluru baru.
     */
    void tembakPlayer(int dx, int dy);

    /**
     * Mereset permainan ke kondisi awal.
     * Skor akan disimpan, namun posisi pemain dan musuh akan dikembalikan ke posisi start.
     */
    void resetGame();

    // -----------------------------------------------------------
    // BAGIAN: KONTROL ALUR (PAUSE & MENU)
    // -----------------------------------------------------------

    /**
     * Mengubah status permainan antara berjalan (Play) dan berhenti sementara (Pause).
     */
    void togglePause();

    /**
     * Memeriksa apakah permainan sedang dalam kondisi pause.
     *
     * @return true jika permainan sedang dipause, false jika sedang berjalan
     */
    boolean isPaused();

    /**
     * Menyimpan data permainan saat ini dan menginstruksikan View
     * untuk menutup layar permainan dan kembali ke menu utama.
     */
    void kembaliKeMenu();

    // -----------------------------------------------------------
    // BAGIAN: PENGAMBILAN DATA (GETTERS)
    // Digunakan oleh View untuk menggambar (rendering) objek
    // -----------------------------------------------------------

    /**
     * Mengambil objek Player untuk keperluan rendering posisi dan status.
     *
     * @return objek Player aktif
     */
    Player getPlayer();

    /**
     * Mengambil daftar semua musuh (Alien) yang ada di area permainan.
     *
     * @return List berisi objek Alien
     */
    List<Alien> getListAlien();

    /**
     * Mengambil daftar semua rintangan (Batu) yang ada di level.
     *
     * @return List berisi objek Batu
     */
    List<Batu> getListBatu();

    /**
     * Mengambil daftar semua proyektil (Peluru) yang sedang melayang.
     *
     * @return List berisi objek Peluru
     */
    List<Peluru> getListPeluru();

    /**
     * Mengambil jumlah tembakan yang meleset untuk statistik akurasi.
     *
     * @return jumlah peluru yang keluar layar tanpa mengenai target
     */
    int getPeluruMeleset();
}