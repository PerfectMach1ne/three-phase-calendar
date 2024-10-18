package lvsa.tpcalendar.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class QueryParams extends Filter {
	@Override
	public String description() {
		return "";
	}

	@Override
	public void doFilter(HttpExchange htex, Chain chain) throws IOException {
		String query = htex.getRequestURI().getQuery();
		HashMap<String, String> queryParams = new HashMap<String, String>();

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
