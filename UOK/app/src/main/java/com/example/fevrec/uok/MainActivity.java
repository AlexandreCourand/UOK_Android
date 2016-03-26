package com.example.fevrec.uok;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fevrec.uok.res.User;
import com.google.gson.Gson;

public class MainActivity extends Activity {

    String URL = "http://www.nbeaussart.me:9090/v1/userdb/1";

    private boolean logIn = true;
    private EditText pseudo;
    private EditText name;
    private EditText mdp;
    private Button change;

    public User get(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final User[] tmp = new User[1];
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.d("test", json);
                        tmp[0] = new Gson().fromJson(json, User.class);
                        Log.d("test", tmp[0].toString());
                        Log.d("test", new Gson().toJson(tmp[0]));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
        return tmp[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pseudo = (EditText) findViewById(R.id.pseudo);
        name = (EditText) findViewById(R.id.name);
        mdp = (EditText) findViewById(R.id.password);
        change = (Button) findViewById(R.id.change);

        get();
    }

    public void login(View v){
        if (logIn){
            Toast.makeText(this,"Do Login", Toast.LENGTH_SHORT).show();
            //TODO la connection au site.
        } else {
            Toast.makeText(this, "Do register", Toast.LENGTH_SHORT).show();
            //Todo l'enregistrement d'un nouvel utilisateur.
        }
    }

    public void change(View v){
        if (logIn){
            logIn = false;
            name.setVisibility(View.VISIBLE);
            change.setText(R.string.CanLog);
        }
        else {
            logIn = true;
            name.setVisibility(View.GONE);
            change.setText(R.string.register);
        }
    }
}
