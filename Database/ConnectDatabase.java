package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
    private static Connection connection = null;
    private static ConnectDatabase instance = null;
    
    // Thông tin kết nối
    private static final String SERVER_NAME = "localhost"; 
    private static final String DATABASE_NAME = "CAKE_MANAGEMENT";
    private static final String USER_NAME = "admin_cake"; 
    private static final String PASSWORD = "admin"; 
    
    // URL kết nối
    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME + ":1433;"
            + "databaseName=" + DATABASE_NAME + ";"
            + "user=" + USER_NAME + ";"
            + "password=" + PASSWORD + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;";
    
    // Constructor private để implement Singleton pattern
    private ConnectDatabase() {
        try {
            // Load SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL);
            System.out.println("Kết nối database thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy SQL Server JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database!");
            e.printStackTrace();
        }
    }
    
    // Lấy instance duy nhất
    public static ConnectDatabase getInstance() {
        if (instance == null) {
            instance = new ConnectDatabase();
        }
        return instance;
    }
    
    // Lấy connection
    public static Connection getConnection() {
        if (instance == null) {
            getInstance();
        }
        return connection;
    }
    
    // Đóng kết nối
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đã đóng kết nối database!");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối!");
                e.printStackTrace();
            }
        }
    }
    
    // Test kết nối
    public static void main(String[] args) {
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection conn = ConnectDatabase.getConnection();
        
        if (conn != null) {
            System.out.println("Test kết nối thành công!");
        } else {
            System.out.println("Test kết nối thất bại!");
        }
        
        ConnectDatabase.closeConnection();
    }
}
