package ui.start;

import data.Batch;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class StartModel {
    private StartView ui;

    final String new_project_button_text = "New Project";
    final String load_project_button_text = "Open Project";
    final String title_text = "S E 339 Final Project";
    final String[] authors = {"Josh Engelhardt", "Andrew McKeigan", "Wesley Vansteenburg"};
    final String font = "Segoe UI";
    final int title_size = 26;
    final String background_url="/questions-and-answers-main-screen.jpg";
    final String window_title = "Q&A Desktop App";
    Stage stage;

    public StartModel(StartView stage) {
        ui = stage;
    }

    public Stage getStage() {
        return stage;
    }



    public void loadMainUI(Batch project) {
        

        stage.close();
    }
}
