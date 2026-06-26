import java.sql.*;

public class CheckTableStructure {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/klinik?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
            Connection con = DriverManager.getConnection(url, "root", "");
            
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, "dokter", null);
            
            System.out.println("=== Struktur Tabel DOKTER ===");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String dataType = rs.getString("TYPE_NAME");
                int columnSize = rs.getInt("COLUMN_SIZE");
                boolean nullable = rs.getInt("NULLABLE") == 1;
                
                System.out.println(columnName + " | " + dataType + "(" + columnSize + ") | Nullable: " + nullable);
            }
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
