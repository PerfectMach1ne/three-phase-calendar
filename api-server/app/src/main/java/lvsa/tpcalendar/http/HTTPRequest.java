package lvsa.tpcalendar.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.Filter;

import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.http.APIContexts.HTTPContext;

public class HTTPRequest implements BaseRequest {
	private final HttpExchange HTEX;
	private final String HTTP_METHOD;
	private final Map<String, String> QUERY_PARAMS;
	private HTTPStatusCode status = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;

    public static final String REGISTERED_NURSE = "\r\n";

	@SuppressWarnings("unchecked")
	HTTPRequest(HttpExchange httpExchange) {
		this.HTEX = httpExchange;
		this.HTTP_METHOD = this.HTEX.getRequestMethod().toUpperCase();
		this.QUERY_PARAMS = (Map<String, String>) HTEX.getAttribute("queryParams");	
		System.out.println(HTEX.getAttribute("queryParams").toString());
		System.out.println(httpExchange.getRequestMethod());
	}

	public void endWithStatus(HTTPStatusCode status, String responseType, String responseMessage) throws IOException {	
		Headers resh = this.HTEX.getResponseHeaders();
		resh.set("Content-Type", "application/json");

		String res = null;
		JsonObject resJson = new JsonObject(); 
		resJson.add(responseType, new JsonPrimitive(responseMessage));
		res = resJson.getAsString() + REGISTERED_NURSE;

		this.HTEX.sendResponseHeaders(status.getint(), 0);
		OutputStream os = this.HTEX.getResponseBody();
		os.write(res.getBytes());
		os.flush();
	}
}
