package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

public class AuthRoute implements APIRoute {
    private String response = "INTERNAL_SERVER_ERROR";

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }
    
}
