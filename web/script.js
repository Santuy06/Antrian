const API_BASE = '/api';

// --- FUNGSI HALAMAN USER ---
async function ambilAntrian(tipe) {
    try {
        const response = await fetch(`${API_BASE}/ambil?tipe=${tipe}`, { method: 'POST' });
        const data = await response.json();
        
        if(data.success) {
            document.getElementById('nomor-anda').textContent = data.nomor;
            document.getElementById('tipe-anda').textContent = tipe.toUpperCase();
            
            // Tampilkan kotak hasil
            const resultBox = document.getElementById('result-box');
            resultBox.classList.remove('hidden');
            
            // Animasi kecil
            resultBox.style.transform = 'scale(0.9)';
            setTimeout(() => resultBox.style.transform = 'scale(1)', 100);
        } else {
            alert('Gagal mengambil antrian');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Terjadi kesalahan jaringan');
    }
}

// --- FUNGSI HALAMAN ADMIN & DISPLAY ---
let lastCalledNumber = null;

async function fetchStatus(isAdmin = false) {
    try {
        const response = await fetch(`${API_BASE}/status`);
        const data = await response.json();
        
        if (isAdmin) {
            updateAdminView(data);
        } else {
            updateDisplayView(data);
        }
    } catch (error) {
        console.error('Error fetching status:', error);
    }
}

function updateAdminView(data) {
    // Update nomor saat ini
    const currentNumEl = document.getElementById('admin-current-number');
    if (currentNumEl) {
        currentNumEl.textContent = data.current_called ? data.current_called.nomor : '--';
    }

    // Update daftar tunggu
    const listEl = document.getElementById('admin-waiting-list');
    if (listEl) {
        listEl.innerHTML = '';
        if (data.waiting_list.length === 0) {
            listEl.innerHTML = '<li>Kosong</li>';
        } else {
            data.waiting_list.forEach(item => {
                const li = document.createElement('li');
                const badgeClass = item.tipe === 'prioritas' ? 'style="color: var(--warning); font-weight:bold;"' : '';
                li.innerHTML = `<span>Nomor <b>${item.nomor}</b></span> <span ${badgeClass}>${item.tipe.toUpperCase()}</span>`;
                listEl.appendChild(li);
            });
        }
    }
}

function updateDisplayView(data) {
    // Update nomor saat ini
    const currentNumEl = document.getElementById('display-current-number');
    const currentTypeEl = document.getElementById('display-current-type');
    
    if (currentNumEl && data.current_called) {
        const newNumber = data.current_called.nomor;
        if (lastCalledNumber !== newNumber) {
            lastCalledNumber = newNumber;
            // Play sound if new number called
            const audio = document.getElementById('tingtong');
            if (audio) audio.play().catch(e => console.log('Audio autoplay blocked'));
        }
        currentNumEl.textContent = newNumber;
        currentTypeEl.textContent = data.current_called.tipe.toUpperCase();
    } else if (currentNumEl) {
        currentNumEl.textContent = '--';
        currentTypeEl.textContent = '--';
    }

    // Update daftar tunggu
    const listEl = document.getElementById('display-waiting-list');
    if (listEl) {
        listEl.innerHTML = '';
        data.waiting_list.forEach(item => {
            const div = document.createElement('div');
            div.className = 'card';
            const badgeClass = item.tipe === 'prioritas' ? 'badge-prioritas' : 'badge-reguler';
            div.innerHTML = `<span>${item.nomor}</span> <span class="${badgeClass}">${item.tipe}</span>`;
            listEl.appendChild(div);
        });
    }
}

async function panggilBerikutnya() {
    try {
        const response = await fetch(`${API_BASE}/layani`, { method: 'POST' });
        const data = await response.json();
        
        if(data.success) {
            refreshAdminData();
        } else {
            alert('Semua antrian kosong!');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function selesaiLayanan() {
    try {
        const response = await fetch(`${API_BASE}/selesai`, { method: 'POST' });
        const data = await response.json();
        
        if(data.success) {
            refreshAdminData();
        } else {
            alert('Tidak ada nomor yang sedang dipanggil!');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Helper untuk interval
function refreshAdminData() { fetchStatus(true); }
function refreshDisplayData() { fetchStatus(false); }
