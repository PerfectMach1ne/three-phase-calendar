package lvsa.tpcalendar.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIContexts {
	private final HashMap<String, Map.Entry<String, APIRoute>> contextsParamsMap;

	public APIContexts(HashMap<String, Map.Entry<String, APIRoute>> contextParamsMap) {
		this.contextsParamsMap = contextParamsMap;
	}

	public ArrayList<HTTPContext> getContexts() {
		ArrayList<HTTPContext> list = new ArrayList<HTTPContext>();

		contextsParamsMap.forEach( (context, entryPair) -> {
			String queryParam = entryPair.getKey();
			APIRoute apiRoute = entryPair.getValue();
			if (queryParam.isEmpty()) {
				list.add(new HTTPContext(context, "", apiRoute));
			} else {
				list.add(new HTTPContext(context, queryParam, apiRoute));
			}
		} );

		return list;
	}

	public class HTTPContext {
		private final String URI_STRING;
		private final String QUERY_PARAMS;
		private final APIRoute ROUTE_CLASS;
		private HttpHandler HANDLER = null;

		HTTPContext(String uri, String params, APIRoute route) {
			this.URI_STRING = uri;
			this.QUERY_PARAMS = params;
			this.ROUTE_CLASS = route;
		}

		public HttpHandler getHandler() {
			this.HANDLER = new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					switch (exchange.getRequestMethod().toUpperCase()) {
						case "GET":
							ROUTE_CLASS.GET(exchange);
							break;
						case "POST":
							ROUTE_CLASS.POST(exchange);
							break;
						case "DELETE":
							ROUTE_CLASS.DELETE(exchange);
							break;
						case "PUT":
							ROUTE_CLASS.PUT(exchange);
							break;
						case "HEAD":
							ROUTE_CLASS.HEAD(exchange);
							break;
						case "CONNECT":
							ROUTE_CLASS.CONNECT(exchange);
							break;
						case "OPTIONS":
							ROUTE_CLASS.OPTIONS(exchange);
							break;
						case "TRACE":
							ROUTE_CLASS.TRACE(exchange);
							break;
						default:
							// req.endWithStatus(HTTPStatusCode.HTTP_400_BAD_REQUEST, "error", "Invalid HTTP method!");
					}
				}
			};
			return this.HANDLER;
		}

		public String getURI() { return this.URI_STRING; }

		public String getQueryParams() { return this.QUERY_PARAMS; } 

		public APIRoute getAPIRoute() { return this.ROUTE_CLASS; };
	}
}
