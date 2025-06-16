package lvsa.tpcalendar.utils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

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

    /**
     * Get props related to database connection only.
     * @return truncated Properties object.
     */
    public Properties getDBProps() {
        Properties truncatedProps = getProps();

        truncatedProps.remove("key_id");
        truncatedProps.remove("priv_key");

        truncatedProps.remove("ip");
        truncatedProps.remove("port");

        return truncatedProps;
    }

    /**
     * Get props related to HTTP/HTTPS server setup only. 
     * @return truncated Properties object.
     */
    public Properties getIPProps() {
        Properties truncatedProps = getProps();

        truncatedProps.remove("key_id");
        truncatedProps.remove("priv_key");

        truncatedProps.remove("url");
        truncatedProps.remove("username");
        truncatedProps.remove("password");

        return truncatedProps;
    }

    public Properties getRsaKeys() {
        Properties truncatedProps = getProps();

        truncatedProps.remove("ip");
        truncatedProps.remove("port");

        truncatedProps.remove("url");
        truncatedProps.remove("username");
        truncatedProps.remove("password");

        return truncatedProps;
    }
}
