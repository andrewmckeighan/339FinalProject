package fileio;

import com.google.gson.Gson;
import data.Batch;
import data.Question;
import fileio.net.Connection;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class AppData {

    private final static AppData singleton = new AppData();

    public Connection client;

    private AppData() {
        /*try {
            client = new Connection(new URI("ws://localhost:80"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();*/
    }

    public static AppData send() {
        return singleton;
    }

    public void serverRequest(Batch serverData, Callback callback, int type) {

    }

    public void localRequest(Batch serverData, Callback callback, int type) {
        switch(type) {
            case Local.LOAD_FILE:
                //TODO
                break;
            case Local.SAVE_FILE:
                //TODO
                break;
            case Local.SAVE_PROJECT_FILE:
                //TODO
                break;
            case Local.LOAD_PROJECT_FILE:

                break;
            default:
                throw new IllegalArgumentException("Local Request #"+type+" is not a recognized request.");
        }
    }


    public interface Callback {
        void handle(int type, Batch response);
    }

    public class Local {
        public static final int SAVE_FILE = 1;
        public static final int LOAD_FILE = 2;
        public static final int SAVE_PROJECT_FILE = 3;
        public static final int LOAD_PROJECT_FILE = 4;
    }
}
