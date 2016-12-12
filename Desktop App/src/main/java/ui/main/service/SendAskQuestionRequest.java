package ui.main.service;

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
                System.out.println("In createtask");
                Batch answers = new Batch();

                for(int x=0; x < q.getNumAnswers(); x++) {
                    answers.put(""+(x+1), q.getAnswer(x));
                }

                while(!AppData.send().serverRequest(new Batch()
                                .putString(AppData.Server.Request.Data.KEY, session)
                                .putString(AppData.Server.Request.Data.QUESTION, q.question)
                                .putBatch(AppData.Server.Request.Data.ANSWER, answers)
                        , AppData.Server.Request.ASK_QUESTION));
                return true;
            }
        };
    }
}
