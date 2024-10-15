
package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.InputStream;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Iterator;

import lvsa.tpcalendar.util.IOUtils;

public class DatabaseHandler implements AutoCloseable {
    private Connection conn;

    public DatabaseHandler() throws SQLException {
        Properties props = loadProperties();
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

    public JsonObject queryByHashcode(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("SELECT * FROM taskevents WHERE hashcode = ?;");
        query.setInt(1, hashcode);

        JsonObject json = new JsonObject();

        ResultSet rs = query.executeQuery();
        if (!rs.next()) {
            return json;
        } else {
            json.add("hashcode", new JsonPrimitive(rs.getInt("hashcode")));
            json.add("datetime", new JsonPrimitive(rs.getString("datetime")));
            json.add("name", new JsonPrimitive(rs.getString("name")));
            json.add("desc", new JsonPrimitive(rs.getString("description")));
            JsonObject colorObj = new JsonObject();
            colorObj.add("hasColor", 
                new JsonPrimitive(rs.getString("color") == "" ? false : true));
            colorObj.add("hex", new JsonPrimitive(rs.getString("color")));
            json.add("color", colorObj);
            json.add("isDone", new JsonPrimitive(rs.getBoolean("isdone")));
            json.add("createdAt", new JsonPrimitive(rs.getString("createdat")));
            json.add("updatedAt", new JsonPrimitive(rs.getString("updatedat")));

            return json;
        }
    }

    public Connection getDBConnection() {
        return conn;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}