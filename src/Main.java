import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.List;

// ================== MAIN ENTRY POINT ==================
public class Main {
    public static void main(String[] args) {
        System.out.println("====== MEMULAI SERVER SISTEM ANTRIAN ======\n");

        // --- Inisialisasi semua komponen backend ---
        History history               = new History();            
        QueueManager queueManager     = new QueueManager();       
        StatusAntrian statusAntrian   = new StatusAntrian();      
        UserService userService       = new UserService(queueManager, statusAntrian); 
        PrioritasAntrean antrian      = new PrioritasAntrean();   
        LoketService loket            = new LoketService(history, statusAntrian, queueManager);

        try {
            // Membuat HttpServer di port 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            // Daftarkan handler untuk semua request (root "/")
            server.createContext("/", new WebHandler(userService, antrian, loket));
            
            server.setExecutor(null); // Gunakan default executor
            server.start();
            
            System.out.println("\n✅ Server Web berhasil berjalan!");
            System.out.println("👉 Buka di browser: http://localhost:8080");
            
        } catch (IOException e) {
            System.out.println("Gagal memulai server: " + e.getMessage());
        }
    }
}

// ================== CLASS WEB HANDLER ==================
// PENJELASAN UNTUK DOSEN:
// Class ini bertugas sebagai jembatan agar aplikasi Java bisa dibuka di Web Browser.
// Kami tidak menggunakan framework tambahan, murni menggunakan library bawaan Java (HttpHandler).
class WebHandler implements HttpHandler {
    private UserService userService;
    private PrioritasAntrean antrian;
    private LoketService loket;
    private QueueItem currentCalled = null;

    public WebHandler(UserService userService, PrioritasAntrean antrian, LoketService loket) {
        this.userService = userService;
        this.antrian = antrian;
        this.loket = loket;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        // 1. Jika URL diawali /api/, berarti Web Browser sedang meminta/mengirim data antrian
        if (path.startsWith("/api/")) {
            handleApi(exchange, path, method);
            return;
        }

        // 2. Jika bukan /api/, berarti Web Browser meminta file tampilan (HTML/CSS/JS)
        if (path.equals("/")) {
            path = "/index.html";
        }
        
        File file = new File("web" + path);
        if (!file.exists()) {
            file = new File("../web" + path); // Fallback jika dijalankan dari dalam folder src/
        }

        if (file.exists() && !file.isDirectory()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            
            // Set content type
            if (path.endsWith(".html")) exchange.getResponseHeaders().set("Content-Type", "text/html");
            else if (path.endsWith(".css")) exchange.getResponseHeaders().set("Content-Type", "text/css");
            else if (path.endsWith(".js")) exchange.getResponseHeaders().set("Content-Type", "application/javascript");
            
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } else {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleApi(HttpExchange exchange, String path, String method) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String response = "";

        if (path.equals("/api/status") && method.equals("GET")) {
            response = generateStatusJson();
            sendJsonResponse(exchange, 200, response);
            
        } else if (path.equals("/api/ambil") && method.equals("POST")) {
            String query = exchange.getRequestURI().getQuery();
            String tipe = "reguler";
            if (query != null && query.contains("tipe=prioritas")) {
                tipe = "prioritas";
            }
            
            QueueItem newItem = userService.ambilNomor(tipe);
            antrian.tambah(newItem);
            
            response = "{\"success\": true, \"nomor\": " + newItem.getQueueNumber() + ", \"tipe\": \"" + newItem.getType() + "\"}";
            sendJsonResponse(exchange, 200, response);
            
        } else if (path.equals("/api/layani") && method.equals("POST")) {
            // [LOGIC PERBAIKAN]: Auto-selesai jika ada antrian yang menggantung
            if (currentCalled != null) {
                loket.selesaikan(currentCalled);
                currentCalled = null;
            }

            if (!antrian.semuaKosong()) {
                currentCalled = antrian.panggilBerikutnya();
                loket.layani(currentCalled);
                response = "{\"success\": true}";
            } else {
                response = "{\"success\": false, \"message\": \"Antrian kosong\"}";
            }
            sendJsonResponse(exchange, 200, response);
            
        } else if (path.equals("/api/selesai") && method.equals("POST")) {
            if (currentCalled != null) {
                loket.selesaikan(currentCalled);
                currentCalled = null; // Kosongkan papan panggilan admin saat ini
                response = "{\"success\": true}";
            } else {
                response = "{\"success\": false, \"message\": \"Tidak ada antrian yang sedang dilayani\"}";
            }
            sendJsonResponse(exchange, 200, response);
            
        } else {
            sendJsonResponse(exchange, 404, "{\"error\": \"Not Found\"}");
        }
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String generateStatusJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        // Current called
        if (currentCalled != null) {
            sb.append("\"current_called\": {\"nomor\": ").append(currentCalled.getQueueNumber())
              .append(", \"tipe\": \"").append(currentCalled.getType()).append("\"},");
        } else {
            sb.append("\"current_called\": null,");
        }
        
        // Waiting list
        sb.append("\"waiting_list\": [");
        List<QueueItem> waitingList = antrian.getSemuaAntrian();
        for (int i = 0; i < waitingList.size(); i++) {
            QueueItem item = waitingList.get(i);
            sb.append("{\"nomor\": ").append(item.getQueueNumber())
              .append(", \"tipe\": \"").append(item.getType()).append("\"}");
            if (i < waitingList.size() - 1) sb.append(",");
        }
        sb.append("]");
        
        sb.append("}");
        return sb.toString();
    }
}
