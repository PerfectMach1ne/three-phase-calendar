package lvsa.tpcalendar.routes;

import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/api/cal/text</code> endpoint</h3>
 */
public class TextRouter implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private String token;

    public String getResponse() { return this.response; }
    public String getToken() { return this.token; }
    public void flushResponse() { this.response = null; }
    public void flushToken() { this.token = null; }

    /**
     * <b>GET</b> <code>/api/cal/text?id={base64encodedstr}.</code>
     */
    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>POST</b> <code>/api/cal/text [-d application/xml].</code>
     */
    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PUT</b> <code>/api/cal/text?id={base64encodedstr} [-d application/xml].</code>
     */
    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>PATCH</b> <code>/api/cal/text?id={base64encodedstr} [-d application/xml].</code>
     */
    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    /**
     * <b>DELETE</b> <code>/api/cal/text?id={base64encodedstr}.</code>
     */
    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        response = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED.wrapAsJsonRes();
        return HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
    }
}
