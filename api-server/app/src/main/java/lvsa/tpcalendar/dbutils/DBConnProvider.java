package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;

import lvsa.tpcalendar.utils.PropsService;

public class DBConnProvider implements AutoCloseable {
    private Connection conn;

    public DBConnProvider() throws SQLException {
        Properties props = new PropsService().getDBProps();
        String url = props.getProperty("url");
        props.remove("url");
        conn = DriverManager.getConnection(url, props);
    }

    public Connection getDBConnection() { return conn; }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}