package lvsa.tpcalendar;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class App {
    // public String getGreeting() {
    //     return "heeeeELOOOOOOOOO WOOOORLLLLLLLDDD";
    // }

    public static void main(String[] args) throws IOException {
        runServer();
        // System.out.println(new App().getGreeting());
    }

    private static void runServer() {
        InetSocketAddress cd = new InetSocketAddress(8057);
        HttpServer server = null;

        try {
            server = HttpServer.create(cd, 8057);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.createContext("/teapot", new TPCalHttpHandler());

        server.setExecutor(null);
        server.start();
    }
}