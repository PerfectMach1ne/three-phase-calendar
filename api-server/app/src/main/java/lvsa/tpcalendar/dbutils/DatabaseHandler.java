
package lvsa.tpcalendar.dbutils;

import java.sql.SQLException;
import java.sql.DriverManager;

import java.sql.Connection;

public class DatabaseHandler implements AutoCloseable {
    private Connection conn;

    public DatabaseHandler() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:DATABASE/calendar-sqlite.db");
    }

    public Connection getDBConnection() {
        return conn;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    // Statement stat = conn.createStatement();
}