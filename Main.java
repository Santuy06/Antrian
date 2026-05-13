// ================== MAIN - INTEGRASI SEMUA CLASS ==================
// Menghubungkan: QueueManager, UserService, StatusAntrian,
//                PrioritasAntrean, LoketService, History

public class Main {
    public static void main(String[] args) {

        System.out.println("====== SISTEM ANTRIAN ======\n");

        // --- Inisialisasi semua komponen ---
        History history               = new History();            // Code 1 (Aryo)
        QueueManager queueManager     = new QueueManager();       // Code 2 (Daud)
        StatusAntrian statusAntrian   = new StatusAntrian();      // Code 4
        UserService userService       = new UserService(queueManager, statusAntrian); // Code 5 (Ghibran)
        PrioritasAntrean antrian      = new PrioritasAntrean();   // Code 6
        LoketService loket            = new LoketService(history, statusAntrian);     // Code 3 (Riyan)

        System.out.println("\n--- Pengguna mengambil nomor antrian ---");

        // Ghibran ambil 2 nomor reguler, 1 nomor prioritas
        QueueItem q1 = userService.ambilNomor("reguler");
        QueueItem q2 = userService.ambilNomor("prioritas");
        QueueItem q3 = userService.ambilNomor("reguler");

        System.out.println("\n--- Masukkan ke antrian prioritas/reguler ---");

        // Masukkan ke PrioritasAntrean (Code 6)
        antrian.tambah(q1);
        antrian.tambah(q2);
        antrian.tambah(q3);

        // Tampilkan isi antrian
        antrian.tampilkanAntrian();

        // Tampilkan papan status SEBELUM pelayanan (Code 4 - papan display)
        statusAntrian.tampilkanBoard();

        System.out.println("\n--- Loket memanggil dan melayani antrian ---");

        // Layani semua antrian (prioritas duluan)
        while (!antrian.semuaKosong()) {
            QueueItem dipanggil = antrian.panggilBerikutnya();
            loket.layani(dipanggil); // Code 3 (Riyan)
        }

        System.out.println("\n--- Papan Status Akhir (Code 4 - Display) ---");
        statusAntrian.tampilkanBoard(); // Code 4

        System.out.println("\n--- Riwayat Akhir ---");
        history.showHistory(); // Code 1 (Aryo)
        System.out.println("Total dilayani: " + history.totalServed());
    }
}
