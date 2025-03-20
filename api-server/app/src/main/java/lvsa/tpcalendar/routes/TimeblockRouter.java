package lvsa.tpcalendar.routes;

import java.sql.SQLException;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.TimeblockDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/cal/timeblock</code> endpoint</h3>
 */
public class TimeblockRouter implements APIRouter {
    private String response = "\"response\": \"nothing\"";

    @Override
    public String getResponse() { return this.response; }

    /**
     * <b>GET</b> <code>/api/cal/timeblock?id={hashcode}.</code>
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
     * <b>POST</b> <code>/api/cal/timeblock [-d application/json].</code>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PUT</b> <code>/api/cal/timeblock?id={hashcode} [-d application/json].</code>
     */
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PATCH</b> <code>/api/cal/timeblock?id={hashcode} [-d application/json].</code>
     */
    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>DELETE</b> <code>/api/cal/timeblock?id={hashcode}.</code>
     */
    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
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

    private Object[] findAndFetchFromDB(int hashcode){
        String jsonTask = "";

        try (
            DBConnProvider db = new DBConnProvider();
            TimeblockDBProxy proxy = new TimeblockDBProxy(db);
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
}
