package data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class Question implements Serializable{
    public String question;

    private ArrayList<String> answers;

    public static final long serialVersionUID = 7923005923l;

    public Question() {
        question = "";
        answers = new ArrayList<String>(5);
    }

    public static Question create(){
        return new Question();
    }

    public Question ask(String questionName) {
        question = questionName;
        return this;
    }

    public Question withAnswer(String answer) {
        answers.add(answer);
        return this;
    }

    public Question withAnswers(String... arrayOfAnswers) {
        Collections.addAll(this.answers, arrayOfAnswers);
        return this;
    }

    public int getNumAnswers() {
        return this.answers.size();
    }

    public String getAnswer(int loc) {
        return this.answers.get(loc);
    }

    public void editAnswer(int location, String newAnswer) {
        this.answers.set(location, newAnswer);
    }

    public static JSONObject toJSON(Question question) throws JSONException {
        JSONObject json = new JSONObject();

            json.put("Question", question.question);
            JSONObject answerArray = new JSONObject();
            for(int x = 0; x < question.answers.size(); x++) {
                answerArray.put(""+(x+1), question.answers.get(1));
            }
            json.put("Answers", answerArray);


        return json;
    }

    @Override
    public String toString() {
        return question + ": " + answers.toString();
    }
}
