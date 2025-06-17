package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;
import java.io.BufferedReader;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.LoginDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/login</code> endpoint</h3>
 */
public class LoginRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private final int PGERR_UNIQUE_VIOLATION = 23505;

    @Override
    public String getResponse() { return this.response; }

    /** 
     * <b>POST</b> <code>/api/login</code>.
     * <p>Create a user with email and password.</p>
     * <p>Uses the following method:</p> <pre>HTTPStatusCode attemptLogin(String buffer)</pre>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;
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
        response = status.wrapAsJsonRes();
        System.out.println("[DEBUG] status == " + status + 
                           "\n\tresponse == " + response);
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
        int uid = Integer.valueOf(queryParams.get("id"));
        
        Object[] dbResult = loadEvents(uid);
        HTTPStatusCode status = (HTTPStatusCode)dbResult[0];
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
        }

        return status;
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
     * Attempt to log the user in with their email and password and return the resulting HTTP status code.
     * 
     * @param   buffer  a string buffer containing the request JSON data to be inserted into the database.
     * @return  a <code>HTTPStatusCode</code>.
     */
    private HTTPStatusCode attemptLogin(String buffer) {
        try(
            DBConnProvider db = new DBConnProvider();
            LoginDBProxy proxy = new LoginDBProxy(db);
        ) {
            HTTPStatusCode status = proxy.create(buffer);
            return status;
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
            if (eventList.isEmpty()) {
                return new Object[]{HTTPStatusCode.HTTP_404_NOT_FOUND, null};
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new Object[]{HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR, null};
        }

        return new Object[]{HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED, null};
    }
}
