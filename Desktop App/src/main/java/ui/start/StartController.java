package ui.start;

import data.Batch;
import fileio.AppData;
import javafx.scene.Parent;

import java.io.File;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class StartController implements AppData.Callback{
    static final int NEW_PROJECT = 1;
    static final int LOAD_PROJECT = 2;

    private StartModel ui;

    public StartController(StartModel stage) {
        ui = stage;
    }

    void process(int buttonPressed) {
        switch(buttonPressed) {
            case NEW_PROJECT:
                //TODO, create a new project
                System.out.println("Create new project called");
                break;
            case LOAD_PROJECT:
                System.out.println("Load project called");
                loadProject();
                break;
        }
    }

    private void loadProject() {
        Batch settings = new Batch()
                .putString(AppData.Local.DIALOGUE_TITLE, "Load Q&A Project File")
                .put(AppData.Local.DIALOGUE_INITIAL_DIR, new File(System.getProperty("user.home")));

        AppData.send().localRequest(settings, ui.getStage(), this, AppData.Local.LOAD_FILE);
    }

    public void handle(int type, Batch response) {
        if(type == AppData.Local.LOAD_FILE) {
            if(response != null) {
                System.out.println(response.toString());
                ui.loadMainUI(response);
                return;
            }
        }
        System.out.println("Didn't work type: " + type + " | response: " + response);
    }
}
