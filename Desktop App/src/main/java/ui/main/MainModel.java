package ui.main;

import data.Batch;
import data.Project;
import data.Question;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainModel {
    Stage stage;
    MainView ui;
    public Project project_settings;
    public String question_label = "Question:";
    public String answers_label = "Possible Answers:";
    public String add_more_answers_button_label = "+";
    public Question question = new Question();
    public String remove_answer_button_label = "-";
    public int max_num_questions = 10;
    public int current_num_questions = 1;
    public String session_key_title = "Current Session Key";
    public String get_session_key_button_text = "Request a Key!";
    public String current_session_key = "temp";/*project_settings.settings().getString(Project.settings.SESSION_KEY);*/
    public Label currentSessionKey = new Label("temp");

    public MainModel(MainView mainView) {
        ui = mainView;
    }

    public void updateKey(String s) {
        currentSessionKey.setText("New Key");
    }
}
