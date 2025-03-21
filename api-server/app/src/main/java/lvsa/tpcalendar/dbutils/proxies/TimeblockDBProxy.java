package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.Gson;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;

public class TimeblockDBProxy extends BaseDBProxy implements AutoCloseable {
  	public TimeblockDBProxy(DBConnProvider dbConnProvider) {
		super(dbConnProvider);
  	}

	@Override
    public HTTPStatusCode create(String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

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
