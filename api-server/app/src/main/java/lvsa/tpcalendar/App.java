package lvsa.tpcalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import lvsa.tpcalendar.colors.Colors;
import lvsa.tpcalendar.dbutils.DatabaseHandler;
import lvsa.tpcalendar.model.TaskEvent;

public class App {
    private static TaskEvent deleteMeLater;

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
        InetSocketAddress cd = new InetSocketAddress(port);
        HttpServer server = null;
        DatabaseHandler db = null;

        try {
            server = HttpServer.create(cd, port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        try {
            db = new DatabaseHandler();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        server.createContext("/teapot", new TPCalHttpHandler());

        server.createContext("/testTask", new HttpHandler() {

            @Override
            public void handle(HttpExchange het) throws IOException {
                if (het.getRequestMethod().equals("GET")) {
                    System.out.println("GET task :)");

                    // String res = "GET testTask " + deleteMeLater.getName() 
                    //              + "\ncolor HEX code = " + deleteMeLater.getColor() 
                    //              + "\ncreated at " + deleteMeLater.getCreatedDate()
                    //              + "\nlast updated at " + deleteMeLater.getUpdatedDate() + "\n";
                    String res = "{ \"deleteMeLaterName\": \"" + deleteMeLater.getName() + "\" }";

                    het.getResponseHeaders().set("Content-Type", "application/json");
                    het.sendResponseHeaders(200, res.length());

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    os.close();
                } else if (het.getRequestMethod().equals("POST")) {
                    System.out.println("POST task :))");

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
                                     + "Color: " + taskEvent.getColor());

                    Headers resh = het.getResponseHeaders();
                    resh.set("Content-Type", "application/json");
                    System.out.println(resh.toString());
                    

                    String res = "{ \"boink\": \"" + "wheh?" + "\" }";

                    // 0 to use Chunked Transfer Coding
                    // https://www.rfc-editor.org/rfc/rfc9112.html#name-chunked-transfer-coding
                    het.sendResponseHeaders(201, 0);

                    OutputStream os = het.getResponseBody();
                    os.write(res.getBytes());
                    os.flush();
                    os.close();
                    is.close();
                } else if (het.getRequestMethod().equals("PUT")) {
                    System.out.println("PUT (UPDATE) task :)))");

                    InputStream is = het.getRequestBody();

                    InputStreamReader isReader = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isReader);
                    StringBuffer sb = new StringBuffer();

                    String str;
                    while ( (str = reader.readLine()) != null ) {
                        sb.append(str);
                        System.out.println(str);
                    }

                    String res = "PUT testTask " + deleteMeLater.getName() + "\n"; 

                    het.sendResponseHeaders(200, res.length());

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