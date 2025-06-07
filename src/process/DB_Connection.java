package process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility for obtaining database connections using environment variables.
 */
public class DB_Connection {

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Connected with the database successfully");
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String host = System.getenv("DB_HOST");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String db = System.getenv("DB_NAME_537");
        String url = String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;loginTimeout=30;", host, db, user, password);
        return DriverManager.getConnection(url);
    }
}
