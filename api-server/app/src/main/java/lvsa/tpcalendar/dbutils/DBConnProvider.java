
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
     * Query a task from a database by its hashcode.
     * @param hashcode
     * @return a task from database as JSON object. 
     * @throws SQLException
     */
    public String queryByHashcode(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT hashcode, datetime, name, description, color, isdone
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

    /**
     * <b>[WIP]</b> Insert a task into the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public short insertTask(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO taskevents 
            (hashcode, datetime, name, description, color, isdone)
            VALUES (?, ?, ?, ?, ?, ?);           
        """);
        return 0;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}