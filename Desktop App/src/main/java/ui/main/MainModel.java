package ui.main;

import data.Batch;
import data.Project;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainModel {
    Stage stage;
    MainView ui;
    public Project project_settings;

    public MainModel(MainView mainView) {
        ui = mainView;
    }
}
