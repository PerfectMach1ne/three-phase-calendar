package lvsa.tpcalendar;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lvsa.tpcalendar.http.APIContexts;
import lvsa.tpcalendar.http.APIRouter;
import lvsa.tpcalendar.http.QueryParamsFilter;
import lvsa.tpcalendar.dbutils.SchemaInitializer;
import lvsa.tpcalendar.routes.ImATeapotDoubleColon3Router;
import lvsa.tpcalendar.routes.TaskRouter;
import lvsa.tpcalendar.routes.TimeblockRouter;
import lvsa.tpcalendar.utils.IPUtils;
import lvsa.tpcalendar.utils.PropsService;

public final class App {
    // Re: Note about socket backlogs 
    // (https://docs.oracle.com/en/java/javase/21/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html)
    private static final int TCP_BACKLOG = 128;
    private static final Properties ipProps = new PropsService().getIPProps();
    private static final String ADDR_STR = ipProps.getProperty("ip");
    private static final int PORT = Integer.valueOf(ipProps.getProperty("port"));
    private static InetSocketAddress ADDRESS;

    private static HttpServer server = null;
    private static APIContexts apictxs = null;

    public static void main(String[] args) {
        try {
            ADDRESS = new InetSocketAddress(
                InetAddress.getByAddress(
                    ADDR_STR,
                    IPUtils.getIPbytes(ADDR_STR)),
                PORT);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        }

        try {
            runServer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Create the API contexts via <code>APIContexts</code>, <code>HTMLContext</code> wrappers and <code>APIRoute</code> interface
     * and connect the query parameter validators and classes with API endpoint implementation.
     * @return  an instance of <code>APIContexts</code>
     */
    private static APIContexts createAPIContexts() {
        APIContexts apictxs = new APIContexts(
            new HashMap<String, Map.Entry<String, APIRouter>>(
                Map.of(
                    "/api/cal/task", new AbstractMap.SimpleEntry<String, APIRouter>(
                        "?id={int}", new TaskRouter()),
                    "/api/cal/timeblock", new AbstractMap.SimpleEntry<String, APIRouter>(
                        "?id={int}", new TimeblockRouter()),
                    "/api/cal/text", new AbstractMap.SimpleEntry<String, APIRouter>(
                        "?id={base64}", new TaskRouter()),
                    "/teapot", new AbstractMap.SimpleEntry<String, APIRouter>(
                        "?msg={char[57]}", new ImATeapotDoubleColon3Router()),
                    "/api/teapot", new AbstractMap.SimpleEntry<String, APIRouter>(
                        "?msg={char[57]}", new ImATeapotDoubleColon3Router())
                )
            )
        );

        return apictxs;
    }

    /**
     * Run an instance of the HTTP server based on <code>com.sun.net.httpserver</code>.
     * @throws IOException
     */
    private static void runServer() throws IOException {
        try {
            server = HttpServer.create(ADDRESS, TCP_BACKLOG);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        System.out.println("[DEBUG] Serving at " + ADDRESS);

        /*SchemaInitializer schema = */new SchemaInitializer();

        /**
         * Connect a default query param filter to each HTTP context.
         */
        apictxs = createAPIContexts();
        for (APIContexts.HTTPContext htc : apictxs.getContexts()) {
            if (!htc.getQueryParamsValidator().isBlank()) {
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