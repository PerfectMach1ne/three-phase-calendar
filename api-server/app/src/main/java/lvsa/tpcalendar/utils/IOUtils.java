package lvsa.tpcalendar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class IOUtils {
    /**
     * Helper method for reading .properties files.
     * https://www.baeldung.com/reading-file-in-java
     * @param is an <code>InputStream</code>
     * @return ArrayList<String> of lines 
     */
    public static ArrayList<String> readPropsFromInputStream(InputStream is) {
        ArrayList<String> propsList = new ArrayList<String>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ( (line = br.readLine()) != null ) {
                propsList.add(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return propsList;
    }

    /**
     * Helper method for reading other files into a buffer.
     * @param is an <code>InputStream</code>
     * @return String of file contents
     */
    public static String readAnyResource(String resource) {
        StringBuilder sb = new StringBuilder();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        
        if (is == null) throw new IllegalArgumentException("Resource not found: " + resource);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ( (line = br.readLine()) != null ) {
                sb.append(line).append("\n");
            }
        } catch (IOException ioe) {
            System.out.println("Failed to read resource: " + resource);
            ioe.printStackTrace();
        }

        return sb.toString();
    }
}