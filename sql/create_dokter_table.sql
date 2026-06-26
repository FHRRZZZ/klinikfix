-- Buat tabel dokter jika belum ada
CREATE TABLE IF NOT EXISTS dokter (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    spesialis VARCHAR(100) NOT NULL,
    no_hp VARCHAR(15) NOT NULL,
    jadwal VARCHAR(100) NOT NULL
);
