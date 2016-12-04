package ui.main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainView extends Application {
    MainModel model = new MainModel(this);
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
    }
}
