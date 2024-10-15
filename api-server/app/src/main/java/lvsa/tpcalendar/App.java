package lvsa.tpcalendar;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Locale;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.dbutils.SchemaInitializer;
import lvsa.tpcalendar.model.TaskEvent;
import lvsa.tpcalendar.util.Colors;
import lvsa.tpcalendar.util.IOUtils;

public class App {
    private static final int PORT = 8057;
    private static final InetSocketAddress ADDRESS = new InetSocketAddress(PORT);
    private static final int TCP_BACKLOG = 128; // Re: Note about socket backlogs (https://docs.oracle.com/en/java/javase/21/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html)
    private static HttpServer server = null;

    public static final String REGISTERED_NURSE = "\r\n";

    public static void main(String[] args) {
        try {
            TaskEvent testTaskEvent = new TaskEvent();
            System.out.println(testTaskEvent.getUpdatedDate());
            testTaskEvent.setColor(Colors.LAVENDER);
            System.out.println(testTaskEvent.getUpdatedDate());
            runServer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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

        server.createContext("/teapot", new HttpHandler() {
            @Override
            public void handle(HttpExchange htex) throws IOException {
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
        });

        server.createContext("/testTask", new HttpHandler() {

            @Override
            public void handle(HttpExchange htex) throws IOException {
                System.out.println("[DEBUG] address == " + ADDRESS);
                class HTTPRequest {
                    final String HTTP_METHOD = htex.getRequestMethod();
                    HTTPStatusCode status = HTTPStatusCode.HTTP_418_IM_A_TEAPOT;

                    HTTPRequest(String method) throws IOException {
                        Headers reqh = htex.getRequestHeaders();
                        System.out.println(reqh.get("Host") + " " + HTTP_METHOD + " /testTask " + htex.getProtocol());

                        InputStream is = htex.getRequestBody();

                        InputStreamReader isReader = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isReader);
                        StringBuffer sb = new StringBuffer();
                        String reqdata;
                        while ( (reqdata= reader.readLine()) != null ) {
                            sb.append(reqdata);
                        }
                        
                        Headers resh = htex.getResponseHeaders();
                        resh.set("Content-Type", "application/json");
                        String res = null;

                        JsonObject jsonObj = null;
                        switch (method) {
                            case "GET":
                                jsonObj = JsonParser.parseString(sb.toString()).getAsJsonObject();
                                Object[] dbResult = TaskEvent.findAndFetchFromDB(jsonObj.get("hashcode").getAsInt());
                                if (dbResult[0] == HTTPStatusCode.HTTP_404_NOT_FOUND && dbResult[1] == null) {
                                    status = HTTPStatusCode.HTTP_404_NOT_FOUND;
                                    break;
                                }
                                jsonObj = (JsonObject)dbResult[1];
                                res = jsonObj.toString() + REGISTERED_NURSE;
                                status = HTTPStatusCode.HTTP_200_OK;
                                break;
                            case "POST":
                                jsonObj = JsonParser.parseString(sb.toString()).getAsJsonObject();
                                TaskEvent taskEvent = new TaskEvent();
                                status = taskEvent.create(jsonObj);
                                System.out.println("Hashcode: " + taskEvent.hashCode() + '\n'
                                                + "Taskname: " + taskEvent.getName() + "\n"
                                                + "Description: " + taskEvent.getDescription() + "\n"
                                                + "Date: " + taskEvent.getDateTime().toLocalDate().toString() + "\n"
                                                + "Hour: " + taskEvent.getDateTime().toLocalTime().toString() + "\n"
                                                + "Color: " + taskEvent.getColor());
                                break;
                            case "PUT":
                                status = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
                                break;
                            case "DELETE":
                                status = HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
                                break;
                            case "HEAD":
                            case "CONNECT":
                            case "OPTIONS":
                            case "TRACE":
                                System.out.println(method + " /testTask " + " 405 Method Not Allowed HTTP/1.1");
                                status = HTTPStatusCode.HTTP_405_METHOD_NOT_ALLOWED;
                                break;
                            default:
                                status = HTTPStatusCode.HTTP_400_BAD_REQUEST;
                        }

                        if (res == null) {
                            JsonObject resJson = new JsonObject(); 
                            resJson.add("boink", new JsonPrimitive("wheh"));
                            res = resJson.getAsString() + REGISTERED_NURSE;
                        }

                        // String res = "{ \"boink\": \"" + "wheh?" + "\" }\n";
                        
                        // 0 to use Chunked Transfer Coding
                        // https://www.rfc-editor.org/rfc/rfc9112.html#name-chunked-transfer-coding
                        htex.sendResponseHeaders(status.getint(), 0);

                        OutputStream os = htex.getResponseBody();
                        os.write(res.getBytes());
                        os.flush();
                        is.close(); os.close();
                    }
                }

                new HTTPRequest(htex.getRequestMethod().toUpperCase());

            }
        });

        server.setExecutor(null);
        server.start();
    }
}