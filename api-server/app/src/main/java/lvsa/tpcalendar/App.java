package lvsa.tpcalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lvsa.tpcalendar.dbutils.DatabaseHandler;
import lvsa.tpcalendar.dbutils.SchemaInitializer;
import lvsa.tpcalendar.model.TaskEvent;
import lvsa.tpcalendar.util.Colors;

public class App {
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
        int port = 8057;
        InetSocketAddress address = new InetSocketAddress(port);
        HttpServer server = null;

        try {
            server = HttpServer.create(address, port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        SchemaInitializer schema = new SchemaInitializer();

        server.createContext("/teapot", new TPCalHttpHandler());

        server.createContext("/testTask", new HttpHandler() {

            @Override
            public void handle(HttpExchange het) throws IOException {
                class HTTPRequest {
                    final String HTTP_METHOD = het.getRequestMethod();

                    HTTPRequest(String response/* , int status */) throws IOException {
                        Headers resq = het.getRequestHeaders();
                        System.out.println(resq.get("Host") + " " + HTTP_METHOD + " /testTask " + het.getProtocol());

                        InputStream is = het.getRequestBody();

                        InputStreamReader isReader = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isReader);
                        StringBuffer sb = new StringBuffer();
                        String str;
                        while ( (str = reader.readLine()) != null ) {
                            sb.append(str);
                            // System.out.println(str);
                        }

                        Headers resh = het.getResponseHeaders();
                        resh.set("Content-Type", "application/json");
                        System.out.println(resh.toString());

                        String res = response;
                        int status = 201;
                        // 0 to use Chunked Transfer Coding
                        // https://www.rfc-editor.org/rfc/rfc9112.html#name-chunked-transfer-coding
                        het.sendResponseHeaders(status, 0);

                        OutputStream os = het.getResponseBody();
                        os.write(res.getBytes());
                        os.flush();
                        is.close(); os.close();
                    }
                }
                if (het.getRequestMethod().equals("GET")) {}
                else if (het.getRequestMethod().equals("POST")) {}
                else if (het.getRequestMethod().equals("PUT")) {}
                else if (het.getRequestMethod().equals("DELETE")) {}
                else {}

                if (het.getRequestMethod().equals("GET")) {
                    Headers resq = het.getRequestHeaders();
                    System.out.println(resq.get("Host") + " GET /testTask HTTP/1.1");

                    String res = "{ \"deleteMeLaterName\": \"no\" }";

                    het.getResponseHeaders().set("Content-Type", "application/json");
                    het.sendResponseHeaders(200, 0);

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    os.close();
                } else if (het.getRequestMethod().equals("POST")) {
                    Headers resq = het.getRequestHeaders();
                    System.out.println(resq.get("Host") + " POST /testTask HTTP/1.1");
                    
                    InputStream is = het.getRequestBody();

                    InputStreamReader isReader = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isReader);
                    StringBuffer sb = new StringBuffer();

                    String str;
                    while ( (str = reader.readLine()) != null ) {
                        sb.append(str);
                        // System.out.println(str);
                    }

                    JsonObject jsonObj = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    TaskEvent taskEvent = new TaskEvent();
                    taskEvent.create(jsonObj);
                    System.out.println("Taskname: " + taskEvent.getName() + "\n"
                                     + "Description: " + taskEvent.getDescription() + "\n"
                                     + "Date: " + taskEvent.getDateTime().toLocalDate().toString() + "\n"
                                     + "Hour: " + taskEvent.getDateTime().toLocalTime().toString() + "\n"
                                     + "Color: " + taskEvent.getColor());

                    Headers resh = het.getResponseHeaders();
                    resh.set("Content-Type", "application/json");
                    System.out.println(resh.toString());

                    String res = "{ \"boink\": \"" + "wheh?" + "\" }\n";
                    // 0 to use Chunked Transfer Coding
                    // https://www.rfc-editor.org/rfc/rfc9112.html#name-chunked-transfer-coding
                    het.sendResponseHeaders(201, 0);

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    os.close();
                    is.close();
                } else if (het.getRequestMethod().equals("PUT")) {
                    Headers resq = het.getRequestHeaders();
                    System.out.println(resq.get("Host") + " PUT /testTask HTTP/1.1");

                    InputStream is = het.getRequestBody();

                    InputStreamReader isReader = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isReader);
                    StringBuffer sb = new StringBuffer();

                    String str;
                    while ( (str = reader.readLine()) != null ) {
                        sb.append(str);
                        System.out.println(str);
                    }

                    String res = "PUT testTask \n"; 
                    het.sendResponseHeaders(200, 0);

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    is.close(); os.close();
                } else if (het.getRequestMethod().equals("DELETE")) {
                    Headers resq = het.getRequestHeaders();
                    System.out.println(resq.get("Host") + " DELETE /testTask HTTP/1.1");

                    InputStream is = het.getRequestBody();

                    InputStreamReader isReader = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isReader);
                    StringBuffer sb = new StringBuffer();

                    String str;
                    while ( (str = reader.readLine()) != null ) {
                        sb.append(str);
                        System.out.println(str);
                    }

                    String res = "DELETE testTask \n"; 
                    het.sendResponseHeaders(200, 0);

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    is.close(); os.close();
                }
            }
        });

        server.setExecutor(null);
        server.start();
    }
}