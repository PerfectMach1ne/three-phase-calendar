package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.schemas.json.*;

public class TaskDBProxy extends BaseDBProxy implements AutoCloseable {
    private <T> ViewType invoke(Callable<ViewType> c) throws Exception {
        return c.call();
    }

    public TaskDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }
    
    /**
     * Insert a task into the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    @Override
    public HTTPStatusCode create(String json) throws SQLException {
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
        // TODO: ZoneOffset.of(...) should be configurable to allow user to adjust their timezone.
        // Previously this was UTC, but I changed it to make it Work On My Machine(TM).
        stat.setTimestamp(2, new Timestamp(ldt.toInstant(ZoneOffset.of("+1")).toEpochMilli()));
        stat.setString(3, task.getName());
        stat.setString(4, task.getDesc());
        stat.setObject(5, task.getViewType(), Types.OTHER);
        stat.setString(6, "#" + task.getColor().getHex());
        stat.setBoolean(7, task.isDone());

        stat.executeUpdate();

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    /**
     * Query a task from a database by its hashcode.
     * @param hashcode
     * @return a task from database as JSON object or a JSON server error response. 
     * @throws SQLException
     */
    @Override
    public String read(int hashcode) throws SQLException {
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
     * Update the whole task in the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    @Override
    public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
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
				/*
				 * This is supposed to check if the task from database is identical
				 * to the one we're trying to insert. In which case, return
				 * HTTP_304_NOT_MODIFIED.
				 */
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
				
				System.out.println(json.stripLeading());
				System.out.println(gson.toJson(taskCheck));
				/** 
				 * FIXME: taskCheck returned from db and json have inconsistent formatting of the JSON.
                 * Additionally, some data such as date and color is inconsistent by design, e.g.:
                 * * sout(taskCheck) returns:
                 * ... "datetime": "2024-11-30T13:40:09", ...
                 * ... "hex": "573849") ...
                 * * sout(json) returns:
                 * ... "datetime": "2024-11-30 14:40:09", ...
                 * ... "hex": "#573849") ...
				 */
                if (gson.toJson(taskCheck) == json.stripLeading()) {
                    return HTTPStatusCode.HTTP_304_NOT_MODIFIED;
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
    @Override
    public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
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

    /**
     * Delete a task from the database. Queries the task by hashcode.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
    @Override
    public HTTPStatusCode delete(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            DELETE FROM taskevents WHERE hashcode = ?;
        """);

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