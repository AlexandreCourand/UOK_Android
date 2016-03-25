package com.example.fevrec.uok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.frenchcomputerguy.rest.PostRequest;
import com.frenchcomputerguy.rest.Request;
import com.frenchcomputerguy.utils.JSONElement;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> param = new HashMap<>();
        param.put("Test", "this is a test");
        Request req = new PostRequest("http://httpbin.org/post", param);
        JSONElement response = req.getResponse();
        if (response != null) {
            try {
                Log.d("gros caca",response.getJSONObject().getJSONObject("headers").getString("Test"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
       /** if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    public void loginOk(View v){
        EditText pseudo = (EditText) findViewById(R.id.pseudo);
        EditText mdp = (EditText) findViewById(R.id.password);

        String p = String.valueOf(pseudo.getText());
        String m = String.valueOf(mdp.getText());
        if(p.equals("alan")&&m.equals("turing")){
            System.out.println("ok");
            Log.d("Authorized","Authorized");
            Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
            startActivity(intent);
        }
        else {
            Log.d("Denied","Denied");

        }


    }
}
