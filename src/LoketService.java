// ================== CLASS LOKET SERVICE ==================
// Fungsi: Melayani antrian dari loket → ubah status QueueItem → sinkron ke papan display → kirim ke History

public class LoketService {

    private History history;           
    private StatusAntrian statusAntrian; 
    private QueueManager queueManager;   // Akses Database

    public LoketService(History history, StatusAntrian statusAntrian, QueueManager queueManager) {
        this.history = history;
        this.statusAntrian = statusAntrian;
        this.queueManager = queueManager;
    }

    // Melayani antrian
    public void layani(QueueItem item) {

        if (item == null) {
            System.out.println("Tidak ada antrian.");
            return;
        }

        // Hanya layani yang masih waiting
        if (!item.getStatus().equalsIgnoreCase("waiting")) {
            System.out.println("Antrian " + item.getQueueNumber() +
                    " tidak bisa dilayani (status: " + item.getStatus() + ")");
            return;
        }

        // [LOGIC] Ubah status di QueueItem (sumber kebenaran)
        item.setStatus("called");
        // [DISPLAY] Sinkron ke papan status
        statusAntrian.sinkronStatus(item);
        System.out.println("Memanggil nomor: " + item.getQueueNumber());

        // Simulasi pelayanan (hanya sampai memanggil, tombol selesai dipisah)
        System.out.println("Sedang melayani nomor " + item.getQueueNumber() + "...");
    }

    // Fungsi tombol "Selesai" dari web admin
    public void selesaikan(QueueItem item) {
        if (item == null) return;

        // [LOGIC] Ubah status di QueueItem
        item.setStatus("done");
        // [DISPLAY] Sinkron ke papan status
        statusAntrian.sinkronStatus(item);
        System.out.println("Selesai nomor: " + item.getQueueNumber());

        // Update database queue_items menjadi done (via QueueManager)
        queueManager.updateStatus(item.getQueueNumber(), "done");
        
        // Simpan ke tabel history database (via QueueManager)
        queueManager.simpanHistoryDB(item.getQueueNumber(), item.getType());

        // Simpan ke array history memory
        history.addToHistory(item);
    }
}
