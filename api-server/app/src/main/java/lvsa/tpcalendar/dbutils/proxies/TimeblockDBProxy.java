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
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.schemas.json.ColorObj;
import lvsa.tpcalendar.schemas.json.TimeblockIn;
import lvsa.tpcalendar.schemas.json.TimeblockOut;
import lvsa.tpcalendar.schemas.json.ViewType;

public class TimeblockDBProxy extends BaseDBProxy implements AutoCloseable {
    private int uid;

    public void setUid(int uid) {
        this.uid = uid;
    }

    private <T> ViewType invoke(Callable<ViewType> c) throws Exception {
        return c.call();
    }

  	public TimeblockDBProxy(DBConnProvider dbConnProvider) {
		super(dbConnProvider);
  	}

    /**
     * <p>Insert a timeblock into the database.</p>
     * 
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
	@Override
    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO timeblockevents
            (hashcode, start_datetime, end_datetime, name, description, viewtype, color)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id;           
        """);

        Gson gson = new Gson();
        TimeblockIn timeblock;
        try {
            timeblock = gson.fromJson(json, TimeblockIn.class);
            try {
                TimeblockIn.initHashCode(timeblock);
                if (timeblock.getViewType() == null) return HTTPStatusCode.HTTP_400_BAD_REQUEST;
            } catch(DateTimeParseException dtpe) {
                dtpe.printStackTrace();
                return HTTPStatusCode.HTTP_400_BAD_REQUEST;
            }
        } catch(JsonParseException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }

        stat.setInt(1, timeblock.getHashcode());
        LocalDateTime start_ldt = LocalDateTime.parse(timeblock.getStartDatetime()); 
        LocalDateTime end_ldt = LocalDateTime.parse(timeblock.getEndDatetime()); 
        // TODO: ZoneOffset.of(...) should be configurable to allow user to adjust their timezone.
        // Previously this was UTC, but I changed it to make it Work On My Machine(TM).
        stat.setTimestamp(2, new Timestamp(start_ldt.toInstant(ZoneOffset.of("+1")).toEpochMilli()));
        stat.setTimestamp(3, new Timestamp(end_ldt.toInstant(ZoneOffset.of("+1")).toEpochMilli()));
        stat.setString(4, timeblock.getName());
        stat.setString(5, timeblock.getDesc());
        stat.setObject(6, timeblock.getViewType(), Types.OTHER);
        stat.setString(7, timeblock.getColor().getHex().startsWith("#")
                ? timeblock.getColor().getHex()
                : '#' + timeblock.getColor().getHex() );
        
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        ResultSet rs = stat.executeQuery();

        if (rs.next()) {
            PreparedStatement cspace = this.conn.prepareStatement("""
                INSERT INTO calendarspace
                (user_id, event_id, event_hashcode, event_type)
                VALUES (?, ?, ?, 'timeblock');
            """);

            cspace.setInt(1, this.uid);
            cspace.setInt(2, rs.getInt("id"));
            cspace.setInt(3, timeblock.getHashcode());

            cspace.executeUpdate();

            status = HTTPStatusCode.HTTP_201_CREATED;
        } else {
            return status;
        }

        return status;
    }

    /**
     * <p>Query a timeblock from a database by its hashcode.</p>
     * 
     * @param hashcode
     * @return a timeblock from database as JSON object or a JSON server error response. 
     * @throws SQLException
     */
	@Override
	public String read(int hashcode) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT hashcode, start_datetime, end_datetime, name, description, viewtype, color
            FROM timeblockevents
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
                TimeblockOut timeblock = new TimeblockOut(
                    rs.getInt("hashcode"), 
                    rs.getString("start_datetime"),
                    rs.getString("end_datetime"),
                    rs.getString("name"),
                    rs.getString("description"),
                    invoke(() -> { return ViewType.toViewType(rs.getString("viewtype")); }),
                    new ColorObj(
                        rs.getString("color") == "" ? false : true,
                        rs.getString("color"))
                );
                
