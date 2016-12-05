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

        //StartView.launch(StartView.class);

        SocketConnection test = new SocketConnection("http://localhost");

        JSONObject j = Question.toJSON(new Question().ask("Here is my question").withAnswers("A", "B", "C", "D"));

        Batch sendingBatch = new Batch().putQuestion(AppData.Server.Request.Data.QUESTION, new Question().ask("Here is my question").withAnswers("A", "B", "C", "D"))
                                        .put(AppData.Server.Request.Data.KEY, "randomkey");

        AppData.send().serverRequest(sendingBatch, AppData.Server.Request.ASK_QUESTION);
        /*
        exampleData.putString("Project Name", "Stupid Questions");
        exampleData.putQuestion("Question1", new Question().ask("What is life's goal?").withAnswers("There is none", "Go home"));

        File saveLocation = new File("exampleFile.txt");

        System.out.println("Saved");

        System.out.println(exampleData.toString());
        */
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
