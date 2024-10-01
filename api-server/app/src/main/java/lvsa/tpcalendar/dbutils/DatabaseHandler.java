
package lvsa.tpcalendar.dbutils;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseHandler implements AutoCloseable {
    private Connection conn;

    public DatabaseHandler() throws SQLException {
        // conn = DriverManager.getConnection("jdbc:sqlite:DATABASE/calendar-sqlite.db");
        Properties props = loadProperties();
        String url = props.getProperty("url");
        props.remove("url");
        conn = DriverManager.getConnection(url, props);
        // conn = DriverManager.getConnection(url, props);
    }

    // https://www.baeldung.com/reading-file-in-java
    // Helper method
    private ArrayList<String> readFromInputStream(InputStream is) {
        ArrayList<String> propsList = new ArrayList<String>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ( (line = br.readLine()) != null ) {
                propsList.add(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return propsList;
    }

    private Properties loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("tpc_testing.properties");

        Properties props = new Properties();
        Iterator<String> iter = readFromInputStream(is).iterator();
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