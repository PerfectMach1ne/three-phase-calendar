package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * /api/cal/task
 */
public class TaskRoute implements APIRoute {
    private String response = "INTERNAL_SERVER_ERROR";

    /**
     * Find and fetch the task from the database; Gson JSON object returned is null if task can't be found.
     * @param hashcode
     * @return a 2-element <code>Object[]</code> array containing the HTTP status code and the Gson-compatible JSON object from db.
     */
    public static Object[] findAndFetchFromDB(int hashcode) {
        JsonObject jsonTask = null;

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
        }

        return new Object[]{HTTPStatusCode.HTTP_200_OK, jsonTask};
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * GET /api/cal/task?id={hashcode}
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        Gson gson = new Gson();
        HTTPStatusCode status;
        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        
        String jsonObj;
        int hashcode = Integer.valueOf(queryParams.get("id"));

        Object[] dbResult = findAndFetchFromDB(hashcode);
        if (dbResult[0] == HTTPStatusCode.HTTP_404_NOT_FOUND && dbResult[1] == null) {
            try {
                Map<String, String> errMap = new LinkedHashMap<>();
                errMap.put("error", "404 Not Found");
                jsonObj = gson.toJson(errMap);
            } catch (JsonParseException jpe) {
                jpe.printStackTrace();
                return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
            }
            try {
                // GSON what the fuck. WHy does this throw UOE???
                // response = jsonObj.getAsString() + APIContexts.REGISTERED_NURSE;
                response = jsonObj + APIContexts.REGISTERED_NURSE;
            } catch (UnsupportedOperationException uoe) {
                uoe.printStackTrace();
                return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
            }
            return HTTPStatusCode.HTTP_404_NOT_FOUND;
        }
        JsonObject fetchedJson = (JsonObject)dbResult[1];

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        response = fetchedJson.toString() + APIContexts.REGISTERED_NURSE;
        status = HTTPStatusCode.HTTP_200_OK;
        return status;
    }

    /**
     * POST /api/cal/task [-d application/json]
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
        JsonObject jsonObj = null;
        jsonObj = JsonParser.parseString(sb.toString()).getAsJsonObject();

        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    /**
     * PUT /api/cal/task?id={hashcode} [-d application/json]
     */
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    /**
     * PATCH /api/cal/task?id={hashcode} [-d application/json]
     */
    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * DELETE /api/cal/task?id={hashcode}
     */
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
}
