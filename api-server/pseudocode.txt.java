// APIContexts
public class APIContexts {
	private final HashMap<str,str> contextParamsMap;
	private final List<APIRoute> apiRoutes;

	public APIContexts(contextParamsMap, List<APIRoute> apiRoutes) {...}

	public ArrayList<HTTPContext> getContexts() {
		... new HTTPContext(context, params, apiRoutes.getTheRightRoute()) ...}

	public class HTTPContext {
		private final String URI_STRING;
		private final String QUERY_PARAMS; // validation
		private HttpHandler HANDLER = null;
		private APIRoute API_ROUTE;

		HTTPContext(String uri, String params, APIRoute apiRoute) {...}

		public HttpHandler getHandler() {


			return this.HANDLER;
		}

		public String getURI() {...}
		
		public String getQueryParams() {...}

		public void setAPIRoute(APIRoute route) {
			this.API_ROUTE = route;
		}

		public void prepareAPIRoute() {
			this.HANDLER = new HttpHandler() {
				@Override
				public void handle(HttpExchange htex) {
					switch (htex.getRequestMethod()) {
						case 'GET':
							API_ROUTE.GET(htex);
							break;
						case 'POST':
							API_ROUTE.POST(htex);
							break;
						case 'DELETE':
						case 'PUT':
						case 'HEAD':
						case 'CONNECT':
						case 'OPTIONS':
						case 'TRACE':
						default:
							// 400
					}
				}
			}
		}
	}
}

// APIRoute 
public interface APIRoute {
	public HTTPStatusCode GET(HttpExchange htex);
	public HTTPStatusCode POST(...);
	public HTTPStatusCode DELETE();
	public HTTPStatusCode PUT();
	public HTTPStatusCode HEAD();
	public HTTPStatusCode CONNECT();
	public HTTPStatusCode OPTIONS();
	public HTTPStatusCode TRACE();
}

/api/cal/task
// TaskRoute
public class TaskRoute implements APIRoute {
	@Override
	public HTTPStatusCode METHOD(HttpExchange htex) {
		... impl go there ...
	}
}
/api/cal/
