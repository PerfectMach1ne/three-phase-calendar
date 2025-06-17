package lvsa.tpcalendar.dbutils.proxies;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT name, email, password
            FROM users
            WHERE email = ?;
        """);

        Gson gson = new Gson();
        JsonElement loginJsonEl;
        ResultSet rs;

        try {
            loginJsonEl = gson.fromJson(json, JsonElement.class);
            if (loginJsonEl.isJsonNull()) {
                return HTTPStatusCode.HTTP_400_BAD_REQUEST; // Empty JSON request.
            }
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST; // Malformed JSON request.
        }
        
        if (loginJsonEl.isJsonObject()) {
            JsonObject jobj = loginJsonEl.getAsJsonObject();
            Map<String, JsonElement> map = jobj.asMap();

            query.setString(1, map.get("email").getAsString());
            
            rs = query.executeQuery();

            if (rs.next()) {
                return rs.getString("email").equals(map.get("email").getAsString()) &&
                        rs.getString("password").equals(map.get("password").getAsString()) ?
                    HTTPStatusCode.HTTP_200_OK : // Log in successful.
                    HTTPStatusCode.HTTP_401_UNAUTHORIZED; // Incorrect credentials.
            } else {
                return HTTPStatusCode.HTTP_404_NOT_FOUND; // Account does not exist.
            }
        }

        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR; // Fallback error.
    }

    @Override
    public String read(int id) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT u.id, cs.user_id, cs.id AS calspace_id,
                cs.tasksevents_id_arr, cs.timeblockevents_id_arr, cs.textevents_id_arr
            FROM users u RIGHT JOIN calendarspace cs
            ON u.id = cs.user_id
            WHERE u.id = ?;
        """);
        
        query.setInt(1, id);

        ResultSet rs = query.executeQuery();
        Gson gson = new Gson();

        if (!rs.next()) {
            return ""; // Calendarspace or user not found.
        } else {
            int calspace_id = gson.fromJson(
                String.valueOf(rs.getInt("calspace_id")),
                int.class);

            int[] taskevents_id_arr;
            int[] timeblockevents_id_arr;
            int[] textevents_id_arr;
            
            try {
                taskevents_id_arr = gson.fromJson(
                    rs.getArray("tasksevents_id_arr")/*.getArray()*/.toString(),
                    int[].class);

                timeblockevents_id_arr = gson.fromJson(
                    rs.getArray("timeblockevents_id_arr")/*.getArray()*/.toString(),
                    int[].class);

                textevents_id_arr = gson.fromJson(
                    rs.getArray("textevents_id_arr")/*.getArray()*/.toString(),
                    int[].class);
            } catch (NullPointerException npe) {
                taskevents_id_arr = gson.fromJson(
                    "[]",
                    int[].class);
            }
            
            System.out.println(calspace_id);
            System.out.println(taskevents_id_arr.length);
        }

        return super.read(id);//delet thsi
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
