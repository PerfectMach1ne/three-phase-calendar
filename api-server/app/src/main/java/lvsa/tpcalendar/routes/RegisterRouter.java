package lvsa.tpcalendar.routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.auth.TokenProvider;
import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.RegisterDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * Little JSON schema for POST /api/register request response formatting.
 */
@SuppressWarnings("unused")
class RegisterResponse{
    private int loginUserId;
    private String result;

    RegisterResponse() {}

    RegisterResponse(int uid, String res) {
        this.loginUserId = uid;
        this.result = res;
    }
}
/**
 * <h3><code>/api/register</code> endpoint</h3>
 */
public class RegisterRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private String token;
    private final int PGERR_UNIQUE_VIOLATION = 23505;
    private int loginUserId;

    public String getResponse() { return this.response; }
    public String getToken() { return this.token; }
    public void flushResponse() { this.response = null; }
    public void flushToken() { this.token = null; }

    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    /** 
     * <b>POST</b> <code>/api/register [-d application/json]</code>.
     * <p>Create a user with email and password.</p>
     * <p>Uses the following method:</p> <pre>HTTPStatusCode attemptRegister(String buffer)</pre>
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
            // TODO: log: IOException at StringBuffer in POST /api/register
            ioe.printStackTrace();
            response = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }
        
        status = attemptRegister(sb.toString());

        String resStatus = status.wrapAsJsonRes();
        JsonObject resJobj = gson.fromJson(resStatus, JsonObject.class);
        String resExtract = resJobj.get("result").getAsString();
        System.out.println(resExtract);
        System.out.println(this.loginUserId);
        
        RegisterResponse res = new RegisterResponse(this.loginUserId, resExtract);
        System.out.println(gson.toJsonTree(res).toString());
        response = gson.toJson(res);
        System.out.println(gson.toJson(res));

        return status;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
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
     * Attempt to register the user with given data.
     * 
     * @param   buffer  a string buffer containing the request JSON data to be inserted into the database.
     * @return  a <code>HTTPStatusCode</code>.
     */
    private HTTPStatusCode attemptRegister(String buffer) {
        try(
            DBConnProvider db = new DBConnProvider();
            RegisterDBProxy proxy = new RegisterDBProxy(db);
        ) {
            HTTPStatusCode status = proxy.create(buffer);
            if (proxy.getAuthResult().isSuccess()) {
                try {
                    TokenProvider tp = new TokenProvider();
                    token = tp.createJWTToken(proxy.getAuthResult().getUser_id());
                    tp.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return status; // Guaranteed 201 if successful.
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
}
