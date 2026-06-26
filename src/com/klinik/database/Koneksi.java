package com.klinik.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB   = "klinik";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan!", e);
        }
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB
                   + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
        try {
            return DriverManager.getConnection(url, USER, PASS);
        } catch (SQLException e) {
            throw new SQLException("Gagal menghubungkan ke database klinik!\n"
                    + "Detail error: " + e.getMessage() + "\n\n"
                    + "Solusi: Pastikan XAMPP sudah aktif dan service MySQL sudah di-START.", e);
        }
    }
}
