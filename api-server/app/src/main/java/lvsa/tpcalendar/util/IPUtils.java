package lvsa.tpcalendar.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class IPUtils {
    /**
     * Convert IP string to byte array.
     * @param ipString
     * @return IP as byte array.
     * @throws UnknownHostException
     */
    public static byte[] getIPbytes(String ipString) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(ipString);
        byte[] bytes = ip.getAddress();
        
        return bytes;
    }
}
