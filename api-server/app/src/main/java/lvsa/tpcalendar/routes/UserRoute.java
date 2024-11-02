package lvsa.tpcalendar.routes;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpExchange;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * /api/auth
 */
public class UserRoute implements APIRoute {
    private String response = "INTERNAL_SERVER_ERROR";

    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * Fetch basic user data.
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        HTTPStatusCode status;
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        
        System.out.println(queryParams.get("user"));

        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /** 
     * Create a user with email and password.
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
		InputStream is = htex.getRequestBody();
		InputStreamReader isReader = new InputStreamReader(is);
    	BufferedReader reader = new BufferedReader(isReader);
	    StringBuffer sb = new StringBuffer();
		String reqdata;
        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }
    
}
