package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.schemas.json.*;
import lvsa.tpcalendar.utils.PropsService;

public class DBConnProvider implements AutoCloseable {
    private Connection conn;

    private <T> ViewType invoke(Callable<ViewType> c) throws Exception {
        return c.call();
    }

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
     * @return a task from database as JSON object or a JSON server error response. 
     * @throws SQLException
     */
    public String queryByHashcode(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT hashcode, datetime, name, description, viewtype, color, isdone
            FROM taskevents
            WHERE hashcode = ?;
        """);
        query.setInt(1, hashcode);

        Gson gson = new Gson();
        String json = "";

        try {
            ResultSet rs = query.executeQuery();
            if (!rs.next()) {
                return json;
            } else {
                TaskOut task = new TaskOut(
                    rs.getInt("hashcode"), 
                    rs.getString("datetime"),
                    rs.getString("name"),
                    rs.getString("description"),
                    invoke(() -> { return ViewType.toViewType(rs.getString("viewtype")); }),
                    new ColorObj(
                        rs.getString("color") == "" ? false : true,
                        rs.getString("color")
                    ),
                    rs.getBoolean("isdone")
                );

                json = gson.toJson(task);
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
    }

    /**
     * Insert a task into the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public HTTPStatusCode createTask(String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO taskevents 
            (hashcode, datetime, name, description, viewtype, color, isdone)
            VALUES (?, ?, ?, ?, ?, ?, ?);           
        """);

        Gson gson = new Gson();
        TaskIn task;
        try {
            task = gson.fromJson(json, TaskIn.class);
            try {
                TaskIn.initHashCode(task);
                if (task.getViewType() == null) return HTTPStatusCode.HTTP_400_BAD_REQUEST;
            } catch (DateTimeParseException dtpe) {
                dtpe.printStackTrace();
                return HTTPStatusCode.HTTP_400_BAD_REQUEST;
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
        stat.setObject(5, task.getViewType(), Types.OTHER);
        stat.setString(6, "#" + task.getColor().getHex());
        stat.setBoolean(7, task.isDone());

        stat.executeUpdate();

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    /**
     * Delete a task from the database. Queries the task by hashcode.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public HTTPStatusCode deleteTask(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            DELETE FROM taskevents WHERE hashcode = ?;
        """);

        stat.setInt(1, hashcode);
        final int PG_STATUS = stat.executeUpdate();

        return PG_STATUS > 0 ?
            HTTPStatusCode.HTTP_200_OK :
            HTTPStatusCode.HTTP_404_NOT_FOUND;
    }

    /**
     * Update the whole task in the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public HTTPStatusCode updateWholeTask(int hashcode, String json) throws SQLException {
        PreparedStatement hashcodeQuery = this.conn.prepareStatement("""
            SELECT hashcode, datetime, name, description,
                   viewtype, color, isdone
            FROM taskevents
            WHERE hashcode = ?;         
        """);
        hashcodeQuery.setInt(1, hashcode);
        Gson gson = new Gson();
        TaskOut taskCheck;
        try {
            ResultSet rs = hashcodeQuery.executeQuery();
            if (!rs.next()) {
                return HTTPStatusCode.HTTP_404_NOT_FOUND;
            } else {
                taskCheck = new TaskOut(
                    rs.getInt("hashcode"), 
                    rs.getString("datetime"),
                    rs.getString("name"),
                    rs.getString("description"),
                    invoke(() -> { return ViewType.toViewType(rs.getString("viewtype")); }),
                    new ColorObj(
                        rs.getString("color") == "" ? false : true,
                        rs.getString("color")
                    ),
                    rs.getBoolean("isdone")
                );

                if (gson.toJson(taskCheck) != json) {
                    return HTTPStatusCode.HTTP_404_NOT_FOUND;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: What about updating the hashcode to match the new state of the task? Or should those be preserved?
        PreparedStatement updateStat = this.conn.prepareStatement("""
            UPDATE taskevents SET 
                datetime = ?, name = ?, description = ?,
                viewtype = ?, color = ?, isdone = ?
            WHERE hashcode = ?;
        """);

        TaskIn task;
        try {
            task = gson.fromJson(json, TaskIn.class);
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }

        LocalDateTime ldt = LocalDateTime.parse(task.getDatetime()); 
        updateStat.setTimestamp(1, new Timestamp(ldt.toInstant(ZoneOffset.UTC).toEpochMilli()));
        updateStat.setString(2, task.getName());
        updateStat.setString(3, task.getDesc());
        updateStat.setObject(4, task.getViewType(), Types.OTHER);
        updateStat.setString(5, "#" + task.getColor().getHex());
        updateStat.setBoolean(6, task.isDone());
        updateStat.setInt(7, hashcode);

        final int PG_STATUS = updateStat.executeUpdate();

        return PG_STATUS > 0 ?
            HTTPStatusCode.HTTP_200_OK :
            HTTPStatusCode.HTTP_404_NOT_FOUND;
    }

    /**
     * Partially the task in the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    public HTTPStatusCode updatePartialTask(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            UPDATE taskevents SET 
                datetime = ?, name = ?, description = ?,
                viewtype = ?, color = ?, isdone = ?
            WHERE hashcode = ?;
        """);
        // TODO: What about updating the hashcode to match the new state of the task? Or should those be preserved?

        stat.setInt(1, hashcode);
        final int PG_STATUS = stat.executeUpdate();

        return PG_STATUS > 0 ?
            HTTPStatusCode.HTTP_200_OK :
            HTTPStatusCode.HTTP_404_NOT_FOUND;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}