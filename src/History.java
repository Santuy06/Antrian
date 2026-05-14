import java.util.ArrayList;
import java.util.List;

// ================== CLASS HISTORY ==================
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

// ================== CLASS QUEUE ITEM (BASE CLASS) ==================
// SYARAT PBO: ABSTRACT CLASS
abstract class QueueItem {
    protected int queueNumber;
    protected String status;

    public QueueItem(int queueNumber) {
        this.queueNumber = queueNumber;
        this.status = "waiting";
    }

    // SYARAT PBO: ABSTRACT METHOD (Wajib diisi oleh class anak)
    public abstract String getType();

    // Getter Setter biasa
    public int getQueueNumber() { return queueNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "QueueItem{nomor=" + queueNumber + ", status=" + status + ", tipe=" + getType() + "}";
    }
}

// ================== CLASS REGULER (INHERITANCE) ==================
// SYARAT PBO: PEWARISAN (Mewarisi QueueItem)
class RegulerItem extends QueueItem {
    public RegulerItem(int nomor) {
        super(nomor); // Memanggil constructor induk
    }

    @Override
    public String getType() {
        return "reguler";
    }
}

// ================== CLASS PRIORITAS (INHERITANCE) ==================
// SYARAT PBO: PEWARISAN (Mewarisi QueueItem)
class PrioritasItem extends QueueItem {
    public PrioritasItem(int nomor) {
        super(nomor); // Memanggil constructor induk
    }

    @Override
    public String getType() {
        return "prioritas";
    }
}
