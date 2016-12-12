package ui.main.service;

import data.Batch;
import fileio.AppData;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Squiggs on 12/8/2016.
 */
public class AskForResultsService extends Service<Boolean> {
    private final String key;
    private final long breakAfter;

    public AskForResultsService(String sessionKey, long timeout) {
        key = sessionKey;
        breakAfter = timeout;
    }

    protected Task<Boolean> createTask() {
        return new Task<Boolean>(){

            protected Boolean call() throws Exception {
                long timeLeft = breakAfter;
                while(!AppData.send().serverRequest(new Batch().putString(AppData.Server.Request.Data.KEY, key), AppData.Server.Request.END_QUESTION))
                {
                    Thread.sleep(timeLeft);
                    timeLeft--;
                    if(timeLeft <= 0)
                        return false;
                }
                return true;
            }
        };
    }
}
