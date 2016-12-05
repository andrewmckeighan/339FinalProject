package fileio.net;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
/**
 * Created by Squiggs on 11/28/2016.
 */
public class Connection extends WebSocketClient{

    public Connection(URI serverURI) {
        super(serverURI);
    }


    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("new connection");
    }

    public void onMessage(String message) {
        System.out.println("Message Received: " + message);
    }

    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closing code: " + code + " | reason: " + reason + " | remote: " + remote);
    }

    public void onError(Exception e) {
        System.out.println("Error Received");
        e.printStackTrace();
    }

    public void setSocket(Socket socket) {
        super.getWebSocketFactory();
    }
}
