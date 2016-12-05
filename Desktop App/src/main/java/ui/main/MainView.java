package ui.main;

import data.Batch;
import data.Project;
import fileio.AppData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainView extends Application {
    MainModel model = new MainModel(this);
    MainController controller = new MainController(model);

    public MainView(Project project) {
        model.project_settings = project;
    }
    public MainView() {

    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("This is the main stage");
        //System.out.println(model.project_settings.toString());


        final GridPane grid = new GridPane();
        {
            Label questionLabel = new Label(model.question_label);

            TextField questionText = new TextField();
            Label answersLabel = new Label(model.answers_label);
            final LinkedList<TextField> answers = new LinkedList<TextField>();
            final Button addMoreAnswers = new Button(model.add_more_answers_button_label);
            final Button removeAnswerButton = new Button(model.remove_answer_button_label);

            for (int x = 0; x < model.max_num_questions; x++) {
                final TextField newTextField = new TextField();
                answers.addLast(newTextField);

                if (x > 0) {
                    newTextField.setVisible(false);
                }
            }

            removeAnswerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    addMoreAnswers.setVisible(true);
                    answers.get(model.current_num_questions - 1).setVisible(false);

                    grid.getChildren().removeAll(addMoreAnswers, removeAnswerButton);
                    grid.add(addMoreAnswers, 1, model.current_num_questions);
                    grid.add(removeAnswerButton, 2, model.current_num_questions - 1);

                    model.current_num_questions--;

                    if (model.current_num_questions <= 1)
                        removeAnswerButton.setVisible(false);

                    model.stage.sizeToScene();
                }
            });
            removeAnswerButton.setVisible(false);

            addMoreAnswers.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    removeAnswerButton.setVisible(true);
                    answers.get(model.current_num_questions).setVisible(true);

                    model.current_num_questions++;

                    grid.getChildren().removeAll(addMoreAnswers, removeAnswerButton);
                    grid.add(addMoreAnswers, 1, model.current_num_questions + 1);
                    grid.add(removeAnswerButton, 2, model.current_num_questions);

                    if (model.current_num_questions >= model.max_num_questions)
                        addMoreAnswers.setVisible(false);

                    model.stage.sizeToScene();
                }
            });

            grid.add(questionLabel, 0, 0);
            grid.add(questionText, 1, 0);
            grid.add(answersLabel, 0, 1);
            grid.add(addMoreAnswers, 1, 2);
            grid.add(removeAnswerButton, 2, 1);
            for (int x = 0; x < model.max_num_questions; x++) {
                grid.add(answers.get(x), 1, x + 1);
            }
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setHgap(10);
            grid.setVgap(10);
        }

        VBox main = new VBox();
        VBox sessionKey = new VBox();
        Label sessionKeyTitle = new Label(model.session_key_title);
        final Button getSessionKeyButton = new Button(model.get_session_key_button_text);
        model.currentSessionKey.setVisible(false);
        getSessionKeyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                getSessionKeyButton.setDisable(true);

                ServerService ss = new ServerService();
                ss.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    public void handle(WorkerStateEvent event) {
                        model.currentSessionKey.setText((String) event.getSource().getValue());
                        getSessionKeyButton.setVisible(false);
                        model.currentSessionKey.setVisible(true);
                    }
                });

                ss.start();

            }
        });

        sessionKey.getChildren().addAll(sessionKeyTitle, getSessionKeyButton, model.currentSessionKey);


        main.getChildren().add(sessionKey);

        model.stage = primaryStage;
        model.stage.setScene(new Scene(main));

        model.stage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        MainView.launch(MainView.class);
    }
}
