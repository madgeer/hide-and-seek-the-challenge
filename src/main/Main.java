package main;

import view.ViewMenu;
import javax.swing.SwingUtilities;

/**
 * Kelas utama (Main Class) untuk menjalankan aplikasi "Hide and Seek The
 * Challenge" dan menampilkan jendela menu utama.
 */
public class Main {
    public static void main(String[] args) {
        // Menjalankan GUI di dalam Event Dispatch Thread untuk keamanan thread Swing
        SwingUtilities.invokeLater(() -> {
            // Membuat instance Menu Utama
            ViewMenu menuWindow = new ViewMenu();

            // Menampilkan jendela ke layar
            menuWindow.setVisible(true);
        });
    }
}