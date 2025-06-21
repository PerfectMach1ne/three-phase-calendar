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

public class RegisterDBProxy extends BaseDBProxy implements AutoCloseable {
    private AuthResult authResult;

    public RegisterDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }

    public AuthResult getAuthResult() {
        return authResult;
    }

    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT id, email
            FROM users
            WHERE email = ?;
        """);

        Gson gson = new Gson();
        JsonElement loginJsonEl;
        HTTPStatusCode status;

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

            ResultSet rs = query.executeQuery();
            if (!rs.next()) {
                PreparedStatement stat = this.conn.prepareStatement("""
                    INSERT INTO users (name, email, password)
                    VALUES (?, ?, ?)
                    RETURNING id;
                """);

                stat.setString(1, map.get("name").getAsString());
                stat.setString(2, map.get("email").getAsString());
                stat.setString(3, map.get("password").getAsString());

                try {
                    ResultSet reg_rs = stat.executeQuery();

                    if (!rs.next()) {
                        status = HTTPStatusCode.HTTP_201_CREATED;
                        Integer id = reg_rs.getInt("id");
                        authResult = new AuthResult(id, status);

                        return status;
                    }
                } catch (SQLException sqle) {
                    sqle.printStackTrace();

                    status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
                    authResult = new AuthResult("Insert query failure.", status);
                    return status; // INSERT query failed for some reason.
                }
            } else {
                status = HTTPStatusCode.HTTP_409_CONFLICT;
                authResult = new AuthResult("Account already exists.", status);
                return status; // Account already exists.
            }
        }

        status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        authResult = new AuthResult("Unidentified fallback server error triggered.", status);
        return status; // Fallback error.
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
