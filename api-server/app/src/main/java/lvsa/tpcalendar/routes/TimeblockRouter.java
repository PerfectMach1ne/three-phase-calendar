package lvsa.tpcalendar.routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.auth.TokenProvider;
import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.dbutils.proxies.TimeblockDBProxy;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/cal/timeblock</code> endpoint</h3>
 */
public class TimeblockRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private String token;
    private final int PGERR_UNIQUE_VIOLATION = 23505;

    public String getResponse() { return this.response; }
    public String getToken() { return this.token; }
    public void flushResponse() { this.response = null; }
    public void flushToken() { this.token = null; }

    /**
     * <b>GET</b> <code>/api/cal/timeblock?id={hashcode}.</code>
     * <p>Uses the following method:</p> <pre>Object[] findAndFetchFromDB(int hashcode)</pre>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        // A (in)sane default.
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        int hashcode = -1;
        try {
            hashcode = Integer.parseInt(queryParams.get("id"));
        } catch (NumberFormatException | NullPointerException nXe) {
            status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
            response = status.wrapAsJsonRes();
            nXe.printStackTrace();
        }

        Object[] dbResult = findAndFetchFromDB(hashcode);
        status = (HTTPStatusCode)dbResult[0];
        Object fetchedJsonOrNull = dbResult[1];

        token = htex.getRequestHeaders().getFirst("Authorization");
        token = token.trim().replaceAll("(?i)bearer", "").trim(); // Sanitize received token.
        try( TokenProvider tp = new TokenProvider() ) {
            try {
                tp.verifyToken(token);
            } catch (JWTVerificationException jwtve) {
                jwtve.printStackTrace();
                status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            return status;
        }
        
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
     * <p>Uses the following method:</p> <pre>HTTPStatusCode insertTimeblockIntoDB(String buffer)</pre>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;
        HTTPStatusCode status; // Return value of insertTaskIntoDB()
        DecodedJWT jwt = null;

        token = htex.getRequestHeaders().getFirst("Authorization");
        token = token.trim().replaceAll("(?i)bearer", "").trim(); // Sanitize received token.
        try( TokenProvider tp = new TokenProvider() ) {
            try {
                jwt = tp.verifyToken(token);
            } catch (JWTVerificationException jwtve) {
                jwtve.printStackTrace();
                status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            return status;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch(IOException ioe) {
            // TODO: log: IOException at StringBuffer in POST /api/cal/timeblock
            ioe.printStackTrace();
            response = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

        status = insertTimeblockIntoDB(sb.toString(), Integer.parseInt(jwt.getSubject()));
        response = status.wrapAsJsonRes();
        return status;
    }

    /**
     * <b>PUT</b> <code>/api/cal/timeblock?id={hashcode} [-d application/json].</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        // A (in)sane default.
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        int hashcode = -1;
        try {
            hashcode = Integer.parseInt(queryParams.get("id"));
        } catch (NumberFormatException | NullPointerException nXe) {
            status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
            response = status.wrapAsJsonRes();
            nXe.printStackTrace();
        }

		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();

		String reqdata;

        token = htex.getRequestHeaders().getFirst("Authorization");
        token = token.trim().replaceAll("(?i)bearer", "").trim(); // Sanitize received token.
        try( TokenProvider tp = new TokenProvider() ) {
            try {
                tp.verifyToken(token);
            } catch (JWTVerificationException jwtve) {
                jwtve.printStackTrace();
                status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            return status;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            // TODO: log: IOException at StringBuffer in POST /api/cal/timeblock
            ioe.printStackTrace();
            // Default is HTTP 500 Internal Server Error anyway.
            response = status.wrapAsJsonRes();
            return status;
        }

        try (
            DBConnProvider db = new DBConnProvider();
            TimeblockDBProxy proxy = new TimeblockDBProxy(db)
        ) {
            status = proxy.updateWhole(hashcode, sb.toString());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        response = status.wrapAsJsonRes();
        return status;
    }

    /**
     * <b>DELETE</b> <code>/api/cal/timeblock?id={hashcode}.</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        // A (in)sane default.
        HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        int hashcode = -1;
        try {
            hashcode = Integer.parseInt(queryParams.get("id"));
        } catch (NumberFormatException | NullPointerException nXe) {
            status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
            response = status.wrapAsJsonRes();
            nXe.printStackTrace();
        }

        token = htex.getRequestHeaders().getFirst("Authorization");
        token = token.trim().replaceAll("(?i)bearer", "").trim(); // Sanitize received token.
        try( TokenProvider tp = new TokenProvider() ) {
            try {
                tp.verifyToken(token);
            } catch (JWTVerificationException jwtve) {
                jwtve.printStackTrace();
                status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = HTTPStatusCode.HTTP_401_UNAUTHORIZED;
            return status;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");
        
        try (
            DBConnProvider db = new DBConnProvider();
            TimeblockDBProxy proxy = new TimeblockDBProxy(db);
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
     * <b>PATCH</b> <code>/api/cal/timeblock?id={hashcode} [-d application/json].</code>
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
     * Find and fetch the timeblock from the database and return it alongside its HTTP status code.
     * 
     * @param   hashcode    an <code>int</code> hashcode of a given timeblock.
     * @return  a 2-element <code>Object[]</code> array containing HTTPStatusCode and either a Gson-compatible JSON object from db or a <code>null</code> value.
     */
    private Object[] findAndFetchFromDB(int hashcode){
        String jsonTimeblock = "";

        try (
            DBConnProvider db = new DBConnProvider();
            TimeblockDBProxy proxy = new TimeblockDBProxy(db);
        ) {
            jsonTimeblock = proxy.read(hashcode);
            if (jsonTimeblock.isEmpty()) {
                return new Object[]{HTTPStatusCode.HTTP_404_NOT_FOUND, null};
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new Object[]{HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR, null};
        }

        return new Object[]{HTTPStatusCode.HTTP_200_OK, jsonTimeblock};
    }

    /**
     * Attempt to insert a new timeblock into the database, and return the HTTP status code representing the result of the operation.
     * 
     * @param   buffer  a string buffer containing the request JSON data to be inserted into the database.
     * @return  a <code>HTTPStatusCode</code>.
     */
    private HTTPStatusCode insertTimeblockIntoDB(String buffer, int uid) {
        try(
            DBConnProvider db = new DBConnProvider();
            TimeblockDBProxy proxy = new TimeblockDBProxy(db);
        ) {
            proxy.setUid(uid);
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
