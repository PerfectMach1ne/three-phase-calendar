package lvsa.tpcalendar.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIContexts {
	private final HashMap<String, Map.Entry<String, APIRoute>> contextsParamsMap;

    public static final String REGISTERED_NURSE = "\r\n";

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
		private final String QUERY_PARAMS_VALIDATOR;
		private final APIRoute ROUTE_CLASS;
		private final HttpHandler HANDLER;

		HTTPContext(String uri, String params, APIRoute route) {
			this.URI_STRING = uri;
			this.QUERY_PARAMS_VALIDATOR = params;
			this.ROUTE_CLASS = route;
			this.HANDLER = new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					HTTPStatusCode status;
					// Default response based on unimplemented methods of APIRoute strategy.
					String res = "HTTP 405 : Method Not Allowed." + REGISTERED_NURSE; 					OutputStream os = exchange.getResponseBody();
					switch (exchange.getRequestMethod().toUpperCase()) {
						case "GET":
							status = ROUTE_CLASS.GET(exchange);
							break;
						case "POST":
							status = ROUTE_CLASS.POST(exchange);
							res = ROUTE_CLASS.getResponse();
							break;
						case "DELETE":
							status = ROUTE_CLASS.DELETE(exchange);
							break;
						case "PUT":
							status = ROUTE_CLASS.PUT(exchange);
							break;
						case "HEAD":
							status = ROUTE_CLASS.HEAD(exchange);
							break;
						case "CONNECT":
							status = ROUTE_CLASS.CONNECT(exchange);
							break;
						case "OPTIONS":
							status = ROUTE_CLASS.OPTIONS(exchange);
							break;
						case "TRACE":
							status = ROUTE_CLASS.TRACE(exchange);
							break;
						default:
							res = "HTTP 400 : Invalid HTTP method" + REGISTERED_NURSE;
							status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
					}
					if (status != HTTPStatusCode.HTTP_400_BAD_REQUEST ||
						status != HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED) {
						res = ROUTE_CLASS.getResponse();
					}
					System.out.println(res);
					// 0 to use Chunked Transfer Coding
					// https://www.rfc-editor.org/rfc/rfc9112.html#name-chunked-transfer-coding
					exchange.sendResponseHeaders(status.getint(), 0);
					os = exchange.getResponseBody();
					os.write(res.getBytes());
					os.flush();
					os.close();
				}
			};
		}

		public HttpHandler getHandler() { return this.HANDLER; }

		public String getURI() { return this.URI_STRING; }

		public String getQueryParamsValidator() { return this.QUERY_PARAMS_VALIDATOR; } 

		public APIRoute getAPIRoute() { return this.ROUTE_CLASS; };
	}
}
