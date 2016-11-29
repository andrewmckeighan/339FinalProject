package fileio;

import com.google.gson.Gson;
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
        try {
            client = new Connection(new URI("ws://localhost:80"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();
    }

    public static AppData request() {
        return singleton;
    }

    public void sessionKey(SessionKeyHandler callback) {
        //TODO
        client.send("test");
        callback.handleSessionKeyResult(null);
    }

    public void askQuestion(Question ques, AskQuestionResultHandler callback) {
        //TODO

        callback.handleAskingAQuestion(false);
    }


    public interface CallbackHandler{}

    public interface SessionKeyHandler extends CallbackHandler {
        void handleSessionKeyResult(String key);
    }

    public interface AskQuestionResultHandler extends CallbackHandler{
        void handleAskingAQuestion(boolean serverResult);
    }
}
