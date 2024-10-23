package lvsa.tpcalendar.routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.model.TaskEvent;

/**
 * TaskRoute
 */
public class TaskRoute implements APIRoute {
    private String response = "INTERNAL_SERVER_ERROR";

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        HTTPStatusCode status;

        /******** this should go into POST; use query params \/\/\/ */
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
        /******** this should go into POST; use query params /\/\/\ */

        Object[] dbResult = TaskEvent.findAndFetchFromDB(jsonObj.get("hashcode").getAsInt());
        if (dbResult[0] == HTTPStatusCode.HTTP_404_NOT_FOUND && dbResult[1] == null) {
            try {
                jsonObj = JsonParser.parseString("{\"error\": \"404 Not Found\"}").getAsJsonObject();
            } catch (JsonParseException jpe) {
                jpe.printStackTrace();
                return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
            }
            try {
                // GSON what the fuck. WHy does this throw UOE???
                // response = jsonObj.getAsString() + APIContexts.REGISTERED_NURSE;
                response = jsonObj.toString() + APIContexts.REGISTERED_NURSE;
            } catch (UnsupportedOperationException uoe) {
                uoe.printStackTrace();
                return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
            }
            return HTTPStatusCode.HTTP_404_NOT_FOUND;
        }
        JsonObject fetchedJson = (JsonObject)dbResult[1];
        /* possibly to deprecate \/\/\/ */
        TaskEvent fetchedTask = new TaskEvent(fetchedJson);
        System.out.println("Hashcode: " + fetchedTask.hashCode() + '\n'
            + "Taskname: " + fetchedTask.getName() + "\n"
            + "Description: " + fetchedTask.getDescription() + "\n"
            + "Date: " + fetchedTask.getDateTime().toLocalDate().toString() + "\n"
            + "Hour: " + fetchedTask.getDateTime().toLocalTime().toString() + "\n"
            + "Color: " + fetchedTask.getColor());
        /* possibly to deprecate /\/\/\ */

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "application/json");

        response = fetchedJson.toString() + APIContexts.REGISTERED_NURSE;
        status = HTTPStatusCode.HTTP_200_OK;
        return status;
    }

    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
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
}
