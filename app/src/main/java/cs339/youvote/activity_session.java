package cs339.youvote;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class activity_session extends AppCompatActivity {

    private LinearLayout mLayout;
    private int qIdNum = 0;
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
        RadioGroup RG = new RadioGroup(this);
        for(int i = 0; i < answers.length; i++){
            RG.addView(createNewQuestions(answers[i], ((qIdNum*10)+i)));
        }
        mLayout.addView(RG);
        qIdNum++;
    }

    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setTextSize(32);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private Button createNewQuestions(String question, final int btnId) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLayout);
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final RadioButton button = new RadioButton(this);
        button.setLayoutParams(lparams);
        button.setText(" "+ question);
        button.setTextSize(32);
        button.setId(btnId);
        button.setButtonDrawable(android.R.color.transparent);
//        button.setBackgroundColor(Color.TRANSPARENT);
        //button.getBackground().setAlpha(80);
        button.setBackgroundResource(R.drawable.buttonbackground);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //TODO
            }
        });
        //button.setWidth(layout.getWidth());
        return button;
    }


    public void castVote(){ //Sends users answers to the server
        //TODO
    }
}
