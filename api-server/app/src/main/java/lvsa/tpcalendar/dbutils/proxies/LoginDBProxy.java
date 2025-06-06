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
                        VALUES (?, ?, ?);
                    """);

                    stat.setString(1, map.get("name").getAsString());
                    stat.setString(2, map.get("email").getAsString());
                    stat.setString(3, map.get("password").getAsString());

                    stat.executeUpdate();

                    return HTTPStatusCode.HTTP_201_CREATED; // Account created.
                } else {
                    return HTTPStatusCode.HTTP_409_CONFLICT;        
                }
            } else {
                

                if (!rs.next()) {
                    return HTTPStatusCode.HTTP_404_NOT_FOUND; // Account does not exist.
                } else {
                    System.out.println(map.get("email").getAsString() + " == " + rs.getString("email"));
                    System.out.println(map.get("password").getAsString() + " == " + rs.getString("password"));
                    System.out.println(rs.getString("email").equals(map.get("email").getAsString()));
                    System.out.println(rs.getString("password").equals(map.get("password").getAsString()));
                    return rs.getString("email").equals(map.get("email").getAsString()) &&
                           rs.getString("password").equals(map.get("password").getAsString()) ?
                        HTTPStatusCode.HTTP_200_OK : // Log in successful.
                        HTTPStatusCode.HTTP_401_UNAUTHORIZED; // Incorrect credentials.
                }
            }
        }

        // Could this potentially be 500 instead...? I mean... what has to happen for us to get there?
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
