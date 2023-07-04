import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    public static void createTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS todos (id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR(255), done boolean)";
        Statement statement = connection.createStatement();
        statement.execute(createTableQuery);
        statement.close();
    }
}
