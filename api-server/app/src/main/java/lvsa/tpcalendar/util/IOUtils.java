package lvsa.tpcalendar.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class IOUtils {

    // https://www.baeldung.com/reading-file-in-java
    // Helper method
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
}