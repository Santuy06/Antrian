import java.util.ArrayList;
import java.util.List;

// ================== CODE 1 - CLASS HISTORY ==================
// Dibuat oleh: Aryo
// Fungsi: Menyimpan riwayat antrian yang sudah selesai (status = "done")

public class History {
    private List<QueueItem> completedQueue;

    public History() {
        completedQueue = new ArrayList<>();
    }

    // Tambahkan ke riwayat jika status DONE
    public void addToHistory(QueueItem item) {
        if (item.getStatus().equalsIgnoreCase("done")) {
            completedQueue.add(item);
            System.out.println("Antrian " + item.getQueueNumber() + " masuk ke history.");
        } else {
            System.out.println("Antrian belum selesai, tidak bisa masuk history.");
        }
    }

    // Tampilkan semua riwayat
    public void showHistory() {
        System.out.println("\n=== RIWAYAT ANTRIAN ===");
        if (completedQueue.isEmpty()) {
            System.out.println("Belum ada riwayat.");
        } else {
            for (QueueItem q : completedQueue) {
                System.out.println("Nomor: " + q.getQueueNumber() +
                        " | Tipe: " + q.getType() +
                        " | Status: " + q.getStatus());
            }
        }
    }

    // Hitung total antrian selesai
    public int totalServed() {
        return completedQueue.size();
    }

    // Hapus semua riwayat
    public void clearHistory() {
        completedQueue.clear();
        System.out.println("History berhasil dihapus.");
    }
}
