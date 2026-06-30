/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.klinik.janjitemu;

import com.klinik.database.Koneksi;
import com.klinik.ui.panel.index;
import com.klinik.pasien.PasienPanel;
import com.klinik.dokter.DokterPanel;
import com.klinik.rekammedis.RekamMedisPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class JanjiTemuPanel extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private int selectedJanjiTemuId = -1;

    public JanjiTemuPanel() {
        initComponents();
        initComboBoxes();
        initTable();
        loadData();
        clearFields();
        setSize(1242, 650);
        setLocationRelativeTo(null);
        
        // Bind actions programmatically since they are not bound in initComponents
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        btnAi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAiActionPerformed(evt);
            }
        });
        // Bind sidebar panel actions programmatically for navigation
        java.awt.event.MouseAdapter homeNav = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new index().setVisible(true);
                dispose();
            }
        };
        jPanel14.addMouseListener(homeNav);
        jLabel20.addMouseListener(homeNav);

        java.awt.event.MouseAdapter pasienNav = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new PasienPanel().setVisible(true);
                dispose();
            }
        };
        jPanel10.addMouseListener(pasienNav);
        jLabel16.addMouseListener(pasienNav);

        java.awt.event.MouseAdapter dokterNav = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new DokterPanel().setVisible(true);
                dispose();
            }
        };
        jPanel11.addMouseListener(dokterNav);
        jLabel17.addMouseListener(dokterNav);

        java.awt.event.MouseAdapter rekamMedisNav = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RekamMedisPanel().setVisible(true);
                dispose();
            }
        };
        jPanel13.addMouseListener(rekamMedisNav);
        jLabel19.addMouseListener(rekamMedisNav);
    }

    private void initComboBoxes() {
        // Populate Pasien dari DB
        javax.swing.DefaultComboBoxModel<String> pasienModel = new javax.swing.DefaultComboBoxModel<>();
        pasienModel.addElement("-- Pilih Pasien --");
        try {
            Connection conn = Koneksi.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nama FROM pasien ORDER BY nama");
            while (rs.next()) pasienModel.addElement(rs.getString("nama"));
            rs.close(); st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pasien: " + e.getMessage());
        }
        PilihanPasien.setModel(pasienModel);

        // Populate Dokter dari DB
        javax.swing.DefaultComboBoxModel<String> dokterModel = new javax.swing.DefaultComboBoxModel<>();
        dokterModel.addElement("-- Pilih Dokter --");
        try {
            Connection conn = Koneksi.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nama FROM dokter ORDER BY nama");
            while (rs.next()) dokterModel.addElement(rs.getString("nama"));
            rs.close(); st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat dokter: " + e.getMessage());
        }
        PilihanDokter.setModel(dokterModel);

        // Status
        setStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Menunggu", "Selesai", "Dibatalkan"}));
    }

    private void initTable() {
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Pasien", "Dokter", "Tanggal & Waktu", "Keluhan", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            Connection conn = Koneksi.getConnection();
            String sql = "SELECT jt.id, p.nama AS pasien, d.nama AS dokter, "
                       + "jt.tanggal_waktu, jt.keluhan, jt.status "
                       + "FROM janji_temu jt "
                       + "INNER JOIN pasien p ON jt.pasien_id = p.id "
                       + "INNER JOIN dokter d ON jt.dokter_id = d.id "
                       + "ORDER BY jt.tanggal_waktu";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("pasien"),
                    rs.getString("dokter"),
                    rs.getTimestamp("tanggal_waktu"),
                    rs.getString("keluhan"),
                    rs.getString("status")
                });
            }
            rs.close(); st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        PilihanPasien.setSelectedIndex(0);
        PilihanDokter.setSelectedIndex(0);
        setJanji.setDate(new Date());
        setKeluhan.setText("");
        setStatus.setSelectedIndex(0);
        txtAi.setText("");
        selectedJanjiTemuId = -1;
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            selectedJanjiTemuId = (int) tableModel.getValueAt(selectedRow, 0);
            String namaPasien = tableModel.getValueAt(selectedRow, 1).toString();
            String namaDokter = tableModel.getValueAt(selectedRow, 2).toString();

            // Pilih pasien di ComboBox
            for (int i = 0; i < PilihanPasien.getItemCount(); i++) {
                if (PilihanPasien.getItemAt(i).toString().equals(namaPasien)) {
                    PilihanPasien.setSelectedIndex(i); break;
                }
            }
            // Pilih dokter di ComboBox
            for (int i = 0; i < PilihanDokter.getItemCount(); i++) {
                if (PilihanDokter.getItemAt(i).toString().equals(namaDokter)) {
                    PilihanDokter.setSelectedIndex(i); break;
                }
            }

            Object tglObj = tableModel.getValueAt(selectedRow, 3);
            if (tglObj instanceof java.util.Date) setJanji.setDate((java.util.Date) tglObj);
            String keluhanStr = tableModel.getValueAt(selectedRow, 4).toString();
            setKeluhan.setText(keluhanStr);
            setStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());

            txtAi.setText("Menghubungi Gemini AI...");
            getAiRecommendationAsync(namaPasien, namaDokter, keluhanStr);
        }
    }

    private void getAiRecommendationAsync(String pasien, String dokter, String keluhan) {
        new javax.swing.SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                // Baca API Key dari environment variable, system property, atau gunakan key default
                String apiKey = System.getenv("GEMINI_API_KEY");
                if (apiKey == null || apiKey.isEmpty()) {
                    apiKey = System.getProperty("GEMINI_API_KEY", "");
                }
                if (apiKey == null || apiKey.isEmpty()) {
                    apiKey = "GCP_API_KEY";
                }

                // Fallback chain: coba model berikutnya jika kena quota 429 atau model tidak ditemukan 404
                String[] models = {
                    "gemini-2.5-flash",          // model terbaru 2025
                    "gemini-2.5-flash-lite-preview-06-17", // versi lite 2025
                    "gemini-2.0-flash",          // stable 2.0
                    "gemini-2.0-flash-lite",     // lite 2.0
                    "gemini-1.5-flash-latest",   // 1.5 flash terbaru
                    "gemini-1.5-pro-latest"      // 1.5 pro terbaru
                };

                String prompt = "Anda adalah asisten medis AI pintar. Berikan rekomendasi analisis awal untuk pasien bernama '" + pasien +
                                "' yang memiliki keluhan: '" + keluhan + "'. Dokter yang dituju adalah spesialis '" + dokter +
                                "'. Berikan saran langkah pertama, rekomendasi persiapan sebelum bertemu dokter, dan peringatan darurat jika ada. Jawab secara ringkas dan profesional menggunakan Bahasa Indonesia.";

                // Escape JSON sekali di luar loop
                String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
                String jsonInputString = "{\"contents\": [{\"parts\":[{\"text\": \"" + escapedPrompt + "\"}]}]}";

                String lastError = "Semua model AI tidak tersedia. Periksa koneksi internet atau API Key Anda.";

                for (int mi = 0; mi < models.length; mi++) {
                    String model = models[mi];
                    boolean isLastModel = (mi == models.length - 1);
                    try {
                        String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/"
                                        + model + ":generateContent?key=" + apiKey;
                        java.net.URL url = java.net.URI.create(urlStr).toURL();
                        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setDoOutput(true);
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(30000);

                        try (java.io.OutputStream os = conn.getOutputStream()) {
                            byte[] input = jsonInputString.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        int code = conn.getResponseCode();

                        if (code == 200) {
                            try (java.io.BufferedReader br = new java.io.BufferedReader(
                                    new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
                                StringBuilder response = new StringBuilder();
                                String responseLine;
                                while ((responseLine = br.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                String resp = response.toString();
                                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                                    "\"text\":\\s*\"(.*?)(?<!\\\\)\"",
                                    java.util.regex.Pattern.DOTALL
                                );
                                java.util.regex.Matcher matcher = pattern.matcher(resp);
                                if (matcher.find()) {
                                    return matcher.group(1)
                                        .replace("\\n", "\n")
                                        .replace("\\\"", "\"")
                                        .replace("\\\\", "\\")
                                        .replace("\\t", "\t");
                                }
                                return "AI merespons, namun format tidak dikenali. Coba lagi.";
                            }
                        } else if (code == 429 || code == 404) {
                            // 429 = quota habis, 404 = model deprecated/tidak ada
                            // Keduanya: skip ke model berikutnya
                            StringBuilder errMsg = new StringBuilder();
                            java.io.InputStream errStream = conn.getErrorStream();
                            if (errStream != null) {
                                try (java.io.BufferedReader br = new java.io.BufferedReader(
                                        new java.io.InputStreamReader(errStream, "utf-8"))) {
                                    String line;
                                    while ((line = br.readLine()) != null) errMsg.append(line);
                                }
                            }
                            if (code == 429) {
                                java.util.regex.Matcher retryMatcher = java.util.regex.Pattern
                                    .compile("retry in ([\\d.]+)s")
                                    .matcher(errMsg.toString());
                                String retrySec = retryMatcher.find() ? retryMatcher.group(1) : "?";
                                lastError = isLastModel
                                    ? "Quota habis pada semua model AI. Coba lagi dalam beberapa menit."
                                    : "Quota model " + model + " habis (retry " + retrySec + "s). Mencoba model lain...";
                            } else {
                                lastError = isLastModel
                                    ? "Semua model AI tidak tersedia. Periksa API Key atau coba lagi nanti."
                                    : "Model " + model + " tidak tersedia. Mencoba model lain...";
                            }
                            continue; // coba model berikutnya
                        } else {
                            // Error lain - baca pesan dan hentikan loop
                            StringBuilder errMsg = new StringBuilder();
                            java.io.InputStream errStream = conn.getErrorStream();
                            if (errStream != null) {
                                try (java.io.BufferedReader br = new java.io.BufferedReader(
                                        new java.io.InputStreamReader(errStream, "utf-8"))) {
                                    String line;
                                    while ((line = br.readLine()) != null) errMsg.append(line);
                                }
                            }
                            java.util.regex.Matcher errMatcher = java.util.regex.Pattern
                                .compile("\"message\":\\s*\"(.*?)\"")
                                .matcher(errMsg.toString());
                            lastError = errMatcher.find()
                                ? "Error AI (HTTP " + code + "): " + errMatcher.group(1)
                                : "Gagal mendapatkan respon dari AI (HTTP: " + code + ")";
                            break;
                        }
                    } catch (java.net.SocketTimeoutException e) {
                        lastError = "Koneksi ke model " + model + " timeout. Mencoba model lain...";
                    } catch (Exception e) {
                        lastError = "Koneksi ke Gemini AI error: " + e.getMessage();
                        break;
                    }
                }
                return lastError;
            }

            @Override
            protected void done() {
                try {
                    txtAi.setText(get());
                } catch (Exception e) {
                    txtAi.setText("Error: " + e.getMessage());
                }
            }
        }.execute();
    }






    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        PilihanPasien = new javax.swing.JComboBox<>();
        PilihanDokter = new javax.swing.JComboBox<>();
        setStatus = new javax.swing.JComboBox<>();
        setJanji = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        setKeluhan = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAi = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnAi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();

        jLabel3.setText("Keluhan");

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CareSynce");

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 153));
        jLabel8.setText("Pasien");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 153));
        jLabel9.setText("Dokter");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 153, 153));
        jLabel12.setText("Janji Temu");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 153, 153));
        jLabel13.setText("Rekam Medis");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(35, 35, 35))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 153, 153));
        jLabel14.setText("Home");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(12, 12, 12)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PilihanPasien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PilihanPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PilihanPasienActionPerformed(evt);
            }
        });

        PilihanDokter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        setStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setStatusActionPerformed(evt);
            }
        });

        setKeluhan.setColumns(20);
        setKeluhan.setRows(5);
        jScrollPane1.setViewportView(setKeluhan);

        txtAi.setColumns(20);
        txtAi.setRows(5);
        jScrollPane2.setViewportView(txtAi);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        btnSave.setBackground(new java.awt.Color(0, 204, 204));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("simpan");
        btnSave.setMaximumSize(new java.awt.Dimension(100, 100));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(0, 204, 204));
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(204, 204, 0));
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.setPreferredSize(new java.awt.Dimension(87, 23));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnAi.setBackground(new java.awt.Color(51, 153, 255));
        btnAi.setForeground(new java.awt.Color(255, 255, 255));
        btnAi.setText("Generate");
        btnAi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAiActionPerformed(evt);
            }
        });

        jLabel1.setText("Tanggal");

        jLabel2.setText("Pilih Dokter");

        jLabel4.setText("Keluhan");

        jLabel5.setText("Pasien");

        jLabel6.setText("Status");

        jPanel9.setBackground(new java.awt.Color(0, 204, 204));

        jLabel15.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("CareSynce");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 153, 153));
        jLabel16.setText("Pasien");
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 153, 153));
        jLabel17.setText("Dokter");
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 153, 153));
        jLabel18.setText("Janji Temu");
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 153, 153));
        jLabel19.setText("Rekam Medis");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(35, 35, 35))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 153, 153));
        jLabel20.setText("Home");
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(12, 12, 12)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(PilihanPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PilihanDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAi, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setJanji, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(83, 83, 83)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PilihanPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PilihanDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setJanji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(jScrollPane1)))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnAi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PilihanPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PilihanPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PilihanPasienActionPerformed

    private void setStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setStatusActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (selectedJanjiTemuId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus janji temu ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM janji_temu WHERE id = ?");
            ps.setInt(1, selectedJanjiTemuId);
            int affected = ps.executeUpdate();
            ps.close();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Data janji temu berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnAiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAiActionPerformed
        String pasien = PilihanPasien.getSelectedItem() != null ? PilihanPasien.getSelectedItem().toString() : "";
        String namaDokter = PilihanDokter.getSelectedItem() != null ? PilihanDokter.getSelectedItem().toString() : "";
        String keluhan = setKeluhan.getText().trim();

        if (pasien.equals("-- Pilih Pasien --") || pasien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih pasien terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (namaDokter.equals("-- Pilih Dokter --") || namaDokter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih dokter terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (keluhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan isi keluhan pasien terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil spesialisasi dokter dari DB untuk memperkaya prompt AI
        String spesialisasi = "";
        try {
            Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT spesialisasi FROM dokter WHERE nama = ?");
            ps.setString(1, namaDokter);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString("spesialisasi") != null) {
                spesialisasi = rs.getString("spesialisasi");
            }
            rs.close(); ps.close();
        } catch (Exception ex) {
            // Jika kolom spesialisasi tidak ada, lanjutkan dengan nama dokter saja
        }
        String dokterInfo = spesialisasi.isEmpty() ? namaDokter : namaDokter + " (" + spesialisasi + ")";

        txtAi.setText("Menghubungi Gemini AI...");
        getAiRecommendationAsync(pasien, dokterInfo, keluhan);
    }//GEN-LAST:event_btnAiActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         // Validasi input
    String namaPasien = PilihanPasien.getSelectedItem() != null ? PilihanPasien.getSelectedItem().toString() : "";
    String namaDokter = PilihanDokter.getSelectedItem() != null ? PilihanDokter.getSelectedItem().toString() : "";
    String keluhan   = setKeluhan.getText().trim();
    String status    = setStatus.getSelectedItem() != null ? setStatus.getSelectedItem().toString() : "";
    java.util.Date tgl = setJanji.getDate();
    if (namaPasien.equals("-- Pilih Pasien --") || namaPasien.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (namaDokter.equals("-- Pilih Dokter --") || namaDokter.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih dokter terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (keluhan.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Keluhan tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (tgl == null) {
        JOptionPane.showMessageDialog(this, "Tanggal janji tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        Connection conn = Koneksi.getConnection();
        // Ambil pasien_id berdasarkan nama
        PreparedStatement psPasien = conn.prepareStatement("SELECT id FROM pasien WHERE nama = ?");
        psPasien.setString(1, namaPasien);
        ResultSet rsPasien = psPasien.executeQuery();
        if (!rsPasien.next()) {
            JOptionPane.showMessageDialog(this, "Pasien tidak ditemukan di database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int pasienId = rsPasien.getInt("id");
        rsPasien.close(); psPasien.close();
        // Ambil dokter_id berdasarkan nama
        PreparedStatement psDokter = conn.prepareStatement("SELECT id FROM dokter WHERE nama = ?");
        psDokter.setString(1, namaDokter);
        ResultSet rsDokter = psDokter.executeQuery();
        if (!rsDokter.next()) {
            JOptionPane.showMessageDialog(this, "Dokter tidak ditemukan di database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int dokterId = rsDokter.getInt("id");
        rsDokter.close(); psDokter.close();
        // Insert data janji temu
        String sql = "INSERT INTO janji_temu (pasien_id, dokter_id, tanggal_waktu, keluhan, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pasienId);
        ps.setInt(2, dokterId);
        ps.setTimestamp(3, new java.sql.Timestamp(tgl.getTime()));
        ps.setString(4, keluhan);
        ps.setString(5, status);
        ps.executeUpdate();
        ps.close();
        JOptionPane.showMessageDialog(this, "Data janji temu berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        loadData();
        clearFields();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (selectedJanjiTemuId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diubah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String namaPasien = PilihanPasien.getSelectedItem() != null ? PilihanPasien.getSelectedItem().toString() : "";
        String namaDokter = PilihanDokter.getSelectedItem() != null ? PilihanDokter.getSelectedItem().toString() : "";
        String keluhan    = setKeluhan.getText().trim();
        String status     = setStatus.getSelectedItem() != null ? setStatus.getSelectedItem().toString() : "";
        java.util.Date tgl = setJanji.getDate();
        if (namaPasien.equals("-- Pilih Pasien --") || namaPasien.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (namaDokter.equals("-- Pilih Dokter --") || namaDokter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih dokter terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (keluhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keluhan tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tgl == null) {
            JOptionPane.showMessageDialog(this, "Tanggal janji tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Connection conn = Koneksi.getConnection();
            // Ambil pasien_id
            PreparedStatement psPasien = conn.prepareStatement("SELECT id FROM pasien WHERE nama = ?");
            psPasien.setString(1, namaPasien);
            ResultSet rsPasien = psPasien.executeQuery();
            if (!rsPasien.next()) {
                JOptionPane.showMessageDialog(this, "Pasien tidak ditemukan di database!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int pasienId = rsPasien.getInt("id");
            rsPasien.close(); psPasien.close();
            // Ambil dokter_id
            PreparedStatement psDokter = conn.prepareStatement("SELECT id FROM dokter WHERE nama = ?");
            psDokter.setString(1, namaDokter);
            ResultSet rsDokter = psDokter.executeQuery();
            if (!rsDokter.next()) {
                JOptionPane.showMessageDialog(this, "Dokter tidak ditemukan di database!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dokterId = rsDokter.getInt("id");
            rsDokter.close(); psDokter.close();
            // Update data
            String sql = "UPDATE janji_temu SET pasien_id = ?, dokter_id = ?, tanggal_waktu = ?, keluhan = ?, status = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pasienId);
            ps.setInt(2, dokterId);
            ps.setTimestamp(3, new java.sql.Timestamp(tgl.getTime()));
            ps.setString(4, keluhan);
            ps.setString(5, status);
            ps.setInt(6, selectedJanjiTemuId);
            int affected = ps.executeUpdate();
            ps.close();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Data janji temu berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang diperbarui (ID tidak ditemukan).", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        new index().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        new PasienPanel().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        new DokterPanel().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel17MouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        new JanjiTemuPanel().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        new RekamMedisPanel().setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel19MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JanjiTemuPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JanjiTemuPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JanjiTemuPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JanjiTemuPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JanjiTemuPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> PilihanDokter;
    private javax.swing.JComboBox<String> PilihanPasien;
    private javax.swing.JButton btnAi;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser setJanji;
    private javax.swing.JTextArea setKeluhan;
    private javax.swing.JComboBox<String> setStatus;
    private javax.swing.JTextArea txtAi;
    // End of variables declaration//GEN-END:variables
}
