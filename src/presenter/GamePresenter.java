package presenter;

import model.Alien;
import model.Batu;
import model.DuniaGame;
import model.Peluru;
import model.Player;
import model.TabelBenefit;
import view.KontrakViewGame;
import model.AudioHelper;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Kelas Presenter yang bertindak sebagai pengendali logika utama permainan
 * (Game Loop).
 * Kelas ini menghubungkan Model (DuniaGame) dan View (TampilGame), serta
 * menangani
 * seluruh mekanisme permainan mulai dari pergerakan, fisika, hingga manajemen
 * data.
 */
public class GamePresenter implements KontrakGamePresenter {

    private DuniaGame gameWorld; // Model data permainan
    private KontrakViewGame gameView; // Interface untuk komunikasi ke View

    // Status input keyboard (Flags)
    private boolean isMovingLeft, isMovingRight, isMovingUp, isMovingDown;

    // Status apakah permainan sedang dihentikan sementara (Pause)
    private boolean isGamePaused = false;

    // Data sesi pemain saat ini
    private String currentUsername;
    private int initialAmmo;

    /**
     * Konstruktor GamePresenter.
     * Menginisialisasi dunia permainan berdasarkan data yang diterima dari Menu
     * Utama.
     * * @param username Nama pengguna pemain.
     * 
     * @param initialAmmo   Jumlah amunisi awal (dari database).
     * @param initialScore  Skor awal (akumulasi).
     * @param initialMissed Jumlah tembakan meleset (akumulasi).
     */
    public GamePresenter(String username, int initialAmmo, int initialScore, int initialMissed) {
        this.currentUsername = username;
        this.initialAmmo = initialAmmo;
        // Membentuk objek DuniaGame dengan data akumulatif
        this.gameWorld = new DuniaGame(username, initialAmmo, initialScore, initialMissed);
    }

    @Override
    public void setView(KontrakViewGame view) {
        this.gameView = view;
    }

    // ==========================================
    // BAGIAN MANAJEMEN DATA & MENU
    // ==========================================

