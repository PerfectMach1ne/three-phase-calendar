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
				list.add(new HTTPContext(context, null));
			} else {
				list.add(new HTTPContext(context, new QueryParams(queryParam)));
			}
		} );

		return list;
	}

	public class HTTPContext {
		private final String URI_STRING;
		private final QueryParams QUERY_PARAMS;

		HTTPContext(String uri, QueryParams params) {
			this.URI_STRING = uri;
			this.QUERY_PARAMS = params;
		}

		public HttpHandler getHandler() {
			return new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					new HTTPRequest(exchange);
				}
			};
		}

		public String getURI() { return this.URI_STRING; }
	}
}
