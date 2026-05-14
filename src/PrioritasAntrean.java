import java.util.LinkedList;
import java.util.Queue;

// ================== CLASS PRIORITAS ANTREAN ==================
// Fungsi: Mengelola dua antrian — prioritas (VIP/Lansia) dan reguler

public class PrioritasAntrean {

    private Queue<QueueItem> prioritas; // antrian VIP / Lansia
    private Queue<QueueItem> reguler;   // antrian biasa

    public PrioritasAntrean() {
        prioritas = new LinkedList<>();
        reguler = new LinkedList<>();
    }

    // Ambil daftar semua antrian (untuk API Web)
    public java.util.List<QueueItem> getSemuaAntrian() {
        java.util.List<QueueItem> list = new java.util.ArrayList<>();
        list.addAll(prioritas);
        list.addAll(reguler);
        return list;
    }

    // Tambah item ke antrian sesuai tipenya
    public void tambah(QueueItem item) {
        if (item.getType().equalsIgnoreCase("prioritas")) {
            prioritas.add(item);
            System.out.println("Nomor " + item.getQueueNumber() + " masuk antrian PRIORITAS");
        } else {
            reguler.add(item);
            System.out.println("Nomor " + item.getQueueNumber() + " masuk antrian REGULER");
        }
    }

    // Panggil antrian berikutnya (prioritas didahulukan)
    public QueueItem panggilBerikutnya() {
        if (!prioritas.isEmpty()) {
            QueueItem item = prioritas.poll();
            System.out.println("Dipanggil (PRIORITAS): Nomor " + item.getQueueNumber());
            return item;
        } else if (!reguler.isEmpty()) {
            QueueItem item = reguler.poll();
            System.out.println("Dipanggil (REGULER): Nomor " + item.getQueueNumber());
            return item;
        } else {
            System.out.println("Semua antrian kosong.");
            return null;
        }
    }

    // Tampilkan isi antrian
    public void tampilkanAntrian() {
        System.out.println("\nAntrean Prioritas : " + prioritas.size() + " orang");
        for (QueueItem q : prioritas) {
            System.out.println("  - Nomor " + q.getQueueNumber());
        }
        System.out.println("Antrean Reguler   : " + reguler.size() + " orang");
        for (QueueItem q : reguler) {
            System.out.println("  - Nomor " + q.getQueueNumber());
        }
    }

    // Cek apakah semua antrian kosong
    public boolean semuaKosong() {
        return prioritas.isEmpty() && reguler.isEmpty();
    }
}
