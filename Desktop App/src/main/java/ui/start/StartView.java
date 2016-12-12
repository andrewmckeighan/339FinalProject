package ui.start;

import fileio.AppData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class StartView extends Application{
    private StartController controller;
    private StartModel model = new StartModel(this);

    @Override
    public void stop() throws Exception {
        AppData.send().serverRequest(null, AppData.Server.Request.DISCONNECT);
    }

    public void start(Stage primaryStage) throws Exception {
        controller = new StartController(model);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("StartView.handle CLOSING");
                Platform.exit();
            }
        });
        model.stage = primaryStage;

        VBox mainPane = new VBox();
        Label stageTitle = new Label(model.title_text);
        VBox centerPane = new VBox();
        VBox bottomPane = new VBox();

        {
            mainPane.setAlignment(Pos.TOP_CENTER);
            mainPane.getChildren().addAll(stageTitle, centerPane, bottomPane);
            mainPane.setBackground(new Background(new BackgroundImage(new Image(model.background_url),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.SPACE,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }

        {
            stageTitle.setAlignment(Pos.CENTER);
            stageTitle.setFont(Font.font(model.font, model.title_size));
        }

        {
            Button newProject = new Button(model.new_project_button_text);
            Button openProject = new Button(model.load_project_button_text);
            newProject.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    controller.process(StartController.NEW_PROJECT);
                }
            });
            openProject.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    controller.process(StartController.LOAD_PROJECT);
                }
            });

            centerPane.getChildren().addAll(newProject, openProject);
            centerPane.setAlignment(Pos.TOP_CENTER);
            centerPane.setSpacing(10);
            centerPane.setPadding(new Insets(7, 0, 133, 0));
        }

        {
            Label authors[] = new Label[model.authors.length];
            for (int x=0; x < model.authors.length; x++){
                authors[x] = new Label(model.authors[x]);
                authors[x].setFont(Font.font(model.font));
            }

            bottomPane.setAlignment(Pos.CENTER_RIGHT);
            bottomPane.getChildren().addAll(authors);
        }
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setResizable(false);
        primaryStage.setTitle(model.window_title);
        primaryStage.setMinWidth(390);
        primaryStage.show();
    }

    // Callbacks to modify the ui go here
}
