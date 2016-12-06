package fileio.net;

import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.Security;
import java.util.Arrays;

/**
 * Created by Squiggs on 12/4/2016.
 */
public class SSLConnectionExample {
    public static void main(String[] args) throws Exception{
        WebSocketImpl.DEBUG = true;

        Connection client = new Connection(new URI("wss://localhost:8887"));

        String STORETYPE = "JKS";
        String KEYSTORE = "C:\\Users\\Squiggs\\Documents\\GitHub\\339FinalProject\\Desktop App\\target\\classes\\keystore.jks";
        String STOREPASSWORD = "password";
        String KEYPASSWORD = "finalProject";

        System.out.println(Arrays.toString(Security.getProviders()));


        KeyStore ks = KeyStore.getInstance(STORETYPE);
        File kf = new File(KEYSTORE);
        ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, KEYPASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        client.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));

        client.connectBlocking();

        java.util.Scanner in = new java.util.Scanner(System.in);

        while(true)
        {
            String line = in.nextLine();
            if(line.equals("close"))
                break;
            else
                client.send(line);
        }
    }
}
