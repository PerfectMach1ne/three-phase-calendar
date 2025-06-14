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

        try {
            loginJsonEl = gson.fromJson(json, JsonElement.class);
            if (loginJsonEl.isJsonNull()) {
                return HTTPStatusCode.HTTP_400_BAD_REQUEST;
            }
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return HTTPStatusCode.HTTP_400_BAD_REQUEST;
        }
        
        if (loginJsonEl.isJsonObject()) {
            JsonObject jobj = loginJsonEl.getAsJsonObject();
            Map<String, JsonElement> map = jobj.asMap();

            query.setString(1, map.get("email").getAsString());
            
            ResultSet rs = query.executeQuery();
            if (map.get("name") != null) {
                if (!rs.next()) {
                    PreparedStatement stat = this.conn.prepareStatement("""
                        INSERT INTO users (name, email, password)
                        VALUES (?, ?, ?)
                        RETURNING id;
                    """);

                    stat.setString(1, map.get("name").getAsString());
                    stat.setString(2, map.get("email").getAsString());
                    stat.setString(3, map.get("password").getAsString());

                    ResultSet stat_rs = stat.executeQuery();
                    if (stat_rs.next()) {
                        try(
                            DBConnProvider db = new DBConnProvider();
                            CalendarSpaceDBProxy cspace = new CalendarSpaceDBProxy(db);
                        ) {
                            Integer id = stat_rs.getInt("id");
                            HTTPStatusCode status = cspace.create(id.toString());
                            return status; // 201 == Account created.
                        } catch (SQLException sqle) {
                            sqle.printStackTrace();
                            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
                        }
                        // TODO: for each user account creation, a corresponding calendarspace also ough to be created.
                        // CSpaceDBProxy cspace.create(user_id)
                    } else {
                        System.out.println("weird ass error");
                        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
                    }
                    
                    
                } else {
                    return HTTPStatusCode.HTTP_409_CONFLICT;        
                }
            } else {
                if (!rs.next()) {
                    return HTTPStatusCode.HTTP_404_NOT_FOUND; // Account does not exist.
                } else {
                    return rs.getString("email").equals(map.get("email").getAsString()) &&
                           rs.getString("password").equals(map.get("password").getAsString()) ?
                        HTTPStatusCode.HTTP_200_OK : // Log in successful.
                        HTTPStatusCode.HTTP_401_UNAUTHORIZED; // Incorrect credentials.
                }
            }
        }

        return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
