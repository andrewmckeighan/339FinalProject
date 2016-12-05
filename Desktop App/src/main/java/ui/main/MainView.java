package ui.main;

import data.Batch;
import data.Project;
import fileio.AppData;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainView extends Application {
    MainModel model = new MainModel(this);
    MainController controller = new MainController(model);

    public MainView(Project project) {
        model.project_settings = project;
    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("This is the main stage");
        System.out.println(model.project_settings.toString());



        primaryStage.show();
    }
}
