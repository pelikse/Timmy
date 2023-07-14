import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    // Method for creating the "todos" table in the database
    public static void createTable(Connection connection) throws SQLException {
        // SQL query to create the table if it doesn't exist
        String createTableQuery = "CREATE TABLE IF NOT EXISTS todos (id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR(255), done boolean)";
        // Creating a statement object to execute the query
        Statement statement = connection.createStatement();
        // Executing the create table query
        statement.execute(createTableQuery);
        // Closing the statement
        statement.close();
    }
}
