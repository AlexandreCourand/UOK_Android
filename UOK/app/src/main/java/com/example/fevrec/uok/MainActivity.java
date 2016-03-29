package com.example.fevrec.uok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fevrec.uok.tools.Tools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_URL = "http://www.nbeaussart.me:8080/";

    public static String authToken;

    private boolean logIn = true;
    private EditText pseudo;
    private EditText name;
    private EditText mdp;
    private Button change;

    public void get(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final String URL = SERVER_URL + "v1/me";
        // Post params to be sent to the server
        final HashMap<String, String> params = new HashMap<>();
        authToken = "Basic " + Tools.toBase64(pseudo.getText().toString()+":"+mdp.getText().toString());
        params.put("Authorization", authToken);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), EventView.class);
                        intent.putExtra("AuthToken",authToken);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() {
                return params;
            }
        };

        queue.add(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pseudo = (EditText) findViewById(R.id.pseudo);
        name = (EditText) findViewById(R.id.name);
        mdp = (EditText) findViewById(R.id.password);
        change = (Button) findViewById(R.id.change);

        pseudo.requestFocus();
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

    public void login(View v){
        if (logIn){
            get();
        } else {
            register();
        }
    }

    private void register(){

        RequestQueue queue = Volley.newRequestQueue(this);
        final String URL = SERVER_URL + "v1/user";
    // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("alias",pseudo.getText().toString());
        params.put("password",mdp.getText().toString());


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), EventView.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.account_not_created, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(req);
    }
}
