package com.example.fevrec.uok;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Mes_Evenement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes__evenement);
       // Spinner sp = (Spinner) findViewById(R.id.spinnerEvent);
        ArrayAdapter arrayAdapter= ArrayAdapter.createFromResource(this,0,android.R.layout.simple_spinner_item); // changer le 0 par les événements contenu dans la base

        //sp.setAdapter(arrayAdapter);

        afficherEvenement(getIntent().getExtras().getInt("pos"));
        
       /* sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                afficherEvenement(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    public void afficherEvenement(int position){
        final String URL = MainActivity.SERVER_URL + "v1/me/myEvents/" + position;

        RequestQueue queue = Volley.newRequestQueue(this);

        final HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", getIntent().getExtras().getString("AuthToken"));

        final JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("test",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_fetch_data, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), getIntent().getExtras().getString("AuthToken"), Toast.LENGTH_LONG).show();
            }
        }) {
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        queue.add(req);


    }



}
