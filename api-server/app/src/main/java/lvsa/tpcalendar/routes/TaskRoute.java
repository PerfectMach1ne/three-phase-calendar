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

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * /api/cal/task
 */
public class TaskRoute implements APIRoute {
    private String response = "";

    public TaskRoute() {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "500 Internal Server Error");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
    }

    /**
     * Find and fetch the task from the database; Gson JSON object returned is null if task can't be found.
     * @param hashcode
     * @return a 2-element <code>Object[]</code> array containing the HTTP status code and the Gson-compatible JSON object from db.
     */
    public static Object[] findAndFetchFromDB(int hashcode) {
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
        }

        return new Object[]{HTTPStatusCode.HTTP_200_OK, jsonTask};
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * <b>GET</b> <code>/api/cal/task?id={hashcode}.</code>
     * <p>Uses the following method:</p> <pre>static Object[] findAndFetchFromDB(int hashcode)</pre>
     */
    @SuppressWarnings("unchecked")
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        Gson gson = new Gson();

        Map<String, String> queryParams = (Map<String, String>)htex.getAttribute("queryParams");
        int hashcode = Integer.valueOf(queryParams.get("id"));
        Object[] dbResult = findAndFetchFromDB(hashcode);

        if (dbResult[0] == HTTPStatusCode.HTTP_404_NOT_FOUND && dbResult[1] == null) {
            Map<String, String> errMap = new LinkedHashMap<>();
            errMap.put("error", "404 Not Found");
            response = gson.toJson(errMap);

            return HTTPStatusCode.HTTP_404_NOT_FOUND;
        }
        String fetchedJson = (String)dbResult[1];

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        response = fetchedJson.toString();
        return HTTPStatusCode.HTTP_200_OK;
    }

    /**
     * <b>POST</b> <code>/api/cal/task [-d application/json]</code>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        Gson gson = new Gson();

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
            // log: IOException at StringBuffer in POST /api/cal/task
            ioe.printStackTrace();
            
            Map<String, String> errMap = new LinkedHashMap<>();
            errMap.put("error", "500 Internal Server Error");
            response = gson.toJson(errMap);

            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        Map<String, String> resMap = new LinkedHashMap<>();
        resMap.put("result", "201 Created");
        // Map<String, String> json = gson.fromJson(sb.toString(), Map.class);
        response = gson.toJson(resMap);

        return HTTPStatusCode.HTTP_201_CREATED;
    }

    /**
     * <b>PUT</b> <code>/api/cal/task?id={hashcode} [-d application/json]</code>
     */
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "501 Method Not Implemented");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PATCH</b> <code>/api/cal/task?id={hashcode} [-d application/json]</code>
     */
    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "501 Method Not Implemented");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>DELETE</b> <code>/api/cal/task?id={hashcode}</code>
     */
    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "501 Method Not Implemented");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "405 Method Not Allowed");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "405 Method Not Allowed");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "405 Method Not Allowed");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        Gson gson = new Gson();
        Map<String, String> errMap = new LinkedHashMap<>();
        errMap.put("error", "405 Method Not Allowed");
        response = gson.toJson(errMap) + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }
}
