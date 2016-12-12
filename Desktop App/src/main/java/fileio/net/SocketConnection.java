package fileio.net;

import data.Batch;
import data.Question;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Squiggs on 12/4/2016.
 */
public class SocketConnection {

    //TODO fill out static field
    //The server request fields
    public static final String REQUEST_SESSION_KEY = "getKey";
    public static final String ASK_A_QUESTION = "submitQA";
    public static final String CLOSE = "close";

    //The server response values
    public static final String GET_SESSION_KEY = "sendKey";
    public static final String GET_ASK_CONFIRMATION = "submitConf";
    public static final String GET_RESULTS = "sendResults";

    //The values in the JSON Objects received
    public static final String SESSION_KEY = "session";
    public static final String ASK_CONFIRMATION = "conf";
    public static final String RESULT_DATA = "answers";


    //The values in the JSON Objects sent


    private final Socket conn;

    public SocketConnection() throws URISyntaxException {
        conn = IO.socket("http://localhost:6668", setOptions());
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
                if (objects.length > 0 && objects[0] instanceof String) {
                    try {
                        String cast = (String) objects[0];
                        System.out.println("SocketConnection.call Arrays.toString(objects)=" + Arrays.toString(objects));
                        System.out.println("SocketConnection.call cast=" + cast);
                        listener.call(JSONtoBatch(new JSONObject(cast), new Batch()));
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        listener.call(null);
                    }

                } else {
                    listener.call(null);
                    System.out.println("SocketConnection.call WHAT");
                }
            }
        });

        return this;
    }

    public SocketConnection emit(String eventName, Batch data) {
        if (data != null)
            conn.emit(eventName, BatchToJSON(data, new JSONObject()));
        else
            conn.emit(eventName);
        return this;
    }

    public void connect() {
        conn.connect();
    }

    public void disconnect(Batch serverData) {
//        conn.disconnect();
    }

    public boolean isConnected() {
        return conn.connected();
    }

    private Batch JSONtoBatch(JSONObject json, Batch out) {
        if (json == null)
            throw new NullPointerException("JSON cannot be null");
        if (out == null)
            throw new NullPointerException("Out parameter cannot be null");

        Iterator<String> jsonKeys = json.keys();

        while (jsonKeys.hasNext()) {
            String key = jsonKeys.next();

            try {
                Object obj = json.get(key);

                if (obj instanceof JSONObject) {
                    out.putBatch(key, JSONtoBatch((JSONObject) obj, new Batch()));
                } else if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) {
                        out.putBoolean(key, Boolean.parseBoolean(str));
                    } else {
                        try {
                            //Test to see if it's an integer
                            out.putInteger(key, Integer.parseInt(str));
                        } catch (NumberFormatException e) {
                            try {
                                //Test to see if its a Double
                                out.putDouble(key, Double.parseDouble(str));
                            } catch (NumberFormatException e2) {
                                //Must be a String and not another accepted type
                                out.putString(key, str);
                            }
                        }
                    }
                } else if (obj instanceof Integer) {
                    out.putInteger(key, (Integer) obj);
                }
            } catch (JSONException e) {
                //Not a valid key, I guess
            }
        }


        return out;
    }

    private JSONObject BatchToJSON(Batch batch, JSONObject out) {
        if (batch == null)
            throw new NullPointerException("Batch cannot be null");
        if (out == null)
            throw new NullPointerException("Out parameter cannot be null");

        Iterator<String> keys = batch.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();

            Object obj = batch.get(key);
            try {
                if (obj instanceof Batch) {
                    out.put(key, BatchToJSON((Batch) obj, new JSONObject()));
                } else if (obj instanceof Question) {
                    out.put(key, Question.toJSON((Question) obj));
                } else if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Boolean) {
                    String placement = "" + obj;
                    out.put(key, placement);
                } else if (obj instanceof File) {
                    out.put(key, (File) obj);
                }
            } catch (JSONException e) {
                //Don't add it, I guess
            }
        }

        return out;
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
