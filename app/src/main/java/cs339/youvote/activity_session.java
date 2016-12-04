package cs339.youvote;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class activity_session extends AppCompatActivity {

    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.session_number);
        textView.setText(message);
        mLayout = (LinearLayout) findViewById(R.id.scrollLayout);
        String[] test = {"hi", "there"};
        setQuestions("test", test);
        setQuestions("test2", test);


    }

    public void setQuestions(String question, String[] answers){ //Sets the arrays of questions to put into the scrollview
        //TODO
        mLayout.addView(createNewTextView(question));
        for(int i = 0; i < answers.length; i++){
            mLayout.addView(createNewQuestions(answers[i]));
        }

    }

    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setTextSize(32);
        return textView;
    }

    private TextView createNewQuestions(String question) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLayout);
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final Button button = new Button(this);
        button.setLayoutParams(lparams);
        button.setText(question);
        button.setTextSize(32);
        //button.setWidth(layout.getWidth());
        return button;
    }


    public void castVote(){ //Sends users answers to the server
        //TODO
    }
}
