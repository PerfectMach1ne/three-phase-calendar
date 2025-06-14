package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.TaskDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/cal/task</code> endpoint</h3>
 */
public class TaskRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private final int PGERR_UNIQUE_VIOLATION = 23505;

    @Override
    public String getResponse() { return this.response; }

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
        HTTPStatusCode status = (HTTPStatusCode)dbResult[0];
        Object fetchedJsonOrNull = dbResult[1];

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        if (status == HTTPStatusCode.HTTP_404_NOT_FOUND && fetchedJsonOrNull == null) {
            response = HTTPStatusCode.HTTP_404_NOT_FOUND.wrapAsJsonRes();
            return status;
        } else if (dbResult[0] != HTTPStatusCode.HTTP_200_OK) {
            response = status.wrapAsJsonRes();
            return status;
        }

        response = fetchedJsonOrNull.toString();
        return status;
    }

    /**
     * <b>POST</b> <code>/api/cal/task [-d application/json]</code>
     * <p>Uses the following method:</p> <pre>HTTPStatusCode insertTaskIntoDB(String buffer)</pre>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        System.out.println("whar");
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;
        HTTPStatusCode status; // Return value of insertTaskIntoDB()

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            // TODO: log: IOException at StringBuffer in POST /api/cal/task
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

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try (
            DBConnProvider db = new DBConnProvider();
            TaskDBProxy proxy = new TaskDBProxy(db);
        ) {
            status = proxy.delete(hashcode);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            response = status.wrapAsJsonRes();
            return status;
        }

        response = status.wrapAsJsonRes();
        return status;
    }

    /**
     * <b>PUT</b> <code>/api/cal/task?id={hashcode} [-d application/json]</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        int hashcode = Integer.valueOf(queryParams.get("id"));

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
            // TODO: log: IOException at StringBuffer in POST /api/cal/task
            ioe.printStackTrace();
            // Default is HTTP 500 Internal Server Error anyway.
            response = status.wrapAsJsonRes();
            return status;
        }

        try (
            DBConnProvider db = new DBConnProvider();
            TaskDBProxy proxy = new TaskDBProxy(db)
        ) {
            status = proxy.updateWhole(hashcode, sb.toString());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        response = status.wrapAsJsonRes();
        return status;
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
     * Find and fetch the task from the database and return it alongside its HTTP status code.
     * 
     * @param   hashcode    an <code>int</code> hashcode of a given task.
     * @return  a 2-element <code>Object[]</code> array containing HTTPStatusCode and either a Gson-compatible JSON object from db or a <code>null</code> value.
     */
    private Object[] findAndFetchFromDB(int hashcode) {
        String jsonTask = "";

        try (
            DBConnProvider db = new DBConnProvider();
            TaskDBProxy proxy = new TaskDBProxy(db);
        ) {
            jsonTask = proxy.read(hashcode);
            if (jsonTask.isEmpty()) {
                return new Object[]{HTTPStatusCode.HTTP_404_NOT_FOUND, null};
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new Object[]{HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR, null};
        }

        return new Object[]{HTTPStatusCode.HTTP_200_OK, jsonTask};
    }

    /**
     * Attempt to insert a new task into the database, and return the HTTP status code representing the result of the operation.
     * 
     * @param   buffer  a string buffer containing the request JSON data to be inserted into the database.
     * @return  a <code>HTTPStatusCode</code>.
     */
    private HTTPStatusCode insertTaskIntoDB(String buffer) {
        try (
            DBConnProvider db = new DBConnProvider();
            TaskDBProxy proxy = new TaskDBProxy(db);
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
}
