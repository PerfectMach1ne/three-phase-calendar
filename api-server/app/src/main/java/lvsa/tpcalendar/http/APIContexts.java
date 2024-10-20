package lvsa.tpcalendar.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class APIContexts {
	private final HashMap<String, String> contextsParamsMap;

	public APIContexts(HashMap<String, String> contextParamsMap) {
		this.contextsParamsMap = contextParamsMap;
	}

	public ArrayList<HTTPContext> getContexts() {
		ArrayList<HTTPContext> list = new ArrayList<HTTPContext>();

		contextsParamsMap.forEach( (context, queryParam) -> {
			if (queryParam.isEmpty()) {
				list.add(new HTTPContext(context, ""));
			} else {
				list.add(new HTTPContext(context, queryParam));
			}
		} );

		return list;
	}

	public class HTTPContext {
		private final String URI_STRING;
		private final String QUERY_PARAMS;
		private HttpHandler HANDLER = null;

		HTTPContext(String uri, String params) {
			this.URI_STRING = uri;
			this.QUERY_PARAMS = params;
		}

		public HttpHandler getHandler() {
			this.HANDLER = new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					HTTPRequest req = new HTTPRequest(exchange);
					switch (exchange.getRequestMethod().toUpperCase()) {
						case "GET":
						case "POST":
						case "DELETE":
						case "PUT":
						case "HEAD":
						case "CONNECT":
						case "OPTIONS":
						case "TRACE":
						default:
							req.endWithStatus(HTTPStatusCode.HTTP_400_BAD_REQUEST, "error", "Invalid HTTP method!");
					}
				}
			};
			return this.HANDLER;
		}

		public String getURI() { return this.URI_STRING; }

		public String getQueryParams() { return this.QUERY_PARAMS; } 
	}
}
