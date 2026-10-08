package view;

import presenter.KontrakMenuPresenter;
import presenter.MenuPresenter;
import model.AudioHelper;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Kelas View yang menangani tampilan Menu Utama permainan.
 * Kelas ini menampilkan input username, tabel skor tinggi (High Score),
 * dan tombol untuk memulai permainan atau keluar.
 * TEMA: Disesuaikan dengan visual Game (Background Mars).
 */
public class ViewMenu extends JFrame implements KontrakMenuView {

    // Menggunakan Interface Presenter
    private KontrakMenuPresenter menuPresenter;

    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JButton playButton;
    private JButton quitButton;

    // Musik Latar Menu
    private Clip bgmSound;

    // Aset Gambar untuk Background Menu
    private BufferedImage backgroundImage;



    /**
     * Konstruktor untuk ViewMenu.
     * Inisialisasi komponen GUI dan mengatur event listener.
     */
    public ViewMenu() {
        // Inisialisasi Presenter (Polymorphism)
        this.menuPresenter = new MenuPresenter(this);

        // Memuat Gambar Background
        loadImages();

        // Membangun Tampilan
        initializeView();

        // Meminta Presenter untuk memuat data skor (bukan load sendiri)
        menuPresenter.muatDataSkor();

        // Memutar Musik Latar Menu
        bgmSound = AudioHelper.playMusic("spaceEngine_000.wav");
    }



    /**
     * Memuat gambar background agar sama dengan TampilGame.
     */
    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("assets/Mars(512 x 512).png"));
        } catch (IOException e) {
            System.err.println("Gagal memuat background menu: " + e.getMessage());
        }
    }




    /**
     * Inisialisasi dan menyusun komponen GUI untuk Menu Utama.
     */
    private void initializeView() {
        setTitle("Hide and Seek The Challenge");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 1. MEMBUAT PANEL UTAMA DENGAN BACKGROUND GAMBAR ---
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Menggambar background
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(210, 105, 30)); // Fallback warna Mars
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel); // Set panel ini sebagai konten utama JFrame

        // --- 2. JUDUL (Bagian Atas) ---
        JLabel titleLabel = new JLabel("HIDE AND SEEK THE CHALLENGE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.YELLOW); // Warna teks kuning agar kontras
        // Memberi bayangan hitam pada teks (opsional, pakai border empty saja)
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- 3. AREA TENGAH (Input & Tabel) ---
        // Panel pembungkus transparan
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50)); // Padding kiri kanan

        // A. Panel Input Username
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false); // Transparan

        JLabel userLabel = new JLabel("Username: ");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE); // Teks Putih

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        inputPanel.add(userLabel);
        inputPanel.add(usernameField);

        centerPanel.add(inputPanel, BorderLayout.NORTH);

        // B. Tabel Skor
        String[] columnNames = {"Username", "Skor", "Meleset", "Sisa Peluru"};
        tableModel = new DefaultTableModel(columnNames, 0);
        scoreTable = new JTable(tableModel);
        scoreTable.setFillsViewportHeight(true);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        // Mengatur transparansi area scroll (opsional, biasanya dibiarkan putih agar tabel terbaca)
        scrollPane.setPreferredSize(new Dimension(700, 300));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- 4. TOMBOL (Bagian Bawah) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false); // Transparan

        playButton = createStyledButton("PLAY GAME");
        quitButton = createStyledButton("QUIT");

        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        setupActionListeners();
    }



    /**
     * Helper untuk membuat tombol dengan gaya yang seragam (Warna Coklat/Oranye Mars).
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(139, 69, 19)); // Warna Coklat Batu
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return btn;
    }


    
    /**
     * Mengatur ActionListener untuk tombol-tombol pada menu.
     */
    private void setupActionListeners() {
        // Tombol Play
        playButton.addActionListener(e -> {
            String inputName = usernameField.getText().trim();
            if (!inputName.isEmpty()) {
                AudioHelper.stopMusic(bgmSound);

                // Panggil lewat interface presenter
                menuPresenter.mulaiGame(inputName);
            } else {
                tampilkanError("Silakan isi username terlebih dahulu!");
            }
        });

        // Logika Tombol Quit
        quitButton.addActionListener(e -> {
            // Matikan musik saat quit
            AudioHelper.stopMusic(bgmSound);
            System.exit(0);
        });
    }

    // --- IMPLEMENTASI DARI KONTRAK VIEW (KontrakMenuView) ---

    /**
     * Memperbarui tabel skor dengan data yang diberikan dari presenter.
     */
    @Override
    public void updateTabelSkor(Object[][] dataSkor) {
        // Reset tabel
        tableModel.setRowCount(0);
        // Isi data dari presenter
        for (Object[] row : dataSkor) {
            tableModel.addRow(row);
        }
    }

    /**
     * Menampilkan pesan error dalam dialog.
     */
    @Override
    public void tampilkanError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Menutup jendela menu.
     */
    @Override
    public void tutupMenu() {
        this.dispose();
    }
}