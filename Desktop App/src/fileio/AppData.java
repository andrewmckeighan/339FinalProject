package fileio;

import com.google.gson.Gson;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class AppData {

    private static AppData singleton = new AppData();

    private AppData() {}

    public static AppData request() {
        return singleton;
    }

    public void sessionKey(SessionKeyHandler callback) {
        //TODO

        callback.handleSessionKeyResult(null);
    }

    public void

    public interface SessionKeyHandler {
        void handleSessionKeyResult(String key);
    }
}
