package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.util.PropsService;
import lvsa.tpcalendar.http.HTTPStatusCode;
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
            TaskOut task = new TaskOut(
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

            json = gson.toJson(task);
            return json;
        }
    }

    /**
     * <b>[WIP]</b> Insert a task into the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public HTTPStatusCode insertTask(String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO taskevents 
            (hashcode, datetime, name, description, color, isdone)
            VALUES (?, ?, ?, ?, ?, ?);           
        """);

        Gson gson = new Gson();
        TaskIn task;
        try {
            try {
                task = gson.fromJson(json, TaskIn.class);
                TaskIn.initHashCode(task);
                System.out.println(task.getHashcode());
            } catch (NullPointerException e) {
                return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
            }
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }

        stat.setInt(1, task.getHashcode());
        LocalDateTime ldt = LocalDateTime.parse(task.getDatetime()); 
        stat.setTimestamp(2, new Timestamp(ldt.toInstant(ZoneOffset.UTC).toEpochMilli()));
        stat.setString(3, task.getName());
        stat.setString(4, task.getDesc());
        stat.setString(5, "#" + task.getColor().getHex());
        stat.setBoolean(6, task.isDone());

        stat.executeUpdate();

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    /**
     * <b>[WIP]</b> Delete a task from the database. Queries the task by hashcode.
     * @param hashcode
     * @return
     * @throws SQLException
     */
    public short deleteTask(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            DELETE FROM taskevents WHERE hashcode = ?;
        """);

        /* TODO */
        stat.executeUpdate();
        
        return 0;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

}