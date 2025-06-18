package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;

public class CalSpaceDBProxy extends BaseDBProxy implements AutoCloseable {
    protected CalSpaceDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }

    public HTTPStatusCode create(String id_str) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO calendarspace (user_id)
            VALUES (?);
        """);

        int user_id = Integer.valueOf(id_str);

        stat.setInt(1, user_id);
        stat.executeUpdate();

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    public String read(int hashcode) throws SQLException {
        return "";
    }

    public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    public HTTPStatusCode delete(int hashcode) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
