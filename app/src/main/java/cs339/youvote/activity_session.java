package cs339.youvote;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import org.json.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class activity_session extends AppCompatActivity {
    private LinkedBlockingQueue<String> questionQueue = new LinkedBlockingQueue<String>();
    private String message;
    private String question;
    private boolean flag = false;
    private String[] answers;
    private ArrayList<String> answerQuestions = new ArrayList<String>();
    private TextView waiting;
    private LinearLayout mLayout;
    private int qIdNum = 0;
    private ArrayList<Integer> answerList = new ArrayList<Integer>();
    private ArrayList<RadioGroup> radioGrpArr = new ArrayList<RadioGroup>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.session_number);
        textView.setText(message);
        mLayout = (LinearLayout) findViewById(R.id.scrollLayout);
        waiting = createNewTextView("Waiting for questions...");
        mLayout.addView(waiting);
//        String[] test = {"hi", "there"};
//        String[] test2 = {"hi", "there", "How", "Are","You?"};
//        setQuestions("test", test);
//        setQuestions("test2", test2);
//        while(flag == false){
//            try {
//                recieveQuestions();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    }




    public void sendQuestions(String key){
        Socket conn = null;
        try {
            conn = IO.socket("http://localhost:1337");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        conn.on("sendQA", new Emitter.Listener(){
            public void call(Object... objects) {
                if(objects.length > 0) {
                    mLayout.removeView(waiting);
                    String quest = (String) objects[0];
                    try {
                        JSONObject json = new JSONObject(quest);
                        String question = json.getString("Question");
                        JSONObject answersjson = json.getJSONObject("Answers");
                        ArrayList<String> answers = new ArrayList<String>();
                        try{
                            for(int i = 1; i<=10; i++){
                                answers.add(answersjson.getString(""+i));
                            }
                            setQuestions(question, answers);
                        } catch(JSONException e){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    public void setQuestions(String question, ArrayList<String> answers){ //Sets the arrays of questions to put into the scrollview
        //TODO

        mLayout.addView(createNewTextView(question));
        RadioGroup RG = new RadioGroup(this);
        for(int i = 0; i < answers.size(); i++){
            RG.addView(createNewQuestions(answers.get(i), ((qIdNum*10)+i)));
            RG.setId(qIdNum);
        }
        radioGrpArr.add(RG);
        mLayout.addView(RG);
        qIdNum++;
    }

    private TextView createNewTextView(String text) {
        mLayout.removeView(waiting);
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setTextSize(32);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private Button createNewQuestions(String question, int btnId) {

        LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLayout);
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final RadioButton button = new RadioButton(this);
        button.setLayoutParams(lparams);
        button.setText(" "+ question);
        button.setTextSize(32);
        button.setId(btnId);
        button.setPadding(5,20,0,20);
        button.setButtonDrawable(android.R.color.transparent);
//        button.setBackgroundColor(Color.TRANSPARENT);
//        button.getBackground().setAlpha(80);
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


    public void castVote(View view) throws JSONException { //Sends users answers to the server
        JSONObject json = new JSONObject();
        json.put("session", message);
        for(int i = 0; i < qIdNum; i++){
            int id = radioGrpArr.get(i).getCheckedRadioButtonId();
            System.out.println(i + " " + id);
            json.put("Answer", id);
        }
        Socket conn = null;
        try {
            conn = IO.socket("http://localhost:1337");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        conn.emit("answerQuestion" , json);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //TODO
    }


    public void recieveQuestions() throws JSONException {
        //isKeyTrue = false;
        //LooperThread thread = new LooperThread();
        JSONObject questionFile = new JSONObject();

        Socket conn = null;
        try {
            conn = IO.socket("http://10.0.2.2:6668/");
            conn.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        conn.on("submitQA", new Emitter.Listener(){
            public void call(Object... objects) {
                Log.w("Main", "Call");
                int length = objects.length;
                if(objects.length > 0) {
                    try {
                        questionQueue.put((String) objects[0]);
                        for(int i=1; i<length; i++){
                            questionQueue.put((String) objects[i]);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    question = (String) objects[0];
                    for(int i=1; i<length; i++){
                        answerQuestions.add((String) objects[i]);
                    }
                }

            }

        });
        try {
            question = questionQueue.take();
            while(!questionQueue.isEmpty()){
                answerQuestions.add(questionQueue.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        setQuestions(question, answerQuestions);
    }



}

