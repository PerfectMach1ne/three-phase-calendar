package lvsa.tpcalendar.routes;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.io.BufferedReader;

import lvsa.tpcalendar.auth.TokenProvider;
import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.LoginDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * Little JSON schema for POST /api/login request response formatting.
 */
@SuppressWarnings("unused")
class LoginResponse{
    private int loginUserId;
    private String result;

    LoginResponse() {}

    LoginResponse(int uid, String res) {
        this.loginUserId = uid;
        this.result = res;
    }
}
/**
 * <h3><code>/api/login</code> endpoint</h3>
 */
public class LoginRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private String token;
    private final int PGERR_UNIQUE_VIOLATION = 23505;
    private int loginUserId;

    public String getResponse() { return this.response; }
    public String getToken() { return this.token; }
    public void flushResponse() { this.response = null; }
    public void flushToken() { this.token = null; }

    /** 
     * <b>POST</b> <code>/api/login [-d application/json]</code>.
     * <p>Authenticate a user with provided email and password.</p>
     * <p>Uses the following method:</p> <pre>HTTPStatusCode attemptLogin(String buffer)</pre>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;
        Gson gson = new GsonBuilder().disableInnerClassSerialization().create();
        // A (in)sane default.
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            // TODO: log: IOException at StringBuffer in POST /api/login
            ioe.printStackTrace();
            response = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

        status = attemptLogin(sb.toString());

        String resStatus = status.wrapAsJsonRes();
        JsonObject resJobj = gson.fromJson(resStatus, JsonObject.class);
        String resExtract = resJobj.get("result").getAsString();
        
        LoginResponse res = new LoginResponse(this.loginUserId, resExtract);
        response = gson.toJson(res);
        System.out.println(gson.toJson(res));

        return status;
    }

    /** 
     * <b>GET</b> <code>/api/login?id={user_id}</code>.
     * <p>Fetch the calendarspace and every event belonging to a particular user (by id).</p>
     * <p>Uses the following method:</p> <pre>Object[] loadEvents(int id)</pre>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        HTTPStatusCode status;
        int uid = -1;
        try {
            uid = Integer.parseInt(queryParams.get("id"));
        } catch (NumberFormatException | NullPointerException nXe) {
            status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
            response = status.wrapAsJsonRes();
            nXe.printStackTrace();
        }
        
        token = htex.getRequestHeaders().getFirst("Authorization");
        token = token.trim().replaceAll("(?i)bearer", "").trim(); // Sanitize received token.
        try( TokenProvider tp = new TokenProvider() ) {
            try {
                DecodedJWT jwt = tp.verifyToken(token);

                String subject = jwt.getSubject();
                int tokenuid = Integer.valueOf(subject).intValue();

                if (tokenuid != uid) {
                    System.out.println(tokenuid);
                    System.out.println(uid);
                    status = HTTPStatusCode.HTTP_403_FORBIDDEN;
                    return status;
                }
            } catch (JWTVerificationException jwtve) {
                jwtve.printStackTrace();
                status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            return status;
        }
        
        Object[] dbResult = loadEvents(uid);
        status = (HTTPStatusCode)dbResult[0];
        Object fetchedJsonOrNull = dbResult[1];

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        // Potentially offload the status code choice to CSpaceDBProxy?
        if (status == HTTPStatusCode.HTTP_404_NOT_FOUND && fetchedJsonOrNull == null) {
            response = HTTPStatusCode.HTTP_404_NOT_FOUND.wrapAsJsonRes();
            return status;
        } else if (dbResult[0] != HTTPStatusCode.HTTP_200_OK) {
            response = status.wrapAsJsonRes();
            return status;
        } else {
            response = fetchedJsonOrNull.toString();
            return status;
        }
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    /**
     * Attempt to log the user in with their email and password, create a session JWT token and return the resulting HTTP status code.
     * 
     * @param   buffer  JSON string containing username, email and hashed password.
     * @return  a <code>HTTPStatusCode</code>.
     */
    private HTTPStatusCode attemptLogin(String buffer) {
        try(
            DBConnProvider db = new DBConnProvider();
            LoginDBProxy proxy = new LoginDBProxy(db);
        ) {
            HTTPStatusCode status = proxy.create(buffer);
            if (proxy.getAuthResult().isSuccess()) {
                try {
                    TokenProvider tp = new TokenProvider();
                    token = tp.createJWTToken(proxy.getAuthResult().getUser_id());
                    loginUserId = proxy.getAuthResult().getUser_id();
                    tp.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return status; // Guaranteed 200 if successful.
            } else {
                return status; // Complement of above set of results.
            }
        } catch (SQLException sqle) {
            if (Integer.parseInt(sqle.getSQLState()) == PGERR_UNIQUE_VIOLATION) {
                return HTTPStatusCode.HTTP_409_CONFLICT;
            }
            sqle.printStackTrace();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Fetch a JSON string containing the calendarspace and all corresponding events of a user with a particular <code>id</code>.
     * 
     * @param   id  User id in the database as <code>int</code>.
     * @return      a 2-element <code>Object[]</code> array containing HTTPStatusCode and either a Gson-compatible JSON object from db or a <code>null</code> value.
     */
    private Object[] loadEvents(int id) {
        String eventList = "";

        try (
            DBConnProvider db = new DBConnProvider();
            LoginDBProxy proxy = new LoginDBProxy(db);
        ) {
            eventList = proxy.read(id);
            if (eventList.isEmpty() || eventList == null) {
                return new Object[]{HTTPStatusCode.HTTP_204_NO_CONTENT, null};
            } else if (proxy.getAuthResult().isSuccess()) {
                return new Object[]{HTTPStatusCode.HTTP_200_OK, eventList};
            } else {
                return new Object[]{HTTPStatusCode.HTTP_204_NO_CONTENT, null};
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new Object[]{HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR, null};
        }
    }
}
