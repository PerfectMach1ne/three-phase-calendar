
package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.InputStream;
import java.util.Properties;
import java.util.Iterator;

import lvsa.tpcalendar.util.IOUtils;

public class DatabaseHandler implements AutoCloseable {
    private Connection conn;

    public DatabaseHandler() throws SQLException {
        Properties props = loadProperties();
        // System.out.println(props.toString());
        String url = props.getProperty("url");
        props.remove("url");
        conn = DriverManager.getConnection(url, props);
    }

    private Properties loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("tpc_testing.properties");

        Properties props = new Properties();
        Iterator<String> iter = IOUtils.readPropsFromInputStream(is).iterator();
        while (iter.hasNext()) {
            String[] propPair = iter.next().split("="); 
            props.setProperty(propPair[0], propPair[1]);
        }

        return props;
    }

    public Connection getDBConnection() {
        return conn;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}