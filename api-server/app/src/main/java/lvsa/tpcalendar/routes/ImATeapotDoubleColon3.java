package lvsa.tpcalendar.routes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * ImATeapotDoubleColon3
 */
public class ImATeapotDoubleColon3 implements APIRoute {

    private void teapot(HttpExchange htex) throws IOException {
        InputStream is = htex.getRequestBody();

        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();

        String str;
        while ( (str = reader.readLine()) != null ) {
            sb.append(str);
            System.out.println(str);
        }

        System.out.println("418 /teapot requested");
        String res = "am a teapot :3\n";

        htex.sendResponseHeaders(418, res.length());

        OutputStream os = htex.getResponseBody();
        os.write(res.getBytes());
        os.flush();
        os.close(); is.close();
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