
package lvsa.tpcalendar.dbutils;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseHandler implements AutoCloseable {
    private Connection conn;

    public DatabaseHandler() throws SQLException {
        // conn = DriverManager.getConnection("jdbc:sqlite:DATABASE/calendar-sqlite.db");
        ArrayList<String> props = loadProperties();
        Iterator<String> iter = props.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        String url = "jdbc:postgresql://127.0.0.1:5432/tpc_testing";
        // Properties props = new Properties();
        // props.setProperty("user", "postgres");
        // props.setProperty("password", "ohno");
        // props.setProperty("ssl", "true");
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

    private ArrayList<String> loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("tpc_testing.properties");

        return readFromInputStream(is);
    }

    public Connection getDBConnection() {
        return conn;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}