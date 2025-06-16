package lvsa.tpcalendar.dbutils.proxies;

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

public class RegisterDBProxy extends BaseDBProxy implements AutoCloseable {

    public RegisterDBProxy(DBConnProvider dbConnProvider) {
        super(dbConnProvider);
    }

    public HTTPStatusCode create(String json) throws SQLException {
        PreparedStatement query = this.conn.prepareStatement("""
            SELECT email
            FROM users
            WHERE email = ?;
        """);

        Gson gson = new Gson();
        JsonElement loginJsonEl;

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

                ResultSet reg_rs = stat.executeQuery();
                if (reg_rs.next()) {
                    try(
                        DBConnProvider db = new DBConnProvider();
                        CalSpaceDBProxy cspace = new CalSpaceDBProxy(db);
                    ) {
                        Integer id = reg_rs.getInt("id");
                        HTTPStatusCode status = cspace.create(id.toString());
                        return status; // 201 == Account created.
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR; // Error while creating calendarspace.
                    }
                } else {
                    return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR; // INSERT query failed for some reason.
                }
            } else {
                return HTTPStatusCode.HTTP_409_CONFLICT; // Account already exists.
            }
        }

        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR; // Fallback error.
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
    
}
