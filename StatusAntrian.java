import java.util.HashMap;
import java.util.Map;

// ================== CODE 4 - CLASS STATUS ANTRIAN ==================
// Dikonversi dari Python ke Java
// Peran (Opsi B): PAPAN DISPLAY / MONITOR antrian saja
// → Sumber kebenaran status tetap di QueueItem.status
// → StatusAntrian hanya MENAMPILKAN ringkasan status semua antrian
// (Python dict → Java HashMap)

public class StatusAntrian {

    // format penyimpanan: {nomor_antrian -> status}
    private Map<Integer, String> dataStatus;

    public StatusAntrian() {
        dataStatus = new HashMap<>();
    }

    // [DISPLAY] Daftarkan nomor baru ke papan monitor (default: menunggu)
    // Dipanggil oleh: UserService saat user ambil nomor
    public void tambahAntrian(int nomor) {
        dataStatus.put(nomor, "menunggu");
    }

    // [DISPLAY] Sinkronisasi tampilan dari QueueItem
    // Dipanggil oleh: LoketService setiap kali status berubah
    // Tujuan: supaya papan display ikut terupdate
    public void sinkronStatus(QueueItem item) {
        if (dataStatus.containsKey(item.getQueueNumber())) {
            // Konversi status Java (waiting/called/done) → label tampilan Indonesia
            String label;
            switch (item.getStatus().toLowerCase()) {
                case "waiting" : label = "menunggu";  break;
                case "called"  : label = "dipanggil"; break;
                case "done"    : label = "selesai";   break;
                default        : label = item.getStatus();
            }
            dataStatus.put(item.getQueueNumber(), label);
        }
    }

    // Cek status nomor tertentu
    public void cekStatus(int nomor) {
        if (dataStatus.containsKey(nomor)) {
            System.out.println("Status nomor " + nomor + ": " + dataStatus.get(nomor));
        } else {
            System.out.println("Nomor tidak ditemukan");
        }
    }

    // [DISPLAY] Tampilkan papan monitor semua antrian
    public void tampilkanBoard() {
        System.out.println("\n====== PAPAN STATUS ANTRIAN ======");
        if (dataStatus.isEmpty()) {
            System.out.println("(belum ada antrian)");
        } else {
            System.out.println(String.format("%-10s %-15s", "No. Antrian", "Status"));
            System.out.println("---------------------------");
            for (Map.Entry<Integer, String> entry : dataStatus.entrySet()) {
                System.out.println(String.format("%-10d %-15s", entry.getKey(), entry.getValue()));
            }
        }
        System.out.println("==================================");
    }

    // Getter untuk keperluan lain
    public String getStatus(int nomor) {
        return dataStatus.getOrDefault(nomor, "tidak ditemukan");
    }
}
