package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Mengelola koneksi langsung ke basis data MySQL dan eksekusi perintah SQL.
 */
public class DB {

    /**
     * Alamat URL untuk koneksi JDBC ke database spesifik.
     * Format: jdbc:mysql://[host]/[nama_database]
     */
    private String connectionUrl = "jdbc:mysql://localhost/hide_and_seek_the_challenge";

    /**
     * Nama pengguna untuk autentikasi database.
     * Menggunakan "root" sebagai standar default XAMPP/WAMP.
     */
    private String databaseUsername = "root";

    /**
     * Kata sandi untuk autentikasi database.
     * Dibiarkan kosong sesuai standar default XAMPP untuk user root.
     */
    private String databasePassword = "";

    /**
     * Objek koneksi aktif ke database.
     */
    protected Connection connection;

    /**
     * Objek pernyataan yang digunakan untuk mengirim perintah SQL ke database.
     */
    protected Statement statement;

    /**
     * Objek yang menampung hasil data (tabel) dari eksekusi query SELECT.
     */
    protected ResultSet resultSet;

    /**
     * Mengonstruksi objek DB baru dan segera mencoba membuat koneksi.
     * Memuat driver JDBC MySQL dan membuka sesi komunikasi ke database.
     *
     * @throws Exception
     *                      jika driver JDBC tidak ditemukan
     *                      (ClassNotFoundException).
     * @throws SQLException
     *                      jika autentikasi gagal atau database tidak dapat
     *                      dijangkau.
     */
    public DB() throws Exception, SQLException {
        // Memuat class Driver MySQL secara eksplisit (diperlukan untuk beberapa versi
        // Java lama)
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Membangun koneksi dan membuat objek statement
        connection = DriverManager.getConnection(connectionUrl, databaseUsername, databasePassword);
        statement = connection.createStatement();
    }

    /**
     * Menjalankan perintah SQL tipe SELECT untuk mengambil data.
     * Hasil eksekusi disimpan dalam variabel member {@code resultSet}.
     *
     * @param sqlQuery perintah SQL lengkap (contoh: "SELECT * FROM users")
     * @throws SQLException
     *                      jika sintaks SQL salah atau terjadi kesalahan pada
     *                      database
     * @throws Exception
     *                      untuk kesalahan umum lainnya
     */
    public void createQuery(String sqlQuery) throws Exception, SQLException {
        resultSet = statement.executeQuery(sqlQuery);
    }

    /**
     * Menjalankan perintah SQL untuk memodifikasi data (INSERT, UPDATE, DELETE).
     * Metode ini tidak menghasilkan ResultSet.
     *
     * @param sqlQuery perintah SQL modifikasi (contoh: "UPDATE users SET
     *                 score=100")
     * @throws SQLException
     *                      jika terjadi pelanggaran constraint atau sintaks SQL
     *                      salah
     * @throws Exception
     *                      untuk kesalahan umum lainnya
     */
    public void createUpdate(String sqlQuery) throws Exception, SQLException {
        statement.executeUpdate(sqlQuery);
    }

    /**
     * Mengambil objek ResultSet yang berisi data hasil query terakhir.
     *
     * @return objek ResultSet aktif, atau null jika belum ada query yang dijalankan
     */
    public ResultSet getResult() {
        return resultSet;
    }

    /**
     * Menutup semua sumber daya database yang terbuka (ResultSet, Statement,
     * Connection).
     * Penting dipanggil untuk mencegah kebocoran memori (memory leaks).
     *
     * @throws SQLException
     *                      jika terjadi kesalahan saat upaya penutupan koneksi
     * @throws Exception
     *                      untuk kesalahan umum lainnya
     */
    public void closeResult() throws Exception, SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}