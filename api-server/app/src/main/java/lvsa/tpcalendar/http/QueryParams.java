package lvsa.tpcalendar.http;

import java.util.ArrayList;

public class QueryParams {
	private ArrayList<String> params;

	public QueryParams(String queryParamString) {
		if (queryParamString.isEmpty()) {
			params = null;
		}	
	}
}
