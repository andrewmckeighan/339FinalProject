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

public class MainActivity extends AppCompatActivity {

    //private TextView waiting = new TextView(this);
    private boolean isKeyTrue = false;
    public final static String EXTRA_MESSAGE = "com.cs339.youvote.MESSAGE";
    private TextView keyTruth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyTruth = (TextView) findViewById(R.id.truth);
       // waiting.setText("Waiting for Server Response");

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
            boolean tst = sendKey(message);
            if(sendKey(message)) {
                startActivity(intent);
            }
        //}
    }

    //Sends the key
    public boolean sendKey(String inputKey) throws JSONException {
        //LooperThread thread = new LooperThread();
        JSONObject keyFile = new JSONObject();
        keyFile.put("session", inputKey);
//        final boolean[] isKeyTrue = {false};

        Socket conn = null;
        try {
            conn = IO.socket("http://10.0.2.2:6668/");
            conn.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        run();
        conn.on("keyconf", new Emitter.Listener(){
            public void call(Object... objects) {
                Log.w("Main", "Call");
                if(objects.length > 0) {
                    String resp = (String) objects[0];
                    if(resp.equals("true")){
                        isKeyTrue = true;
                        Log.w("Main", "Key: True");
//                        keyTruth.setText("true");
                    }
                }

            }
        });
        conn.emit("enterRoom" , keyFile);
        //TODO send the key to the server
        return isKeyTrue;
    }

    public Handler mHandler;

    public void run() {


        mHandler = new Handler() {
            public void handleMessage(Message msg){
                while(isKeyTrue!=true);
                keyTruth.setText("true");
            }
        };


    }

}

class LooperThread extends Thread {

}