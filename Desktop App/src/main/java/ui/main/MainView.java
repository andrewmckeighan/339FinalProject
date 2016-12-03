package ui.main;

import data.Batch;
import fileio.AppData;
import fileio.MainTemo;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainView extends Application{
    public void start(Stage primaryStage) throws Exception {
        Batch openFile = new Batch().putString(AppData.Local.DIALOGUE_TITLE, "Open a file")
                                    .put(AppData.Local.DIALOGUE_INITIAL_DIR, new File("C:\\Users\\Squiggs\\Documents\\GitHub\\339FinalProject\\Desktop App"));
        Batch saveFile = new Batch()
                .putBatch(AppData.Local.SAVE_DATA, new Batch()
                        .putString("MyName", "Wes")
                        .putInteger("myAge", 21)
                        .putDouble("My weight", 160.2))
                .putString(AppData.Local.DIALOGUE_TITLE, "Save a file")
                .put(AppData.Local.DIALOGUE_INITIAL_DIR, new File("C:\\Users\\Squiggs\\Documents\\GitHub\\339FinalProject\\Desktop App"));

        System.out.println("Before Call");
        //AppData.send().localRequest(openFile, primaryStage, new MainTemo(), AppData.Local.LOAD_FILE);
        AppData.send().localRequest(saveFile, primaryStage, new MainTemo(), AppData.Local.SAVE_FILE);

        System.out.println("After call");

        primaryStage.show();
    }
}
