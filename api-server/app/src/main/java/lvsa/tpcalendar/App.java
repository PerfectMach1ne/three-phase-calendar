package lvsa.tpcalendar;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRoute;
import lvsa.tpcalendar.http.QueryParamsFilter;
import lvsa.tpcalendar.dbutils.SchemaInitializer;
import lvsa.tpcalendar.routes.ImATeapotDoubleColon3;
import lvsa.tpcalendar.routes.TaskRoute;

public class App {
    private static final int PORT = 8057;
    private static final InetSocketAddress ADDRESS = new InetSocketAddress(PORT);
    private static final int TCP_BACKLOG = 128; // Re: Note about socket backlogs (https://docs.oracle.com/en/java/javase/21/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html)

    private static HttpServer server = null;
    private static APIContexts apictxs = null;

    public static void main(String[] args) {
        try {
            runServer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static APIContexts createAPIContexts() {
        APIContexts apictxs = new APIContexts(
            new HashMap<String, Map.Entry<String, APIRoute>>(
                Map.of(
                    "/api/cal/task", new AbstractMap.SimpleEntry<String, APIRoute>(
                        "?id={int}", new TaskRoute()),
                    "/api/cal/timeblock", new AbstractMap.SimpleEntry<String, APIRoute>(
                        "?id={int}", new TaskRoute()),
                    "/api/cal/text", new AbstractMap.SimpleEntry<String, APIRoute>(
                        "", new TaskRoute()),
                    "/teapot", new AbstractMap.SimpleEntry<String, APIRoute>(
                        "?msg={char[57]}", new ImATeapotDoubleColon3())
                )
            )
        );
        return apictxs;
    }

    private static void runServer() throws IOException {
        try {
            server = HttpServer.create(ADDRESS, TCP_BACKLOG);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        System.out.println("[DEBUG] address == " + ADDRESS);

        /*SchemaInitializer schema = */new SchemaInitializer();

        apictxs = createAPIContexts();
        for (APIContexts.HTTPContext htc : apictxs.getContexts()) {
            if (!htc.getQueryParams().isBlank()) {
                server.createContext(htc.getURI(), htc.getHandler())
                    .getFilters().add(new QueryParamsFilter());    
            } else {
                server.createContext(htc.getURI(), htc.getHandler());
            }
        }

        server.setExecutor(null);
        server.start();
    }
}