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

    /**
     * Fetch userdata from the database
     */
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
        authResult = new AuthResult("Unidentified fallback server error triggered.", status);
        return status; // Fallback error.
    }

    @Override
    public String read(int id) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            WITH cspace AS (
            SELECT u.id,
                cs.user_id AS user_id,
                cs.id AS calspace_id,
                cs.event_id,
                cs.event_hashcode,
                cs.event_type
            FROM users u RIGHT JOIN calendarspace cs
            ON u.id = cs.user_id WHERE u.id = ?
            )
            SELECT json_build_object(
            'userdata', (SELECT json_agg(u) FROM (
                SELECT user_id FROM cspace
            ) u),
            'tasks', (SELECT json_agg(t) FROM (
                SELECT t.id, t.hashcode, t.datetime, t.name, t.description, t.viewtype, t.color, t.isdone
                FROM taskevents t
                WHERE t.id IN (
                    SELECT event_id FROM cspace WHERE event_type = 'task'
                ) AND t.hashcode IN (
                    SELECT event_hashcode FROM cspace WHERE event_type = 'task'
                )
            ) t),
            'timeblocks', (SELECT json_agg(tb) FROM (
                SELECT tb.id, tb.hashcode, tb.start_datetime, tb.end_datetime, tb.name, tb.description, tb.viewtype, tb.color 
                FROM timeblockevents tb
                WHERE tb.id IN (
                    SELECT event_id FROM cspace WHERE event_type = 'timeblock'
                ) AND tb.hashcode IN (
                    SELECT event_hashcode FROM cspace WHERE event_type = 'timeblock'
                )
            ) tb)
            ) AS calendar_data;
        """);
        
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();

        if (!rs.next()) {
            authResult = new AuthResult("", HTTPStatusCode.HTTP_404_NOT_FOUND);
            return ""; // Calendarspace or user not found.
        } else {
            authResult = new AuthResult(id, HTTPStatusCode.HTTP_200_OK);
            return rs.getString("calendar_data");
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
