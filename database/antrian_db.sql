-- ============================================================
-- SETUP DATABASE SISTEM ANTRIAN
-- Nama Database : antrian_db
-- Dibuat untuk  : Tubes Kelompok PBO
-- ============================================================

-- 1. Buat database (jika belum ada)
CREATE DATABASE IF NOT EXISTS antrian_db;
USE antrian_db;

-- ============================================================
-- TABEL: queue_items
-- Menyimpan semua antrian yang masuk
-- Dipakai oleh: QueueManager.java (Code 2)
-- ============================================================
CREATE TABLE IF NOT EXISTS queue_items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nomor       INT NOT NULL,
    tipe        VARCHAR(10)  NOT NULL DEFAULT 'reguler',  -- 'prioritas' / 'reguler'
    status      VARCHAR(20)  NOT NULL DEFAULT 'waiting',  -- 'waiting' / 'called' / 'done'
    waktu_masuk DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABEL: history
-- Menyimpan antrian yang sudah selesai dilayani
-- Dipakai oleh: History.java (Code 1)
-- ============================================================
CREATE TABLE IF NOT EXISTS history (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nomor         INT         NOT NULL,
    tipe          VARCHAR(10) NOT NULL,
    waktu_masuk   DATETIME,
    waktu_selesai DATETIME    DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- CEK HASIL
-- ============================================================
SELECT * FROM queue_items;

-- ============================================================
-- HAPUS DATA
-- ============================================================

TRUNCATE TABLE queue_items;
TRUNCATE TABLE history;
