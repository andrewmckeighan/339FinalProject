package cs339.youvote;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.*;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity {

    //private TextView waiting = new TextView(this);
    private LinkedBlockingQueue<String> trueQueue = new LinkedBlockingQueue<String>();
    private boolean isKeyTrue = false;
    public final static String EXTRA_MESSAGE = "com.cs339.youvote.MESSAGE";
    private TextView keyTruth;

    private static Socket conn;

    public static final String LOCAL_HOST = "http://10.0.2.2:6668/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyTruth = (TextView) findViewById(R.id.truth);
        // waiting.setText("Waiting for Server Response");
    }

    public static Socket getConn() {
        if (conn != null && conn.connected()) {
            return conn;
        }
        try {
            conn = IO.socket(MainActivity.LOCAL_HOST);
            conn.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return conn;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //submit code to enter session
    public void submit(View view) throws JSONException {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Waiting for server...");
        //if(true){ //This should be whether the sessionKey is true. Fix it later.
        Intent intent = new Intent(this, activity_session.class);
        EditText editText = (EditText) findViewById(R.id.session_code);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        sendKey(message, intent);
    }

    //Sends the key
    public void sendKey(String inputKey, final Intent intent) throws JSONException {
        isKeyTrue = false;
        JSONObject keyFile = new JSONObject();
        keyFile.put("session", inputKey);

        getConn().on("keyconf", new Emitter.Listener() {
            public void call(Object... objects) {
                Log.w("Main", "Call");
                if (objects.length > 0) {
                    String resp = (String) objects[0];
                    if (resp.equals("true")) {
                        try {
                            trueQueue.put("true");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isKeyTrue = true;
                        Log.w("Main", "Key: True");
                    }
                }
            }
        });
        System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("MainActivity.sendKey " + MainActivity.getConn());
        MainActivity.getConn().on("sendQA", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            runMe();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    private void runMe() throws Exception {
                        if (args == null || args.length == 0) {
                            return;
                        }
                        String s = (String) args[0];
                        JSONObject object = new JSONObject(s);
                        String question = object.getString("Question");
                        JSONObject ansObj = (JSONObject) object.get("Answers");
                        ArrayList<String> answers = new ArrayList<>();
                        for (Iterator<String> iter = ansObj.keys(); iter.hasNext(); ) {
                            answers.add(ansObj.getString(iter.next()));
                        }
                        intent.putExtra("question", question);
                        intent.putExtra("answers", answers);

                        if (isKeyTrue) {
                            System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getId());
                            System.out.println("MainActivity.sendKey " + MainActivity.getConn());
                            startActivity(intent);
                        }
                    }
                };
                handler.handleMessage(new Message());
              //  Looper.loop();
            }
        });

        getConn().emit("enterRoom", keyFile);
        //getConn().emit("answerQA", keyFile);
        //TODO send the key to the server
        boolean answer = false;
        try {
            //if (!trueQueue.isEmpty()) {
            if (trueQueue.take().equals("true")) {
                isKeyTrue = true;
            }
            //}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
