package com.example.fevrec.uok;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fevrec.uok.res.Event;
import com.example.fevrec.uok.tools.Tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventView extends Activity {

    private ListView listView;
    private ArrayAdapter eventAdapter;
    private List<Event> events;

    private void getMyEvents(){

        final String URL = MainActivity.SERVER_URL + "v1/me/myEvents";
        // eventAdapter.notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Post params to be sent to the server
        final HashMap<String, String> params = new HashMap<>();

        params.put("Authorization", MainActivity.encodedUser);

        final JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0;i<response.length();++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Date date = Tools.parseStringToDate((String)jsonObject.get("date"));
                                events.add(new Event( jsonObject.getInt("id"), jsonObject.getInt("owner"), date, (String) jsonObject.get("name")));
                                eventAdapter.notifyDataSetChanged();
                            } catch (Exception e){
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_fetch_data, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_event_view);

        events = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listEvent);
        eventAdapter = new EventAdapter(this, R.layout.event_item, events);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position :" + position, Toast.LENGTH_SHORT).show();
            }
        });

        getMyEvents();
    }

}
