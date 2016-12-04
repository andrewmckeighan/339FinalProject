package ui.main;

import data.Batch;
import fileio.AppData;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainController implements AppData.Callback {
    MainModel ui;

    public MainController(MainModel model) {
        ui = model;
    }

    public void handle(int type, Batch response) {
        System.out.println("Type: " + type + " Response: " + response);
    }
}
