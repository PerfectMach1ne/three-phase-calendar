
package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.google.gson.Gson;

import lvsa.tpcalendar.util.PropsService;
import lvsa.tpcalendar.schemas.json.*;

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
    public String queryByHashcode(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT * 
            FROM taskevents
            WHERE hashcode = ?;
        """);
        query.setInt(1, hashcode);

        Gson gson = new Gson();
        String json = "";

        ResultSet rs = query.executeQuery();
        if (!rs.next()) {
            return json;
        } else {
            Task jsonTask = new Task(
                rs.getInt("hashcode"), 
                rs.getString("datetime"),
                rs.getString("name"),
                rs.getString("description"),
                new ColorObj(
                    rs.getString("color") == "" ? false : true,
                    rs.getString("color")
                ),
                rs.getBoolean("isdone")
            );

            json = gson.toJson(jsonTask);

            return json;
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}