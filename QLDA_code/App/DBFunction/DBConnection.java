package DBFunction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "Password.1";

    // Phương thức tĩnh để gọi ở mọi nơi
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  // Chỉ cần gọi 1 lần nếu chưa có
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
