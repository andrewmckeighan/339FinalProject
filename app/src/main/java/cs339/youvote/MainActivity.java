package cs339.youvote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.cs339.youvote.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void submit(View view){
        if(true){ //This should be whether the sessionKey is true. Fix it later.
            Intent intent = new Intent(this, activity_session.class);
            EditText editText = (EditText) findViewById(R.id.session_code);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            //if(sendKey(message)) {
                startActivity(intent);
            //}
        }
    }

    //Sends the key
    public boolean sendKey(String key){
        boolean isKeyTrue = false;
        //TODO send the key to the server
        return isKeyTrue;
    }

}
