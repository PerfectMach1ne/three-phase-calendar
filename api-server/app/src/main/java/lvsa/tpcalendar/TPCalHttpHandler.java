package lvsa.tpcalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

class TPCalHttpHandler implements HttpHandler {

    public void handle(HttpExchange het) throws IOException {
        InputStream is = het.getRequestBody();

        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();

        String str;
        while ( (str = reader.readLine()) != null ) {
            sb.append(str);
            System.out.println(str);
        }

        String res = "am a teapot :3\n";

        het.sendResponseHeaders(418, res.length());

        OutputStream os = het.getResponseBody();
        os.write(res.getBytes());
        os.flush();
        os.close();
        is.close();
    }
}