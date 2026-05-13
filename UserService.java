// ================== CODE 5 - CLASS USER SERVICE ==================
// Dibuat oleh: Ghibran
// Fungsi: User mengambil nomor antrian → generate via QueueManager → buat QueueItem

public class UserService {

    private QueueManager queueManager;     // dari Daud (Code 2)
    private StatusAntrian statusAntrian;   // dari Code 4

    public UserService(QueueManager queueManager, StatusAntrian statusAntrian) {
        this.queueManager = queueManager;
        this.statusAntrian = statusAntrian;
    }

    // User ambil nomor antrian dengan tipe tertentu (prioritas/reguler)
    public QueueItem ambilNomor(String tipe) {
        // Generate nomor dari QueueManager (simpan ke DB)
        queueManager.generateNomor();

        // Ambil nomor yang baru di-generate
        int nomor = queueManager.getNomorTerakhir();

        // Daftarkan ke StatusAntrian
        statusAntrian.tambahAntrian(nomor);

        // Buat QueueItem dengan tipe
        QueueItem item = new QueueItem(nomor, tipe);

        System.out.println("Nomor antrian Anda: " + nomor + " (tipe: " + tipe + ")");

        return item;
    }

    // Overload: default tipe = reguler
    public QueueItem ambilNomor() {
        return ambilNomor("reguler");
    }
}
