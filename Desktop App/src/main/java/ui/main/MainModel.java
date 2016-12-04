package ui.main;

import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainModel {
    Stage stage;
    MainView ui;

    public MainModel(MainView mainView) {
        ui = mainView;
    }
}
