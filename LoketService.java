// ================== CODE 3 - CLASS LOKET SERVICE ==================
// Dibuat oleh: Riyan
// Fungsi: Melayani antrian dari loket → ubah status QueueItem → sinkron ke papan display → kirim ke History

public class LoketService {

    private History history;           // dari Aryo (Code 1)
    private StatusAntrian statusAntrian; // dari Code 4

    public LoketService(History history, StatusAntrian statusAntrian) {
        this.history = history;
        this.statusAntrian = statusAntrian;
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

        // Simulasi pelayanan
        System.out.println("Sedang melayani nomor " + item.getQueueNumber() + "...");

        // [LOGIC] Ubah status di QueueItem
        item.setStatus("done");
        // [DISPLAY] Sinkron ke papan status
        statusAntrian.sinkronStatus(item);
        System.out.println("Selesai nomor: " + item.getQueueNumber());

        // Kirim ke history
        history.addToHistory(item);
    }
}
