package lvsa.tpcalendar.dbutils.proxies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;

public class LoginDBProxy extends BaseDBProxy implements AutoCloseable {
    public LoginDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }

    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement("""
            INSERT INTO users (name, email, password)
            VALUES (?, ?, ?);
        """);

        Gson gson = new Gson();
        JsonElement loginJsonEl;
        try {
            loginJsonEl = gson.fromJson(json, JsonElement.class);
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }
        
        if (loginJsonEl.isJsonObject()) {
            JsonObject jobj = loginJsonEl.getAsJsonObject();
            Map<String, JsonElement> map = jobj.asMap();

            System.out.println(map.get("email"));
            System.out.println(map.get("password"));
        }

        stat.cancel();
        // stat.executeUpdate();

        return HTTPStatusCode.HTTP_200_OK;
    }

    public String read(int hashcode) throws SQLException {
        return "";
    }

    public HTTPStatusCode delete(int hashcode) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    // public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
    //     return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    // }

    // public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
    //     return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    // }
}
