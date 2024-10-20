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
import com.sun.net.httpserver.Filter;

import lvsa.tpcalendar.http.HTTPStatusCode;

public class HTTPRequest implements BaseRequest {
	private final HttpExchange HTEX;
	private final String HTTP_METHOD;
	private final Map<String, String> QUERY_PARAMS;

	@SuppressWarnings("unchecked")
	HTTPRequest(HttpExchange httpExchange) {
		this.HTEX = httpExchange;
		this.HTTP_METHOD = this.HTEX.getRequestMethod().toUpperCase();
		this.QUERY_PARAMS = (Map<String, String>) HTEX.getAttribute("queryParams");	
		System.out.println(HTEX.getAttribute("queryParams").toString());
	}

	@Override
	public HTTPStatusCode switchMethod(Consumer<HTTPStatusCode> lambda) {
		return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
	}
}
