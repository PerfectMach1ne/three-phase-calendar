package lvsa.tpcalendar.http;

import java.io.IOException;
import java.util.HashMap;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class QueryParamsFilter extends Filter {
	@Override
	public String description() {
		return "Parses and validates query parameters.";
	}

	@Override
	public void doFilter(HttpExchange htex, Chain chain) throws IOException {
		String query = htex.getRequestURI().getQuery();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		query = query.strip();

		if (query != null) {
			String[] splitQuery = query.split("&");
			for (String queryPair : splitQuery) {
				String[] splitPair = queryPair.split("=");
				String key = splitPair[0];
				String value = splitPair[1];
				queryParams.put(key, value);
			}
		}

		htex.setAttribute("queryParams", queryParams);
		chain.doFilter(htex);	
	}
}
