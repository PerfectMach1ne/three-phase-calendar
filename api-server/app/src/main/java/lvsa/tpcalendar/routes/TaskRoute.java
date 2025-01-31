package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/cal/task</code> endpoint</h3>
 */
public class TaskRoute implements APIRoute {
    private String response = "";
    private final int PGERR_UNIQUE_VIOLATION = 23505;

    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * <b>GET</b> <code>/api/cal/task?id={hashcode}.</code>
     * <p>Uses the following method:</p> <pre>Object[] findAndFetchFromDB(int hashcode)</pre>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        int hashcode = Integer.valueOf(queryParams.get("id"));
        Object[] dbResult = findAndFetchFromDB(hashcode);

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        if (dbResult[0] == HTTPStatusCode.HTTP_404_NOT_FOUND && dbResult[1] == null) {
            response = HTTPStatusCode.HTTP_404_NOT_FOUND.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_404_NOT_FOUND;
        } else if (dbResult[0] != HTTPStatusCode.HTTP_200_OK) {
            HTTPStatusCode status = (HTTPStatusCode)dbResult[0];
            response = status.wrapAsJsonRes();
            return status;
        }

        String fetchedJson = (String)dbResult[1];
        response = fetchedJson.toString();

        return HTTPStatusCode.HTTP_200_OK;
    }

    /**
     * <b>POST</b> <code>/api/cal/task [-d application/json]</code>
     * <p>Uses the following method:</p> <pre>HTTPStatusCode insertTask(String buffer)</pre>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;
        HTTPStatusCode status;

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            // log: IOException at StringBuffer in POST /api/cal/task
            ioe.printStackTrace();
            response = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

        status = insertTaskIntoDB(sb.toString());
        response = status.wrapAsJsonRes(); 

        return status;
    }

    /**
     * <b>DELETE</b> <code>/api/cal/task?id={hashcode}</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        int hashcode = Integer.valueOf(queryParams.get("id"));

        // A (in)sane default.
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;

        try (
            DBConnProvider db = new DBConnProvider();
            Connection conn = db.getDBConnection();
        ) {
            status = db.deleteTask(hashcode);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            response = status.wrapAsJsonRes();
            return status;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        response = status.wrapAsJsonRes();

        return status;
    }

    /**
     * <b>PUT</b> <code>/api/cal/task?id={hashcode} [-d application/json]</code>
     */
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PATCH</b> <code>/api/cal/task?id={hashcode} [-d application/json]</code>
     */
    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    /**
     * Find and fetch the task from the database; Gson JSON object returned is null if task can't be found or something else goes wrong.
     * 
     * @param   hashcode    an <code>int</code> hashcode of a given task.
     * @return  a 2-element <code>Object[]</code> array containing the HTTP status code and the Gson-compatible JSON object from db.
     */
    private Object[] findAndFetchFromDB(int hashcode) {
        String jsonTask = "";

        try (
            DBConnProvider db = new DBConnProvider();
            Connection conn = db.getDBConnection();
        ) {
            jsonTask = db.queryByHashcode(hashcode);
            if (jsonTask.isEmpty()) {
                return new Object[]{HTTPStatusCode.HTTP_404_NOT_FOUND, null};
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return new Object[]{HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR, null};
        }

        return new Object[]{HTTPStatusCode.HTTP_200_OK, jsonTask};
    }

    /**
     * Attempt to insert a new task into the database, and return the HTTP status code representing the result of the operation.
     * 
     * @param   buffer  a string buffer containing the request JSON data to be inserted into the database.
     * @return  a <code>HTTPStatusCode</code> array containing the HTTP status code.
     */
    private HTTPStatusCode insertTaskIntoDB(String buffer) {
        try (
            DBConnProvider db = new DBConnProvider();
            Connection conn = db.getDBConnection();
        ) {
            HTTPStatusCode status = db.insertTask(buffer);
            return status;
        } catch (SQLException sqle) {
            if (Integer.parseInt(sqle.getSQLState()) == PGERR_UNIQUE_VIOLATION) {
                return HTTPStatusCode.HTTP_409_CONFLICT;
            }
            sqle.printStackTrace();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }
    }
}
