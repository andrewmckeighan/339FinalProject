package fileio.net;

import data.Batch;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Squiggs on 12/4/2016.
 */
public class SocketConnection {

    //TODO fill out static field
    public static final String REQUEST_SESSION_KEY = "getKey";
    public static final String ASK_A_QUESTION = "submitQA";
    public static final String RESOLVE_A_QUESTION = "getQA";

    public static final String GET_SESSION_KEY = "sendKey";
    public static final String GET_ASK_CONFIRMATION = "";
    public static final String GET_RESULTS = "";


    private final Socket conn;

    public SocketConnection() throws URISyntaxException {
        conn = IO.socket("http://localhost:8886", setOptions());
    }

    public SocketConnection(String uri) throws URISyntaxException {
        conn = IO.socket(uri, setOptions());
    }

    private IO.Options setOptions() {
        IO.Options options = new IO.Options();
        options.reconnectionAttempts = 5;
        return options;
    }

    public SocketConnection on(String eventName, final Listener listener) {
        conn.on(eventName, new Emitter.Listener() {
            public void call(Object... objects) {
                if(objects.length > 1 && objects[0] instanceof JSONObject)
                    listener.call(JSONtoBatch((JSONObject)objects[0]));
                else
                    listener.call(null);
            }
        });

        return this;
    }

    public SocketConnection emit(String eventName, Batch data) {
        if(data != null)
            conn.emit(eventName, BatchToJSON(data));
        else
            conn.emit(eventName);
        return this;
    }

    public void connect() {
        conn.connect();
    }

    public void disconnect() {
        conn.disconnect();
    }

    public boolean isConnected()
    {
        return conn.connected();
    }

    private Batch JSONtoBatch(JSONObject json) {
        return null;
    }

    private JSONObject BatchToJSON(Batch batch) {
        return null;
    }

    public interface Listener {
        void call(Batch object);
    }
}

/*
public static void main(String[] args) throws Exception {
        final Socket socket = IO.socket("http://localhost:8886");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... objects) {
                System.out.println("Connected");
                socket.emit("I'm doing this?");
            }
        }).on("event", new Emitter.Listener() {
            public void call(Object... objects) {
                System.out.println("event");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            public void call(Object... objects) {
                System.out.println("Disconnected");
            }
        });

        System.out.println("Before connect");
        socket.connect();
        System.out.println("End of main thread");
    }
 */
