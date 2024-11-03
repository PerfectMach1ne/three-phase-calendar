package lvsa.tpcalendar.http;

import com.sun.net.httpserver.HttpExchange;

/*
 * API route strategy
 */
public interface APIRoute {
    public String getResponse();

    public HTTPStatusCode GET(HttpExchange htex);

    public HTTPStatusCode POST(HttpExchange htex);
    
    public HTTPStatusCode PUT(HttpExchange htex);

    public HTTPStatusCode PATCH(HttpExchange htex);

    public HTTPStatusCode DELETE(HttpExchange htex);

    public HTTPStatusCode HEAD(HttpExchange htex);

    public HTTPStatusCode CONNECT(HttpExchange htex);

    public HTTPStatusCode OPTIONS(HttpExchange htex);

    public HTTPStatusCode TRACE(HttpExchange htex);
}
