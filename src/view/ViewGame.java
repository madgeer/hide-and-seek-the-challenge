package view;

import presenter.KontrakGamePresenter;
import model.Alien;
import model.Batu;
import model.Peluru;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Panel utama untuk menampilkan visual permainan (Gameplay View).
 * Kelas ini menangani rendering gambar (aset), menangkap input keyboard,
 * dan menampilkan layar status (Pause/Game Over).
 */
public class ViewGame extends JPanel implements KontrakViewGame {

    private KontrakGamePresenter gamePresenter;

    // --- VARIABEL ASET GAMBAR ---
    private BufferedImage backgroundImage;
    private BufferedImage playerImage;
    private BufferedImage alienImage;
    private BufferedImage stoneImage;

    /**
     * Konstruktor ViewGame.
     * Mengatur fokus panel, warna background, memuat aset gambar, dan inisialisasi input.
     */
    public ViewGame() {
        setFocusable(true); 
        setBackground(Color.WHITE);
        loadImages();
        setupInputListeners();
    }

    /**
     * Memuat aset gambar dari folder "assets".
     * Jika gambar gagal dimuat, permainan akan tetap berjalan menggunakan bentuk geometris sederhana.
     */
    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("assets/Mars(512 x 512).png"));
            playerImage = ImageIO.read(new File("assets/Soldier(100 x 100).png"));
            alienImage = ImageIO.read(new File("assets/Alien(208 x 208).png"));
            stoneImage = ImageIO.read(new File("assets/Rock(160 x 160).png"));
        } catch (IOException e) {
            System.err.println("Peringatan: Gagal memuat gambar (Cek folder assets). " + e.getMessage());
        }
    }

    /**
     * Menghubungkan View dengan Presenter.
     * @param prosesGame Objek presenter yang mengatur logika game.
     */
    public void setProsesGame(KontrakGamePresenter prosesGame) {
        this.gamePresenter = prosesGame;
        this.gamePresenter.setView(this);
    }

    /**
     * Menutup jendela game dan kembali ke menu utama.
     */
    @Override
    public void tutupGame() {
        // Ambil window tempat panel ini berada
        Window currentWindow = SwingUtilities.getWindowAncestor(this);
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        
        // Buka kembali menu utama
        SwingUtilities.invokeLater(() -> {
            new ViewMenu().setVisible(true);
        });
    }


    /**
     * Mengatur logika input keyboard (Key Listener).
     * Memisahkan kontrol berdasarkan status game (GameOver, Paused, Running).
     */
    private void setupInputListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gamePresenter == null) {
                    return;
                }

                int bulletSpeed = 8;
                boolean isGameOver = !gamePresenter.getPlayer().alive;
                boolean isPaused = gamePresenter.isPaused();

                // --- 1. KONTROL SAAT GAME OVER ---
                if (isGameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_R -> gamePresenter.resetGame();      // R = Restart
                        case KeyEvent.VK_M -> gamePresenter.kembaliKeMenu();  // M = Menu
                    }
                    return; // Stop, jangan jalankan kontrol lain
                }

                // --- 2. KONTROL SAAT PAUSE ---
                if (isPaused) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_SPACE -> gamePresenter.togglePause(); // Spasi = Lanjut Main
                        case KeyEvent.VK_M -> gamePresenter.kembaliKeMenu();   // M = Menu
                    }
                    return; // Stop, jangan jalankan kontrol gerak
                }

                // --- 3. KONTROL SAAT MAIN (Gameplay) ---
                switch (e.getKeyCode()) {
                    // Gerakan (Panah)
                    case KeyEvent.VK_LEFT -> gamePresenter.setGerakKiri(true);
                    case KeyEvent.VK_RIGHT -> gamePresenter.setGerakKanan(true);
                    case KeyEvent.VK_UP -> gamePresenter.setGerakAtas(true);
                    case KeyEvent.VK_DOWN -> gamePresenter.setGerakBawah(true);

                    // Menembak (WASD)
                    case KeyEvent.VK_W -> gamePresenter.tembakPlayer(0, -bulletSpeed); // Tembak Atas
                    case KeyEvent.VK_A -> gamePresenter.tembakPlayer(-bulletSpeed, 0); // Tembak Kiri
                    case KeyEvent.VK_S -> gamePresenter.tembakPlayer(0, bulletSpeed);  // Tembak Bawah
                    case KeyEvent.VK_D -> gamePresenter.tembakPlayer(bulletSpeed, 0);  // Tembak Kanan

                    // Fitur Lain
                    case KeyEvent.VK_SPACE -> gamePresenter.togglePause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (gamePresenter == null) {
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> gamePresenter.setGerakKiri(false);
                    case KeyEvent.VK_RIGHT -> gamePresenter.setGerakKanan(false);
                    case KeyEvent.VK_UP -> gamePresenter.setGerakAtas(false);
                    case KeyEvent.VK_DOWN -> gamePresenter.setGerakBawah(false);
                }
            }
        });
    }



    /**
     * Meminta panel untuk menggambar ulang (refresh) tampilan.
     */
    @Override
    public void tampil() {
        repaint(); // Meminta Java untuk menggambar ulang panel
    }



    /**
     * Metode utama untuk menggambar semua elemen game di panel.
     * Dipanggil otomatis oleh sistem Swing saat repaint() dipanggil.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        // Cegah error jika presenter belum siap
        if (gamePresenter == null) {
            return;
        }

        Player player = gamePresenter.getPlayer();
        List<Alien> alienList = gamePresenter.getListAlien();
        List<Batu> batuList = gamePresenter.getListBatu();
        List<Peluru> peluruList = gamePresenter.getListPeluru();

        // 1. Gambar Background
        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // Fallback warna jika gambar tidak ada
            graphics.setColor(new Color(210, 105, 30)); // Warna Mars-ish
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }

        // Gambar Batas Area
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, 785, 560);

        // 2. Gambar Objek - Batu
        for (Batu batu : batuList) {
            if (stoneImage != null) {
                graphics.drawImage(stoneImage, batu.x, batu.y, batu.width, batu.height, null);
            } else {
                graphics.setColor(new Color(139, 69, 19));
                graphics.fillOval(batu.x, batu.y, batu.width, batu.height);
            }
        }

        // 3. Gambar Objek - Player
        if (player.alive) {
            if (playerImage != null) {
                graphics.drawImage(playerImage, player.x, player.y, player.width, player.height, null);
            } else {
                drawSmiley(graphics, player.x, player.y, player.width, Color.YELLOW, Color.RED);
            }
        }

        // 4. Gambar Objek - Alien
        for (Alien alien : alienList) {
            if (alienImage != null) {
                graphics.drawImage(alienImage, alien.x, alien.y, alien.width, alien.height, null);
            } else {
                drawSmiley(graphics, alien.x, alien.y, alien.width, Color.RED, Color.BLACK);
            }
        }

        // 5. Gambar Objek - Peluru
        for (Peluru peluru : peluruList) {
            graphics.setColor(peluru.fromAlien ? Color.BLUE : Color.GREEN);
            graphics.fillOval(peluru.x, peluru.y, peluru.width, peluru.height);
        }

        // 6. Gambar UI (HUD & Overlay)
        drawHUD(graphics, player);

        if (!player.alive) {
            drawGameOverScreen(graphics); // Tampilan Khusus Game Over
        } else if (gamePresenter.isPaused()) {
            drawPauseScreen(graphics); // Tampilan Khusus Pause
        }
    }

    // --- BAGIAN: HELPER VISUAL METHODS ---

    /**
     * Menggambar karakter sederhana (bulat dengan mata dan mulut) jika gambar aset gagal dimuat.
     */
    private void drawSmiley(Graphics graphics, int x, int y, int size, Color bodyColor, Color faceColor) {
        graphics.setColor(bodyColor);
        graphics.fillOval(x, y, size, size);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x, y, size, size);
        graphics.setColor(faceColor);
        graphics.fillOval(x + 7, y + 8, 5, 5);  // Mata Kiri
        graphics.fillOval(x + 18, y + 8, 5, 5); // Mata Kanan
        graphics.drawArc(x + 8, y + 10, 14, 10, 0, -180); // Mulut Senyum
    }



    /**
     * Menggambar Heads-Up Display (Skor, Ammo, Info).
     */
    private void drawHUD(Graphics graphics, Player player) {
        // Kotak Latar HUD
        graphics.setColor(new Color(200, 220, 255));
        graphics.fillRoundRect(10, 10, 220, 85, 15, 15);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(10, 10, 220, 85, 15, 15);
        
        // Teks Data
        graphics.setFont(new Font("Arial", Font.BOLD, 13));
        graphics.drawString("Skor: " + player.score, 20, 30);
        graphics.drawString("Meleset: " + gamePresenter.getPeluruMeleset(), 20, 50);
        graphics.drawString("Ammo: " + player.ammo, 20, 70);
        
        // Petunjuk Tombol
        graphics.setColor(Color.DARK_GRAY);
        graphics.setFont(new Font("Arial", Font.PLAIN, 10));
        graphics.drawString("[WASD] Tembak   [Spasi] Pause", 20, 88);
    }



    /**
     * Menggambar layar overlay saat game dipause.
     */
    private void drawPauseScreen(Graphics graphics) {
        // Gelapkan layar
        graphics.setColor(new Color(0, 0, 0, 150));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Judul
        graphics.setColor(Color.CYAN);
        graphics.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(graphics, "PAUSED", 200);

        // Tombol / Petunjuk
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(graphics, "[SPACE] Resume Game", 260);
        drawCenteredString(graphics, "[M] Back to Menu", 300);
    }

    /**
     * Menggambar layar overlay saat game over.
     */
    private void drawGameOverScreen(Graphics graphics) {
        // Gelapkan layar (lebih merah biar dramatis)
        graphics.setColor(new Color(50, 0, 0, 180));
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Judul
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(graphics, "GAME OVER", 200);

        // Tombol / Petunjuk
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(graphics, "[R] Restart Game", 280);
        drawCenteredString(graphics, "[M] Back to Menu", 320);
    }



    /**
     * Helper untuk menggambar teks tepat di tengah layar secara horizontal.
     */
    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}