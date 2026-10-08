package model;

/**
 * Data Access Object (DAO) yang menangani operasi database spesifik untuk tabel
 * 'tbenefit'.
 * Kelas ini bertanggung jawab untuk menyimpan skor, memuat sisa amunisi pemain,
 * dan mengambil data untuk papan peringkat (High Score).
 */
public class TabelBenefit extends DB {

    /**
     * Mengonstruksi objek TabelBenefit dan menginisialisasi koneksi database
     * melalui konstruktor superclass (DB).
     *
     * @throws Exception jika driver database tidak ditemukan atau koneksi gagal
     */
    public TabelBenefit() throws Exception {
        super();
    }

    /**
     * Mengambil seluruh data dari tabel 'tbenefit' dan mengurutkannya berdasarkan
     * skor tertinggi.
     * Metode ini biasanya digunakan untuk mengisi tabel High Score pada tampilan
     * menu.
     */
    public void getBenefitData() {
        try {
            String sqlQuery = "SELECT * FROM tbenefit ORDER BY skor DESC";
            createQuery(sqlQuery);
        } catch (Exception e) {
            System.err.println("Error getBenefitData: " + e.toString());
        }
    }

    /**
     * Mengambil data sisa peluru terakhir untuk pengguna tertentu.
     * Jika pengguna belum pernah bermain (data tidak ditemukan di DB),
     * maka akan dikembalikan nilai default (modal awal).
     *
     * @param username nama pengguna yang ingin dicek datanya
     * @return jumlah sisa peluru (int). Default 0 untuk pengguna baru.
     */
    public int getSisaPeluru(String username) {
        int remainingAmmo = 0; // Modal awal peluru untuk pengguna baru

        try {
            // Mengecek data user spesifik
            String sqlQuery = "SELECT sisa_peluru FROM tbenefit WHERE username='" + username + "'";
            createQuery(sqlQuery);

            // Menggunakan variabel 'resultSet' yang diwarisi dari class DB
            if (this.resultSet.next()) {
                // KASUS: User Lama ditemukan -> Ambil sisa peluru terakhir dari database
                remainingAmmo = this.resultSet.getInt("sisa_peluru");
            }
            // KASUS: User Baru tidak ditemukan -> Tetap gunakan nilai default (0)
        } catch (Exception e) {
            System.err.println("Error getSisaPeluru: " + e.toString());
        }

        return remainingAmmo;
    }

    /**
     * Mengambil skor terakhir untuk pengguna tertentu.
     * Digunakan untuk menampilkan skor terakhir pada saat memulai game.
     *
     * @param username nama pengguna yang ingin dicek datanya
     * @return skor terakhir (int). Default 0 jika tidak ditemukan.
     */
    public int getSkorTerakhir(String username) {
        int skor = 0;
        try {
            String query = "SELECT skor FROM tbenefit WHERE username='" + username + "'";
            createQuery(query);
            if (this.resultSet.next()) {
                skor = this.resultSet.getInt("skor");
            }
        } catch (Exception e) {
            System.err.println("Error getSkorTerakhir: " + e.toString());
        }
        return skor;
    }

    /**
     * Mengambil jumlah peluru meleset terakhir untuk pengguna tertentu.
     *
     * @param username nama pengguna yang ingin dicek datanya
     * @return jumlah peluru meleset (int). Default 0 jika tidak ditemukan.
     */
    public int getMelesetTerakhir(String username) {
        int meleset = 0;
        try {
            String query = "SELECT peluru_meleset FROM tbenefit WHERE username='" + username + "'";
            createQuery(query);
            if (this.resultSet.next()) {
                meleset = this.resultSet.getInt("peluru_meleset");
            }
        } catch (Exception e) {
            System.err.println("Error getMelesetTerakhir: " + e.toString());
        }
        return meleset;
    }

    /**
     * Menyimpan atau memperbarui data permainan untuk pengguna tertentu.
     * Jika pengguna sudah ada di database, maka data akan diupdate.
     * Jika pengguna belum ada, maka data baru akan dimasukkan (insert).
     *
     * @param username    nama pengguna yang datanya akan disimpan
     * @param totalSkor   skor total yang akan disimpan
     * @param totalMeleset jumlah peluru meleset yang akan ditambahkan
     * @param sisaPeluru  jumlah sisa peluru yang akan disimpan
     */
    public void saveGameData(String username, int totalSkor, int totalMeleset, int sisaPeluru) {
        try {
            String checkQuery = "SELECT * FROM tbenefit WHERE username='" + username + "'";
            createQuery(checkQuery);

            if (this.resultSet.next()) {                
                String updateQuery = "UPDATE tbenefit SET " +
                        "skor = " + totalSkor + ", " +
                        "peluru_meleset = " + totalMeleset + ", " + 
                        "sisa_peluru = " + sisaPeluru + " " +
                        "WHERE username = '" + username + "'";
                createUpdate(updateQuery);
            } else {
                String insertQuery = "INSERT INTO tbenefit VALUES ('" + username + "', " +
                        totalSkor + ", " + totalMeleset + ", " + sisaPeluru + ")";
                createUpdate(insertQuery);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}