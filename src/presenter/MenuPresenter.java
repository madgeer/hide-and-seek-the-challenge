package presenter;

import model.TabelBenefit;
import view.KontrakMenuView;
import view.ViewGame;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Presenter yang menangani logika bisnis untuk Menu Utama.
 * Bertanggung jawab untuk:
 * 1. Mengambil data High Score dari Database untuk ditampilkan di View.
 * 2. Menangani logika ketika tombol "Play" ditekan (Start Game).
 * 3. Menginisialisasi Game Loop dan transisi dari Menu ke Gameplay.
 */
public class MenuPresenter implements KontrakMenuPresenter {

    // Referensi ke Interface View Menu (untuk komunikasi balik ke TampilMenu)
    private KontrakMenuView menuView;

    /**
     * Konstruktor MenuPresenter.
     * @param menuView Objek tampilan menu yang mengimplementasikan KontrakMenuView.
     */
    public MenuPresenter(KontrakMenuView menuView) {
        this.menuView = menuView;
    }

    @Override
    public void muatDataSkor() {
        try {
            // 1. Ambil data dari Database
            TabelBenefit benefitDAO = new TabelBenefit();
            benefitDAO.getBenefitData(); // Eksekusi query SELECT
            ResultSet rs = benefitDAO.getResult();
            
            // 2. Konversi ResultSet ke Object[][]
            // Tujuannya agar View (TampilMenu) menerima data mentah dan TIDAK perlu mengimport java.sql.*
            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getInt("skor"),
                    rs.getInt("peluru_meleset"),
                    rs.getInt("sisa_peluru")
                });
            }
            benefitDAO.closeResult(); // Tutup koneksi DB
            
            // 3. Kirim data yang sudah matang ke View untuk ditampilkan di JTable
            Object[][] dataArray = rows.toArray(new Object[0][]);
            menuView.updateTabelSkor(dataArray);
            
        } catch (Exception e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
        }
    }

    @Override
    public void mulaiGame(String username) {
        try {
            // --- LANGKAH 1: Ambil Data User ---
            // Cek apakah user ini punya sisa peluru dari permainan sebelumnya
            TabelBenefit benefitDAO = new TabelBenefit();
            int initialAmmo = benefitDAO.getSisaPeluru(username);
            int initialScore = benefitDAO.getSkorTerakhir(username);
            int initialMissed = benefitDAO.getMelesetTerakhir(username);
            benefitDAO.closeResult();

            // --- LANGKAH 2: Siapkan Komponen MVP Game ---
            // Buat View Game
            ViewGame gameView = new ViewGame();
            
            // Buat Presenter Game (Logic) dengan menyuntikkan data user & ammo
            GamePresenter gamePresenter = new GamePresenter(username, initialAmmo, initialScore, initialMissed);
            
            // Hubungkan View Game dengan Presenter Game
            gameView.setProsesGame(gamePresenter);

            // --- LANGKAH 3: Setup Jendela Game (JFrame) ---
            JFrame gameWindow = new JFrame("HIDE AND SEEK THE CHALLENGE - Gameplay");
            gameWindow.setSize(800, 600);
            gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameWindow.setResizable(false);
            gameWindow.setLocationRelativeTo(null); // Tengah layar
            gameWindow.add(gameView);
            gameWindow.setVisible(true);
            
            // Fokuskan input ke panel game agar keyboard langsung bisa dipakai
            gameView.requestFocusInWindow();

            // --- LANGKAH 4: Jalankan Game Loop (Timer) ---
            // Timer berjalan setiap 16ms (~60 FPS)
            Timer gameLoop = new Timer(16, e -> {
                // Safety Check: Jika window game ditutup, matikan timer
                if (!gameWindow.isDisplayable()) {
                    ((Timer)e.getSource()).stop();
                    return;
                }
                // Update Logika Fisika
                gamePresenter.prosesDataGame();
                // Render Ulang Layar
                gameView.tampil();
            });
            gameLoop.start();
            
            // --- LANGKAH 5: Tutup Menu Utama ---
            // Perintahkan View Menu untuk menutup dirinya sendiri
            menuView.tutupMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}