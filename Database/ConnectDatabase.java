package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {

    private static ConnectDatabase instance;
    private static Connection connection;

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "CAKE_MANAGEMENT";
    private static final String USER_NAME = "root";     // thường MySQL dùng root
    private static final String PASSWORD = "";         // XAMPP mặc định rỗng

    private static final String URL =
            "jdbc:mysql://" + SERVER_NAME + ":3306/"
            + DATABASE_NAME;

    private ConnectDatabase() {
    }

    public static ConnectDatabase getInstance() {
        if (instance == null) {
            instance = new ConnectDatabase();
        }
        return instance;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                // Load MySQL Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println("Kết nối MySQL thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy MySQL JDBC Driver!");
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối MySQL!");
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection conn = db.getConnection();

        if (conn != null) {
            System.out.println("Test kết nối MySQL thành công!");
        } else {
            System.out.println("Test kết nối thất bại!");
        }

        db.closeConnection();
    }
}