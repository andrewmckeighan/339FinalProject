package cs339.youvote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class activity_session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.session_number);
        textView.setText(message);

    }

    public void setQuestions(String question, String[] answers){ //Sets the arrays of questions to put into the scrollview
        //TODO
        


    }

    public void castVote(){ //Sends users answers to the server
        //TODO
    }
}
