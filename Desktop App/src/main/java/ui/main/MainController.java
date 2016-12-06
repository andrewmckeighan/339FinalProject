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
    private AppData.Callback confirmationCallback;

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
        System.out.println("MainController | Type: " + type + " Response: " + response);
        String key;
        switch(type) {
            case Server.Response.RECEIVE_SESSION_KEY:
                key = response.getString(Server.Response.Data.SESSION_KEY);
                ui.project_settings.settings().putString(Project.settings.SESSION_KEY, key);
                sessionCallback.handle(type, response);
                break;
            case Server.Response.RECEIVE_SENT_QA_CONFIRMATION:
                boolean conf = response.getBoolean(Server.Response.Data.CONFIRMATION);
                System.out.println("conf: " + conf);
                confirmationCallback.handle(type, response);
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

        System.out.println("Sending request");
        SendAskQuestionRequest r = new SendAskQuestionRequest(ui.project_settings.settings().getString(Project.settings.SESSION_KEY), q);
        r.start();
    }

    public void askForSessionKey(final AppData.Callback anonymous) {
        sessionCallback = anonymous;
    }

    public void askForConfirmation(final AppData.Callback anonymous) {
        confirmationCallback = anonymous;
    }

    public void getSessionKey() {
        while(!AppData.send().serverRequest(null, AppData.Server.Request.CONNECT));

        AppData.send().subscribeToServerResponse(Server.Response.RECEIVE_SESSION_KEY, sessionCallback);
        while(!AppData.send().serverRequest(null, Server.Request.SESSION_KEY));

    }
}
