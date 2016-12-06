package ui.main.service;

import fileio.AppData;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Squiggs on 12/5/2016.
 */
public class AskForSessionKeyService extends Service<Boolean> {
    private final long breakAfter;

    public AskForSessionKeyService(long timeout) {
        this.breakAfter = timeout;
    }

    protected Task<Boolean> createTask() {
        return new Task<Boolean>(){

            protected Boolean call() throws Exception {
                System.out.println("Connecting...");
                while(!AppData.send().serverRequest(null, AppData.Server.Request.CONNECT)) wait(1);
                System.out.println("Sending Request");

                long waitTime = breakAfter;
                while(!AppData.send().serverRequest(null, AppData.Server.Request.SESSION_KEY)) {
                    Thread.sleep(1);
                    waitTime--;
                    if(waitTime <= 0) return false;
                }

                return true;
            }
        };
    }
}
