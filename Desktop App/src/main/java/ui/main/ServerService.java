package ui.main;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Squiggs on 12/5/2016.
 */
public class ServerService extends Service<String> {
    protected Task<String> createTask() {
        return new Task<String>(){

            protected String call() throws Exception {

                return "This is the result of the call";
            }
        };
    }
}
