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

		if (query == null || query.trim().isEmpty()) {
			chain.doFilter(htex);
			return;
		}

		HashMap<String, String> queryParams = new HashMap<String, String>();
		String[] splitQuery = query.split("&");
		for (String queryPair : splitQuery) {
			if (queryPair == null || !queryPair.contains("=")) continue;
			String[] splitPair = queryPair.split("=");
			if (splitPair.length >= 2 && splitPair[0] != null) {
				String key = splitPair[0];
				String value = splitPair[1] != null ? splitPair[1] : "";
				queryParams.put(key.strip(), value.strip());
			}
		}

		htex.setAttribute("queryParams", queryParams);
		chain.doFilter(htex);	
	}
}