    /**
     * Menyimpan progres permainan saat ini ke dalam database.
     * Metode ini dipanggil saat permainan berakhir, dipause, atau direset.
     */
    private void saveToDatabase() {
        try {
            TabelBenefit benefitTable = new TabelBenefit();
            benefitTable.saveGameData(
                    gameWorld.currentUsername,
                    gameWorld.player.score,
                    gameWorld.missedShotCount,
                    gameWorld.player.ammo);
            benefitTable.closeResult();
            System.out.println("Data pemain " + gameWorld.currentUsername + " berhasil disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Kembalikan ke menu utama dengan menyimpan data terlebih dahulu.
     */
    @Override
    public void kembaliKeMenu() {
        // Simpan data terlebih dahulu untuk mencegah kehilangan progres
        saveToDatabase();

        // Menutup jendela permainan melalui View
        if (gameView != null) {
            gameView.tutupGame();
        }
    }

    /**
     * Toggles status pause permainan.
     * Jika pemain sudah mati, tombol ini berfungsi untuk kembali ke menu utama.
     */
    @Override
    public void togglePause() {
        // Jika pemain sudah mati, tombol ini berfungsi untuk kembali ke menu utama
        if (!gameWorld.player.alive) {
            kembaliKeMenu();
        } else {
            // Jika pemain masih hidup, alihkan status pause/resume
            isGamePaused = !isGamePaused;
        }
    }

    /** 
     * Mereset permainan ke kondisi awal tanpa menutup jendela.
     * Skor, peluru meleset, dan sisa peluru akan tetap terakumulasi.
     */
    @Override
    public void resetGame() {
        // 1. Simpan data skor dan statistik terkini ke database
        saveToDatabase();

        // 2. [MODE HARDCORE]
        // Menggunakan sisa amunisi terakhir untuk permainan baru.
        // Jika amunisi habis (0), pemain harus bertahan hidup tanpa amunisi awal.
        int currentAmmo = gameWorld.player.ammo;

        // Mengambil data skor dan statistik meleset agar tetap terakumulasi
        int currentScore = gameWorld.player.score;
        int currentMissed = gameWorld.missedShotCount;

        // Memperbarui variabel global untuk sinkronisasi data
        this.initialAmmo = currentAmmo;

        // 3. Menginisialisasi ulang objek DuniaGame dengan parameter terbaru
        this.gameWorld = new DuniaGame(currentUsername, currentAmmo, currentScore, currentMissed);

        // 4. Mengatur ulang status input dan pause
        isMovingLeft = isMovingRight = isMovingUp = isMovingDown = false;
        isGamePaused = false;
    }

    // ==========================================
    // BAGIAN GAME LOOP (LOGIKA UTAMA)
    // ==========================================

    /**
     * Memproses logika permainan setiap frame.
     * Metode ini dipanggil secara berkala oleh Timer di View.
     */
    @Override
    public void prosesDataGame() {
        // Hentikan pemrosesan logika jika pemain mati atau game sedang dipause
        if (!gameWorld.player.alive || isGamePaused) {
            return;
        }

        // Perbarui status seluruh entitas dalam permainan
        updatePlayerMovement();
        updateAliens();
        updateBullets();
    }

    /**
     * Memperbarui posisi pemain berdasarkan input keyboard.
     * Melakukan pengecekan tabrakan dengan batu sebelum memindahkan posisi.
     */
    private void updatePlayerMovement() {
        int speed = 5; // Kecepatan gerak pemain

        // Periksa setiap arah gerakan, validasi tabrakan dengan batu
        if (isMovingLeft && !checkRockCollision(gameWorld.player.x - speed, gameWorld.player.y)) {
            gameWorld.player.x -= speed;
        }
        if (isMovingRight && !checkRockCollision(gameWorld.player.x + speed, gameWorld.player.y)) {
            gameWorld.player.x += speed;
        }
        if (isMovingUp && !checkRockCollision(gameWorld.player.x, gameWorld.player.y - speed)) {
            gameWorld.player.y -= speed;
        }
        if (isMovingDown && !checkRockCollision(gameWorld.player.x, gameWorld.player.y + speed)) {
            gameWorld.player.y += speed;
        }

        // Membatasi posisi pemain agar tidak keluar dari area layar (Clamping)
        if (gameWorld.player.x < 0)
            gameWorld.player.x = 0;
        if (gameWorld.player.y < 0)
            gameWorld.player.y = 0;
        if (gameWorld.player.x > 770)
            gameWorld.player.x = 770;
        if (gameWorld.player.y > 560)
            gameWorld.player.y = 560;
    }

    /**
     * Mendeteksi tabrakan antara pemain dan batu menggunakan logika "Forgiving
     * Hitbox".
     * Ukuran hitbox batu dikurangi secara internal untuk memberikan toleransi
     * gerak.
     *
     * @param x Koordinat X prediksi pemain.
     * @param y Koordinat Y prediksi pemain.
     * @return True jika terjadi tabrakan, False jika aman.
     */
    private boolean checkRockCollision(int x, int y) {
        // Membuat hitbox pemain sedikit lebih kecil (padding) untuk kelancaran manuver
        Rectangle playerHitbox = new Rectangle(
                x + 5,
                y + 5,
                gameWorld.player.width - 10,
                gameWorld.player.height - 10);

        for (Batu b : gameWorld.batuList) {
            // Memanipulasi hitbox batu:
            // Bagian bawah batu dipotong signifikan (-25 piksel) agar pemain dapat
            // bergerak di area visual bawah batu yang kosong.

            Rectangle rockHitbox = new Rectangle(
                    b.x + 8,
                    b.y + 8,
                    b.width - 16,
                    b.height - 25);

            if (playerHitbox.intersects(rockHitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Memperbarui logika musuh (Alien).
     * Mengatur pergerakan memantul dan mekanisme penembakan acak.
     */
    private void updateAliens() {
        for (Alien alien : gameWorld.alienList) {

            // Perbarui posisi berdasarkan kecepatan saat ini
            alien.x += alien.horizontalVelocity;
            alien.y += alien.verticalVelocity;

            // Pantulan dinding batas layar
            if (alien.x < 0 || alien.x > 750) {
                alien.horizontalVelocity *= -1;
            }
            if (alien.y < 0 || alien.y > 550) {
                alien.verticalVelocity *= -1;
            }

            // Pantulan terhadap objek batu
            for (Batu rock : gameWorld.batuList) {
                if (alien.getBounds().intersects(rock.getBounds())) {
                    alien.horizontalVelocity *= -1;
                    alien.verticalVelocity *= -1;
                }
            }

            // Logika menembak: Peluang 1% setiap frame untuk menembak ke arah pemain
            if (Math.random() < 0.01) {
                // Menghitung sudut tembak (trajectory) menuju pemain
                double angle = Math.atan2(gameWorld.player.y - alien.y, gameWorld.player.x - alien.x);
                double speed = 2.0;

                // Menambahkan peluru baru (flag true menandakan milik Alien)
                gameWorld.peluruList.add(new Peluru(
                        alien.x,
                        alien.y,
                        Math.cos(angle) * speed,
                        Math.sin(angle) * speed,
                        true));
            }
        }
    }

    /**
     * Memperbarui posisi dan status seluruh peluru aktif.
     * Menangani deteksi tabrakan peluru dengan entitas lain.
     */
    private void updateBullets() {
        Iterator<Peluru> it = gameWorld.peluruList.iterator();

        while (it.hasNext()) {
            Peluru p = it.next();

            // Perbarui posisi peluru
            p.x += p.horizontalVelocity;
            p.y += p.verticalVelocity;

            // 1. Cek jika peluru keluar layar
            if (p.x < 0 || p.x > 800 || p.y < 0 || p.y > 600) {
                if (!p.fromAlien) {
                    // Peluru pemain meleset -> Catat statistik
                    gameWorld.missedShotCount++;
                } else {
                    // Peluru alien meleset -> Berikan bonus amunisi ke pemain
                    gameWorld.player.ammo++;
                }
                it.remove(); // Hapus peluru dari memori
                continue;
            }

            // 2. Cek tabrakan peluru dengan Batu
            boolean hitRock = false;
            for (Batu b : gameWorld.batuList) {
                if (p.getBounds().intersects(b.getBounds())) {
                    gameWorld.player.ammo++; // Berikan bonus amunisi ke pemain
                    it.remove();
                    hitRock = true;
                    break;
                }
            }
            if (hitRock) {
                continue;
            }

            // 3. Cek tabrakan Peluru Musuh dengan Pemain
            if (p.fromAlien && p.getBounds().intersects(gameWorld.player.getBounds())) {
                gameWorld.player.alive = false;
                AudioHelper.playSound("explosionCrunch_000.wav");
            }

            // 4. Cek tabrakan Peluru Pemain dengan Alien
            if (!p.fromAlien) {
                Iterator<Alien> ia = gameWorld.alienList.iterator();
                while (ia.hasNext()) {
                    Alien a = ia.next();
                    if (p.getBounds().intersects(a.getBounds())) {
                        ia.remove(); // Hapus Alien
                        it.remove(); // Hapus Peluru

                        gameWorld.player.score += 100; // Tambah skor

                        // Munculkan alien baru untuk menjaga kelangsungan permainan (Endless)
                        spawnNewAlien();

                        // Mainkan efek suara ledakan
                        AudioHelper.playSound("explosionCrunch_000.wav");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Memunculkan (Spawn) alien baru di lokasi acak yang aman.
     * Memastikan posisi spawn tidak bertabrakan dengan batu atau terlalu dekat
     * dengan pemain.
     */
    private void spawnNewAlien() {
        Random rand = new Random();
        int x, y;
        boolean safe;

        // Loop hingga menemukan posisi koordinat yang valid
        do {
            x = rand.nextInt(750);
            y = rand.nextInt(70) + 480; // Area spawn dibatasi di bagian bawah layar
            safe = true;

            // Buat area hitbox sementara untuk validasi
            Rectangle r = new Rectangle(x, y, 45, 45);

            // Validasi tabrakan dengan batu
            for (Batu b : gameWorld.batuList) {
                if (r.intersects(b.getBounds())) {
                    safe = false;
                    break;
                }
            }

            // Validasi jarak aman dari pemain (mencegah spawn kill)
            Rectangle playerSafeZone = new Rectangle(
                    gameWorld.player.x - 50,
                    gameWorld.player.y - 50,
                    gameWorld.player.width + 100,
                    gameWorld.player.height + 100);

            if (r.intersects(playerSafeZone)) {
                safe = false;
            }

        } while (!safe);

        // Tambahkan alien ke dalam daftar entitas aktif
        gameWorld.alienList.add(new Alien(x, y));
    }

    @Override
    public void tembakPlayer(int dx, int dy) {
        // Validasi kondisi menembak: Pemain hidup, tidak pause, dan memiliki amunisi
        if (gameWorld.player.alive && !isGamePaused && gameWorld.player.ammo > 0) {
            // Tentukan posisi awal peluru (tengah karakter pemain)
            int sx = gameWorld.player.x + (gameWorld.player.width / 2) - 5;
            int sy = gameWorld.player.y + (gameWorld.player.height / 2) - 5;

            // Instansiasi peluru baru
            gameWorld.peluruList.add(new Peluru(sx, sy, dx, dy, false));

            // Kurangi stok amunisi
            gameWorld.player.ammo--;

            // Mainkan efek suara tembakan
            AudioHelper.playSound("laserSmall_001.wav");
        }
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    @Override
    public boolean isPaused() {
        return isGamePaused;
    }

    @Override
    public void setGerakKiri(boolean m) {
        isMovingLeft = m;
    }

    @Override
    public void setGerakKanan(boolean m) {
        isMovingRight = m;
    }

    @Override
    public void setGerakAtas(boolean m) {
        isMovingUp = m;
    }

    @Override
    public void setGerakBawah(boolean m) {
        isMovingDown = m;
    }

    // Metode Getter untuk akses data oleh View (Rendering)
    @Override
    public Player getPlayer() {
        return gameWorld.player;
    }

    @Override
    public List<Alien> getListAlien() {
        return gameWorld.alienList;
    }

    @Override
    public List<Batu> getListBatu() {
        return gameWorld.batuList;
    }

    @Override
    public List<Peluru> getListPeluru() {
        return gameWorld.peluruList;
    }

    @Override
    public int getPeluruMeleset() {
        return gameWorld.missedShotCount;
    }
}