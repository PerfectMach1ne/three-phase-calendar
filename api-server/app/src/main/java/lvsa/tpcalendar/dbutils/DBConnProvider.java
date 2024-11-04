
package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lvsa.tpcalendar.util.PropsService;

public class DBConnProvider implements AutoCloseable {
    private Connection conn;

    public DBConnProvider() throws SQLException {
        Properties props = new PropsService().getDBProps();
        String url = props.getProperty("url");
        props.remove("url");
        conn = DriverManager.getConnection(url, props);
    }

    public Connection getDBConnection() {
        return conn;
    }

    /**
     * Pretty function that has done nothing wrong ever~! <3
     * @param hashcode
     * @return a good dream 
     * @throws SQLException
     */
    public Map<String, String> queryByHashcode(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT * 
            FROM taskevents
            WHERE hashcode = ?;
        """);
        query.setInt(1, hashcode);

        Gson gson = new Gson();
        Map<String, String> json = new LinkedHashMap<>();

        ResultSet rs = query.executeQuery();
        if (!rs.next()) {
            return json;
        } else {
            json.put("hashcode", String.valueOf(rs.getInt("hashcode")));
            json.put("datetime", rs.getString("datetime"));
            json.put("name", rs.getString("name"));
            json.put("desc", rs.getString("description"));

            Map<String, String> colorObj = new LinkedHashMap<>();
            colorObj.put("hasColor", 
                String.valueOf(
                    rs.getString("color") == "" ? false : true));
            colorObj.put("hex", rs.getString("color"));
            // Bugged, it just stringifies it, messing with the final
            // request deserialization on client side.
            json.put("color", gson.toJson(colorObj));
            json.put("isDone", String.valueOf(rs.getBoolean("isdone")));
            json.put("createdAt", rs.getString("createdat"));
            json.put("updatedAt", rs.getString("updatedat"));

            return json;
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}