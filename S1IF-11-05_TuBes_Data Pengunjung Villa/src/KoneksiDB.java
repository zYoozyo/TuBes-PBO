import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Koneksi ke database MySQL
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/villa_db",
                "root", // username MySQL
                ""      // password MySQL (kosong jika default)
            );
            System.out.println("Koneksi ke database berhasil.");
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return conn;
    }

    // Untuk mencoba koneksi
    public static void main(String[] args) {
        getConnection();
    }
}