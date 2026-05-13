// ================== CLASS QUEUE ITEM (BASE CLASS) ==================
// Dipakai oleh: History, QueueManager, LoketService, UserService, PrioritasAntrean, StatusAntrian

public class QueueItem {
    protected int queueNumber;
    protected String status;
    protected String type; // "prioritas" atau "reguler"

    // Constructor default → tipe reguler
    public QueueItem(int queueNumber) {
        this.queueNumber = queueNumber;
        this.status = "waiting";
        this.type = "reguler";
    }

    // Constructor dengan tipe (prioritas/reguler)
    public QueueItem(int queueNumber, String type) {
        this.queueNumber = queueNumber;
        this.status = "waiting";
        this.type = type;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "QueueItem{nomor=" + queueNumber + ", status=" + status + ", tipe=" + type + "}";
    }
}
