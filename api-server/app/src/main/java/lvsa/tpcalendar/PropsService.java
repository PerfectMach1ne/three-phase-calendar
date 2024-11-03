package lvsa.tpcalendar;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import lvsa.tpcalendar.util.IOUtils;

public final class PropsService {
    private Properties props;

    public PropsService() {
        props = loadProperties();
    }

    private Properties getProps() {
        return props;
    }

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

    public Properties getDBProps() {
        Properties truncatedProps = getProps();

        truncatedProps.remove("ip");
        truncatedProps.remove("port");

        return truncatedProps;
    }

    public Properties getIPProps() {
        Properties truncatedProps = getProps();

        truncatedProps.remove("url");
        truncatedProps.remove("username");
        truncatedProps.remove("password");

        return truncatedProps;
    }
}
