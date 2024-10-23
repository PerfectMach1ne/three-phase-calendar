package lvsa.tpcalendar.routes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * ImATeapotDoubleColon3
 */
public class ImATeapotDoubleColon3 implements APIRoute {
    private String response = "NULL_RESPONSE";

    public String getResponse() {
        return this.response;
    }

    private void teapot(HttpExchange htex) throws IOException {
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

        String str;
        while ( (str = reader.readLine()) != null ) {
            sb.append(str);
            System.out.println(str);
        }

        response = "am a teapot :3" + APIContexts.REGISTERED_NURSE;
        // int status = HTTPStatusCode.HTTP_418_IM_A_TEAPOT.getint();
        // htex.sendResponseHeaders(status, res.length());

        // OutputStream os = htex.getResponseBody();
        // os.write(res.getBytes());
        // os.flush();
        // os.close(); is.close();
    }

    @Override
    public HTTPStatusCode GET(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode POST(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode PUT(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode DELETE(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode HEAD(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode CONNECT(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode OPTIONS(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }

    @Override
    public HTTPStatusCode TRACE(HttpExchange htex) {
        try { teapot(htex); } catch (IOException ioe) { ioe.printStackTrace(); }
        return HTTPStatusCode.HTTP_418_IM_A_TEAPOT;
    }
}