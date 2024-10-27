package lvsa.tpcalendar.routes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * /teapot
 */
public class ImATeapotDoubleColon3 implements APIRoute {
    private String response = "INTERNAL_SERVER_ERROR";

    public String getResponse() {
        return this.response;
    }

    private HTTPStatusCode teapot(HttpExchange htex) {
        Headers reqHeaders = htex.getRequestHeaders();
        reqHeaders.forEach( (header, value) -> {
            System.out.println(header + ": ");
            value.forEach((listEl) -> {
                System.out.println(" " + listEl);
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
            return HTTPStatusCode.HTTP_500_INTERNAL_SERVER_ERROR;
        }

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