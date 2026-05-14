// ================== CLASS USER SERVICE ==================
// Fungsi: Mengatur pengambilan nomor antrian untuk pengunjung (Kios K)

public class UserService {

    private QueueManager queueManager;     
    private StatusAntrian statusAntrian;   

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

        // SYARAT PBO: POLYMORPHISM & INHERITANCE
        // Membuat objek spesifik (RegulerItem / PrioritasItem) 
        // tapi disimpan di variabel induk (QueueItem)
        QueueItem item;
        if (tipe.equalsIgnoreCase("prioritas")) {
            item = new PrioritasItem(nomor);
        } else {
            item = new RegulerItem(nomor);
        }

        System.out.println("Nomor antrian Anda: " + nomor + " (tipe: " + item.getType() + ")");

        return item;
    }

    // Overload: default tipe = reguler
    public QueueItem ambilNomor() {
        return ambilNomor("reguler");
    }
}
