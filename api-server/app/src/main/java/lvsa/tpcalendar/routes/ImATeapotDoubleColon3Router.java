package lvsa.tpcalendar.routes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * <h3><code>/teapot</code> endpoint</h3>
 * <h3><code>/api/teapot</code> endpoint</h3>
 */
public class ImATeapotDoubleColon3Router implements APIRouter {
    private String response = "{ \"response\": \"nothing\" }";
    private String token;

    public String getResponse() { return this.response; }
    public String getToken() { return this.token; }
    public void flushResponse() { this.response = null; }
    public void flushToken() { this.token = null; }

    private HTTPStatusCode teapot(HttpExchange htex) {

        Headers reqHeaders = htex.getRequestHeaders();
        reqHeaders.forEach( (header, value) -> {
            System.out.print("[DEBUG] " + header + ": ");
            value.forEach((listEl) -> {
                System.out.println(listEl);
            });
        });

        InputStream is = htex.getRequestBody();
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
		String reqdata;

        try {
            while ( (reqdata = reader.readLine()) != null ) {
                sb.append(reqdata);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            response = HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR.wrapAsJsonRes();
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

        Headers resh = htex.getResponseHeaders();
        resh.set("Content-Type", "text/plain");

        response = "am a teapot :3" + APIContexts.REGISTERED_NURSE;
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode PATCH(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        teapot(htex);
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }
}