package fileio;

import data.Batch;
import data.Question;
import sun.applet.Main;
import ui.main.MainView;
import ui.start.StartView;

import java.io.File;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainTemo implements AppData.Callback {
    public static void main(String[] args) {
        int i = 0;
        while(true)
            i++;
//        StartView.launch(StartView.class);

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
