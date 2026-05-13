import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ================== CODE 2 - CLASS QUEUE MANAGER ==================
// Dibuat oleh: Daud
// Fungsi: Generate nomor antrian otomatis + simpan ke database MySQL
// CATATAN: Database akan dikoneksikan nanti. Sementara pakai simulasi in-memory.

public class QueueManager {
    private Connection conn;
    private int nomorTerakhir;

    // Simulasi in-memory (sementara, sebelum DB tersambung)
    private List<Integer> simulasiDB = new ArrayList<>();
    private boolean dbTersambung = false;

    // Konstruktor: coba buka koneksi ke database
    public QueueManager() {
        try {
            // TODO: ganti user/password sesuai konfigurasi MySQL
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/antrian_db",
                    "root",
                    "password"
            );
            dbTersambung = true;
            nomorTerakhir = getLastNumber();
            System.out.println("Database tersambung.");
        } catch (SQLException e) {
            System.out.println("[INFO] Database belum tersambung, menggunakan mode simulasi.");
            nomorTerakhir = 0;
        }
    }

    // Ambil nomor terakhir dari tabel database
    private int getLastNumber() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(nomor) FROM queue_items");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Generate nomor otomatis + simpan ke DB (atau simulasi)
    public void generateNomor() {
        nomorTerakhir++;

        if (dbTersambung) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO queue_items (nomor, status) VALUES (?, ?)"
                );
                ps.setInt(1, nomorTerakhir);
                ps.setString(2, "waiting");
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Mode simulasi: simpan ke list
            simulasiDB.add(nomorTerakhir);
        }

        System.out.println("Nomor antrian baru dibuat: " + nomorTerakhir);
    }

    // Return nomor terakhir yang di-generate
    // (dipakai oleh UserService)
    public int getNomorTerakhir() {
        return nomorTerakhir;
    }
}
