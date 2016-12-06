package ui.main;

import data.Batch;
import data.Project;
import data.Question;
import fileio.AppData;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Squiggs on 12/5/2016.
 */
public class SendAskQuestionRequest extends Service<Boolean> {
    private String session;
    private Question q;

    public SendAskQuestionRequest(String sessionID, Question q) {
        session = sessionID;
        this.q=q;
    }

    protected Task<Boolean> createTask() {
        return new Task<Boolean>(){
            protected Boolean call() throws Exception {
                while(!AppData.send().serverRequest(new Batch()
                                .putString(AppData.Server.Request.Data.KEY, session)
                                .putQuestion(AppData.Server.Request.Data.QUESTION, q)
                        , AppData.Server.Request.ASK_QUESTION));
                return true;
            }
        };
    }
}
