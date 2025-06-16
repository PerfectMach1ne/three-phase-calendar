package lvsa.tpcalendar.utils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public final class PropsService {
    private Properties props;

    public PropsService() {
        props = loadProperties();
    }

    private Properties getProps() {
        return props;
    }

    /**
     * Get contents of the properties file and return them as a Properties object.
     * @return contents of .properties file.
     */
    private Properties loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("tpc_testing.properties");

        Properties props = new Properties();
        Iterator<String> iter = IOUtils.readPropsFromInputStream(is).iterator();
        while (iter.hasNext()) {
            String[] propPair = iter.next().split("="); 
            props.setProperty(propPair[0], propPair[1]);
        }

        return props;
    }

    private Properties truncate(Properties props, Set<String> keys) {
        for (String propKey : props.stringPropertyNames()) {
            if (!keys.contains(propKey)) {
                props.remove(propKey);
            }
        }
        return props;
    }

    /**
     * Get props related to database connection only.
     * @return truncated Properties object.
     */
    public Properties getDBProps() {
        return truncate(getProps(), Set.of("url", "username", "password"));
    }

    /**
     * Get props related to HTTP/HTTPS server setup only. 
     * @return truncated Properties object.
     */
    public Properties getIPProps() {
        return truncate(getProps(), Set.of("ip", "port"));
    }

    /**
     * Get props related to JWT encryption only.
     * @return truncated Properties object.
     */
    public Properties getRsaKeys() {
        return truncate(getProps(), Set.of("key_id", "priv_key"));
    }
}
