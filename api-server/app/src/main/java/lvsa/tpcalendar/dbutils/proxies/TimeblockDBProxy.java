package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            INSERT INTO taskevents 
            (hashcode, start_datetime, end_datetime, name, description, viewtype, color)
            VALUES (?, ?, ?, ?, ?, ?, ?);           
        """);

        Gson gson = new Gson();
        TimeblockIn timeblock;
        try {
            timeblock = gson.fromJson(json, TimeblockIn.class);
            System.out.println(timeblock);
        } catch(JsonParseException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }

        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
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
