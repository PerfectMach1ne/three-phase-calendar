package lvsa.tpcalendar.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIContexts {
	private final HashMap<String, Map.Entry<String, APIRouter>> contextsParamsMap;

    public static final String REGISTERED_NURSE = "\r\n";

	public APIContexts(HashMap<String, Map.Entry<String, APIRouter>> contextParamsMap) {
		this.contextsParamsMap = contextParamsMap;
	}

	public ArrayList<HTTPContext> getContexts() {
		ArrayList<HTTPContext> list = new ArrayList<HTTPContext>();

		contextsParamsMap.forEach( (context, entryPair) -> {
			String queryParam = entryPair.getKey();
			APIRouter apiRoute = entryPair.getValue();
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
		private final APIRouter ROUTER;
		private final HttpHandler HANDLER;

		HTTPContext(String uri, String params, APIRouter route) {
			this.URI_STRING = uri;
			this.QUERY_PARAMS_VALIDATOR = params;
			this.ROUTER = route;
			this.HANDLER = new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					// Default response based on unimplemented methods of APIRouter strategy.
					HTTPStatusCode status = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
					String res = status.wrapAsJsonRes() + REGISTERED_NURSE;

					OutputStream os = exchange.getResponseBody();

					switch (exchange.getRequestMethod().toUpperCase()) {
						case "GET":
							status = ROUTER.GET(exchange);
							break;
						case "POST":
							status = ROUTER.POST(exchange);
							break;
						case "PATCH":
							status = ROUTER.PATCH(exchange);
							break;
						case "PUT":
							status = ROUTER.PUT(exchange);
							break;
						case "DELETE":
							status = ROUTER.DELETE(exchange);
							break;
						case "HEAD":
							status = ROUTER.HEAD(exchange);
							break;
						case "CONNECT":
							status = ROUTER.CONNECT(exchange);
							break;
						case "OPTIONS":
							status = ROUTER.OPTIONS(exchange);
							break;
						case "TRACE":
							status = ROUTER.TRACE(exchange);
							break;
						default:
							status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
							res = status.wrapAsJsonRes() + REGISTERED_NURSE;
					}
					
					// Wacky ternary to avoid \r\n duplication.
					res = ROUTER.getResponse() 
						+ (ROUTER.getResponse().endsWith(REGISTERED_NURSE) ? "" : REGISTERED_NURSE);

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

		public APIRouter getAPIRoute() { return this.ROUTER; };
	}
}
