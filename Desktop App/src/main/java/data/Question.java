package data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class Question implements Serializable{
    String question;

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

    public void editAnswer(int location, String newAnswer) {
        this.answers.set(location, newAnswer);
    }

    @Override
    public String toString() {
        return question + ": " + answers.toString();
    }
}
