package com.example.fevrec.uok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fevrec.uok.res.User;
import com.google.gson.Gson;

public class MainActivity extends Activity {

    String URL = "http://www.nbeaussart.me:9090/v1/userdb/1";

    public void get(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.d("test", json);
                        User tmp = new Gson().fromJson(json, User.class);
                        Log.d("test",tmp.toString());
                        Log.d("test", new Gson().toJson(tmp));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get();
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