                json = gson.toJson(timeblock);
                return json;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
	}

    /**
     * <p>Update the whole timeblock in the database.</p>
     * 
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
	@Override
    public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
        PreparedStatement hashcodeQuery = this.conn.prepareStatement("""
            SELECT hashcode, start_datetime, end_datetime, name,
                description, viewtype, color
            FROM timeblockevents 
            WHERE hashcode = ?;         
        """);
        hashcodeQuery.setInt(1, hashcode);
        Gson gson = new Gson();
        TimeblockOut timeblockCheck;

        try {
            ResultSet rs = hashcodeQuery.executeQuery();
            if (!rs.next()) {
                return HTTPStatusCode.HTTP_404_NOT_FOUND;
            } else {
				/*
				 * This is supposed to check if the timeblock from database is identical
				 * to the one we're trying to insert. In which case, return
				 * HTTP_304_NOT_MODIFIED.
				 */
                timeblockCheck = new TimeblockOut(
                    rs.getInt("hashcode"), 
                    rs.getString("start_datetime"),
                    rs.getString("end_datetime"),
                    rs.getString("name"),
                    rs.getString("description"),
                    invoke(() -> { return ViewType.toViewType(rs.getString("viewtype")); }),
                    new ColorObj(
                        rs.getString("color") == "" ? false : true,
                        rs.getString("color"))
                );

                if (gson.toJson(timeblockCheck) == json.stripLeading()) {
                    return HTTPStatusCode.HTTP_304_NOT_MODIFIED;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: What about updating the hashcode to match the new state of the task? Or should those be preserved?
        PreparedStatement updateStat = this.conn.prepareStatement("""
            UPDATE timeblockevents SET 
                start_datetime = ?, end_datetime = ?, name = ?,
                description = ?, viewtype = ?, color = ?
            WHERE hashcode = ?;
        """);

        TimeblockIn timeblock;
        try {
            timeblock = gson.fromJson(json, TimeblockIn.class);
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }

        LocalDateTime start_ldt = LocalDateTime.parse(timeblock.getStartDatetime()); 
        LocalDateTime end_ldt = LocalDateTime.parse(timeblock.getEndDatetime()); 
        // TODO: ZoneOffset.of(...) should be configurable to allow user to adjust their timezone.
        // Previously this was UTC, but I changed it to make it Work On My Machine(TM).
        updateStat.setTimestamp(1, new Timestamp(start_ldt.toInstant(ZoneOffset.UTC).toEpochMilli()));
        updateStat.setTimestamp(2, new Timestamp(end_ldt.toInstant(ZoneOffset.UTC).toEpochMilli()));
        updateStat.setString(3, timeblock.getName());
        updateStat.setString(4, timeblock.getDesc());
        updateStat.setObject(5, timeblock.getViewType(), Types.OTHER);
        updateStat.setString(6, timeblock.getColor().getHex().startsWith("#")
                ? timeblock.getColor().getHex()
                : '#' + timeblock.getColor().getHex() );
        updateStat.setInt(7, hashcode);

        final int PG_STATUS = updateStat.executeUpdate();

        return PG_STATUS > 0 ?
            HTTPStatusCode.HTTP_200_OK :
            HTTPStatusCode.HTTP_404_NOT_FOUND;
    }

    /**
     * <h4>WARNING: Unfinished implementation.</h4>
     * <p>Partially the timeblock in the database.</p>
     * 
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
	@Override
    public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            UPDATE timeblockevents SET 
                datetime = ?, name = ?, description = ?,
                viewtype = ?, color = ?, isdone = ?
            WHERE hashcode = ?;
        """);
        // TODO: What about updating the hashcode to match the new state of the timeblock event? Or should those be preserved?

        stat.setInt(1, hashcode);
        final int PG_STATUS = stat.executeUpdate();

        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
        // return PG_STATUS > 0 ?
        //     HTTPStatusCode.HTTP_200_OK :
        //     HTTPStatusCode.HTTP_404_NOT_FOUND;
    }

    /**
     * <p>Delete a timeblock from the database. Queries the timeblock by hashcode.</p>
     * 
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
	@Override
    public HTTPStatusCode delete(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            DELETE FROM timeblockevents WHERE hashcode = ?;
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
