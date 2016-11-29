package data;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class Question {
    String question;

    private ArrayList<String> answers;

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

    public void editAnswer(int location, String newAnswer) {
        this.answers.set(location, newAnswer);
    }
}
