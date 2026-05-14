import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ================== CLASS QUEUE MANAGER ==================
// Fungsi: Generate nomor antrian otomatis + simpan ke database MySQL

public class QueueManager {
    private Connection conn;
    private int nomorTerakhir;

    // Simulasi in-memory (sementara, sebelum DB tersambung)
    private List<Integer> simulasiDB = new ArrayList<>();
    private boolean dbTersambung = false;

    // Konstruktor: coba buka koneksi ke database
    public QueueManager() {
        try {
            // Koneksi ke database antrian_db dengan user root, tanpa password
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/antrian_db",
                    "root",
                    ""
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
    public void generateNomor(String tipe) {
        nomorTerakhir++;

        if (dbTersambung) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO queue_items (nomor, tipe, status) VALUES (?, ?, ?)"
                );
                ps.setInt(1, nomorTerakhir);
                ps.setString(2, tipe);
                ps.setString(3, "waiting");
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

    // Update status di DB
    public void updateStatus(int nomor, String status) {
        if (dbTersambung) {
            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE queue_items SET status = ? WHERE nomor = ?");
                ps.setString(1, status);
                ps.setInt(2, nomor);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Gagal update status: " + e.getMessage());
            }
        }
    }

    // Fungsi tambahan untuk memindahkannya ke tabel history
    public void simpanHistoryDB(int nomor, String tipe) {
        if (dbTersambung) {
            try {
                java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO history (nomor, tipe, waktu_masuk) VALUES (?, ?, (SELECT waktu_masuk FROM queue_items WHERE nomor = ?))");
                ps.setInt(1, nomor);
                ps.setString(2, tipe);
                ps.setInt(3, nomor);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Gagal menyimpan ke history db: " + e.getMessage());
            }
        }
    }
}
