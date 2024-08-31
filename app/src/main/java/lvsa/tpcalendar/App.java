package lvsa.tpcalendar;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class App {
    // public String getGreeting() {
    //     return "heeeeELOOOOOOOOO WOOOORLLLLLLLDDD";
    // }

    public static void main(String[] args) {
        try {
            runServer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        // System.out.println(new App().getGreeting());
    }

    private static void runServer() throws IOException {
        int port = 8057;
        InetSocketAddress cd = new InetSocketAddress(port);
        HttpServer server = null;

        try {
            server = HttpServer.create(cd, port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        server.createContext("/teapot", new TPCalHttpHandler());

        server.setExecutor(null);
        server.start();
    }
}