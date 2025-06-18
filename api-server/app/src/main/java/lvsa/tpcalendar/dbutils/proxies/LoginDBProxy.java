package lvsa.tpcalendar.dbutils.proxies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lvsa.tpcalendar.auth.AuthResult;
import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;

public class LoginDBProxy extends BaseDBProxy implements AutoCloseable {
    private AuthResult authResult;

    public LoginDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }

    public AuthResult getAuthResult() {
        return authResult;
    }

    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT id, name, email, password
            FROM users
            WHERE email = ?;
        """);

        Gson gson = new Gson();
        JsonElement loginJsonEl;
        HTTPStatusCode status;
        ResultSet rs;

        try {
            loginJsonEl = gson.fromJson(json, JsonElement.class);
            if (loginJsonEl.isJsonNull()) {
                status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
                authResult = new AuthResult("Empty JSON request.", status);
                return status; // Empty JSON request.
            }
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
            authResult = new AuthResult("Malformed JSON request", status);
            return status; // Malformed JSON request.
        }
        
        if (loginJsonEl.isJsonObject()) {
            JsonObject jobj = loginJsonEl.getAsJsonObject();
            Map<String, JsonElement> map = jobj.asMap();

            query.setString(1, map.get("email").getAsString());
            
            rs = query.executeQuery();

            if (rs.next()) {
                status = rs.getString("email").equals(map.get("email").getAsString()) &&
                        rs.getString("password").equals(map.get("password").getAsString()) ?
                    HTTPStatusCode.HTTP_200_OK : // Log in successful.
                    HTTPStatusCode.HTTP_401_UNAUTHORIZED; // Incorrect credentials.
                authResult = new AuthResult(rs.getInt("id"), status);
                return status;
            } else {
                status = HTTPStatusCode.HTTP_404_NOT_FOUND;
                authResult = new AuthResult("Account does not exist.", status);
                return status; // Account does not exist.
            }
        }

        status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        authResult = new AuthResult("Unidentified fallback error.", status);
        return status; // Fallback error.
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
        
        System.out.println(id);
        query.setInt(1, id);

        ResultSet rs = query.executeQuery();
        Gson gson = new Gson();

        if (!rs.next()) {
            authResult = new AuthResult("", HTTPStatusCode.HTTP_404_NOT_FOUND);
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
            } catch (NullPointerException | JsonSyntaxException | IllegalStateException e) {
                taskevents_id_arr = gson.fromJson(
                    "[]",
                    int[].class);
                timeblockevents_id_arr = gson.fromJson(
                    "[]",
                    int[].class);
                textevents_id_arr = gson.fromJson(
                    "[]",
                    int[].class);
            }

            System.out.println(rs.toString());
            authResult = new AuthResult(rs.getInt("user_id"), HTTPStatusCode.HTTP_200_OK);
            return String.valueOf(rs.getInt("user_id"));
            
            // System.out.println(calspace_id);
            // System.out.println(taskevents_id_arr.length);
        }

        // return super.read(id);//delet thsi
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
