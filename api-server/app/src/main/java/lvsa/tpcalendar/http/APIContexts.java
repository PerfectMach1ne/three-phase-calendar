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
					// Default response based on unimplemented methods of APIRoute strategy.
					HTTPStatusCode status = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
					String res = status.wrapAsJsonRes() + REGISTERED_NURSE;

					OutputStream os = exchange.getResponseBody();

					switch (exchange.getRequestMethod().toUpperCase()) {
						case "GET":
							status = ROUTE_CLASS.GET(exchange);
							break;
						case "POST":
							status = ROUTE_CLASS.POST(exchange);
							break;
						case "PATCH":
							status = ROUTE_CLASS.PATCH(exchange);
							break;
						case "PUT":
							status = ROUTE_CLASS.PUT(exchange);
							break;
						case "DELETE":
							status = ROUTE_CLASS.DELETE(exchange);
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
							status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
							res = status.wrapAsJsonRes() + REGISTERED_NURSE;
					}
					
					if (status != HTTPStatusCode.HTTP_400_BAD_REQUEST) {
						// Wacky ternary to avoid \r\n duplication.
						res = ROUTE_CLASS.getResponse() 
							+ (ROUTE_CLASS.getResponse().endsWith(REGISTERED_NURSE) ? "" : REGISTERED_NURSE);
					}

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
