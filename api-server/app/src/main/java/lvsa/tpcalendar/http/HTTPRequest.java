package lvsa.tpcalendar.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.dbutils.SchemaInitializer;

public class HTTPRequest {
	private final HttpExchange htex;

	HTTPRequest(HttpExchange httpExchange) {
		this.htex = httpExchange;
	}
}
