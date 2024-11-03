
package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

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