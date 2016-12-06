package ui.main;

import data.Batch;
import data.Project;
import data.Question;
import fileio.AppData;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import static fileio.AppData.Server;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainController implements AppData.Callback {
    MainModel ui;
    private AppData.Callback sessionCallback;

    public MainController(MainModel model) {
        ui = model;
        initServerHandlers();
    }

    private void initServerHandlers() {
        AppData.send().subscribeToServerResponse(Server.Response.RECEIVE_SESSION_KEY, this);
        AppData.send().subscribeToServerResponse(Server.Response.RECEIVE_SENT_QA_CONFIRMATION, this);
        AppData.send().subscribeToServerResponse(Server.Response.RECEIVE_RESULTS, this);
    }

    public void handle(int type, Batch response) {
        System.out.println("Type: " + type + " Response: " + response);
        switch(type) {
            case Server.Response.RECEIVE_SESSION_KEY:
                String key = response.getString(Server.Response.Data.SESSION_KEY);
                ui.project_settings.settings().putString(Project.settings.SESSION_KEY, key);
                sessionCallback.handle(type, response);
                break;
        }

    }

    public void endQuestion() {
    }

    public void askQuestion(TextField question, LinkedList<TextField> answers) {
        Question q = new Question()
                .ask(question.getText());
        for(TextField t: answers) {
                q.withAnswers(t.getText());
        }
        ui.project_settings.settings().putQuestion(Project.settings.CURRENT_QUESTION, q);

        SendAskQuestionRequest r = new SendAskQuestionRequest(ui.project_settings.settings().getString(Project.settings.SESSION_KEY), q);
        r.start();
    }

    public void askForSessionKey(AppData.Callback anonymous) {
        sessionCallback = anonymous;
    }
}
