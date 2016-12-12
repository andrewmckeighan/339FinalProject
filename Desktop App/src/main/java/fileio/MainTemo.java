package fileio;

import data.Batch;
import data.Question;
import fileio.net.SocketConnection;
import org.json.JSONException;
import org.json.JSONObject;
import sun.applet.Main;
import ui.main.MainView;
import ui.start.StartView;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainTemo implements AppData.Callback {
    public static void main(String[] args) throws Exception {
        StartView.launch(StartView.class);
    }

    public void handle(int type, Batch response) {
        if(type == AppData.Local.LOAD_FILE)
            if(response != null)
                System.out.println(response.toString());
            else
                System.out.println("null response");
        else if(type == AppData.Local.SAVE_FILE) {
            System.out.println(response.toString());
        }
        else
            System.out.println("I tried");
    }
}
