package ui.start;

import data.Batch;
import data.Project;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ui.main.MainView;

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

    public void createProject() {
        openNextWindow(new Project());
    }


    public void openProject(Batch project) {
        if(Project.verifyProjectStructure(project)) {
            openNextWindow(new Project(project));
        } else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Project could not be loaded...");
           alert.setHeaderText(null);
           alert.setContentText("The project selected is either corrupt or out of date.\nPlease update project file before continuing.");
           alert.show();
        }
    }

    private void openNextWindow(final Project project) {
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    new MainView(project).start();

                    stage.close();
                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed...");
                }
            }
        });
    }
}
