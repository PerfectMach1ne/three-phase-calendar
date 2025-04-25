package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.schemas.json.TimeblockIn;

public class TimeblockDBProxy extends BaseDBProxy implements AutoCloseable {
  	public TimeblockDBProxy(DBConnProvider dbConnProvider) {
		super(dbConnProvider);
  	}

    /**
     * Insert a timeblock into the database.
     * @param hashcode
     * @return operation status code.
     * @throws SQLException
     */
	@Override
    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO timeblockevents
            (hashcode, start_datetime, end_datetime, name, description, viewtype, color)
            VALUES (?, ?, ?, ?, ?, ?, ?);           
        """);

        Gson gson = new Gson();
        TimeblockIn timeblock;
        try {
            timeblock = gson.fromJson(json, TimeblockIn.class);
            try {
                TimeblockIn.initHashCode(timeblock);
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
        stat.setString(7, timeblock.getColor().getHex());
        
        stat.executeUpdate();

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    /**
     * Query a timeblock from a database by its hashcode.
     * @param hashcode
     * @return a task from database as JSON object or a JSON server error response. 
     * @throws SQLException
     */
	@Override
	public String read(int hashcode) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO timeblockevents 
            (hashcode, start_datetime, end_datetime, name, description, viewtype, color)
            VALUES (?, ?, ?, ?, ?, ?, ?);           
        """);

        Gson gson = new Gson();

        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
	}

	@Override
    public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

	@Override
    public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

	@Override
    public HTTPStatusCode delete(int hashcode) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

	@Override
	public void close() throws SQLException {
		conn.close();
	}
}
