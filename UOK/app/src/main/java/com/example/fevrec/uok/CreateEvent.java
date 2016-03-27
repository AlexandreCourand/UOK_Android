package com.example.fevrec.uok;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fevrec.uok.res.Invit;
import com.example.fevrec.uok.res.User;
import com.example.fevrec.uok.tools.ContactPickerMulti;
import com.example.fevrec.uok.tools.Tools;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {

    private Calendar myCalendar;
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText address;
    private EditText dateEvent;
    private EditText dateLimite;
    private EditText nbPlaces;
    private Switch isRush;

    private ArrayList<String> listPrioritairePhone1;
    private ArrayList<String> listPrioritairePhone2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //START
                RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

                final String URL = MainActivity.SERVER_URL + "v1/me/event";
                // Post params to be sent to the server
                final HashMap<String, String> params = new HashMap<>();
                params.put("location", address.getText().toString());
                params.put("name", name.getText().toString());

                //Generation de la date
                String s = dateEvent.getText().toString().replaceAll(":","/").replaceAll(" ","/");
                String[] dividedDate = s.split("[/]");
                Date d = new Date();
                d.setMonth(Integer.parseInt(dividedDate[0]));
                d.setDate(Integer.parseInt(dividedDate[1]));
                d.setYear(Integer.parseInt(dividedDate[2])+2000);
                d.setHours(Integer.parseInt(dividedDate[3]));
                d.setMinutes(Integer.parseInt(dividedDate[4]));
                d.setSeconds(0);
                //Enfin
                params.put("date", d.toString());

                s = dateLimite.getText().toString().replaceAll(":","/").replaceAll(" ","/");
                dividedDate = s.split("[/]");
                d.setMonth(Integer.parseInt(dividedDate[0]));
                d.setDate(Integer.parseInt(dividedDate[1]));
                d.setYear(Integer.parseInt(dividedDate[2])+2000);
                d.setHours(Integer.parseInt(dividedDate[3]));
                d.setMinutes(Integer.parseInt(dividedDate[4]));
                d.setSeconds(0);
                params.put("limiteTime", d.toString());

                //Enfin
                params.put("date", d.toString());
                params.put("isRush",isRush.isChecked() + "");
                params.put("cost", price.getText().toString());
                params.put("nmbPlaces", nbPlaces.getText().toString());
                params.put("desciption", description.getText().toString());

                JSONArray jsonArray = new JSONArray();

                //Les utilisateurs prioritaire
                for (int i = 0;i<listPrioritairePhone1.size();++i){
                    Invit invit = new Invit();
                    invit.setSecondaryList(false);
                    User user = new User();
                    user.setTelNumber(listPrioritairePhone1.get(i));
                    invit.setUserObject(user);
                    jsonArray.put(new Gson().toJson(invit));
                }
                //Les utilisateurs non prioritaire
                for (int i = 0;i<listPrioritairePhone2.size();++i){
                    Invit invit = new Invit();
                    invit.setSecondaryList(true);
                    User user = new User();
                    user.setTelNumber(listPrioritairePhone2.get(i));
                    invit.setUserObject(user);
                    jsonArray.put(new Gson().toJson(invit));
                }

                JSONObject object = new JSONObject(params);

                try {
                    object.put("invit", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("CreateEvent ::: ", object.toString());


                params.put("Authorization", MainActivity.encodedUser);

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    public Map<String, String> getHeaders() {
                        return params;
                    }
                };

                queue.add(req);

                //END
            }
        });

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateEventPopUp = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDateEvent();
            }

        };

        final DatePickerDialog.OnDateSetListener dateEventLimit = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDateLimit();
            }

        };

        name = (EditText) findViewById(R.id.create_event_name);
        description = (EditText) findViewById(R.id.create_event_description);
        price = (EditText) findViewById(R.id.create_event_price);
        address = (EditText) findViewById(R.id.create_event_address);
        dateEvent = (EditText) findViewById(R.id.create_event_date);
        dateLimite = (EditText) findViewById(R.id.create_event_limit_date);
        isRush = (Switch) findViewById(R.id.create_event_switch_rush);
        nbPlaces = (EditText) findViewById(R.id.create_event_limited_place);

        dateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEvent.this, dateEventPopUp, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
        });

        dateLimite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEvent.this, dateEventLimit, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabelDateEvent() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEvent.setText(sdf.format(myCalendar.getTime()));
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                NumberFormat formatter = new DecimalFormat("00");
                String s = dateEvent.getText().toString() + " " + formatter.format(selectedHour) + ":" + formatter.format(selectedMinute);
                dateEvent.setText(s);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void updateLabelDateLimit() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateLimite.setText(sdf.format(myCalendar.getTime()));
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                NumberFormat formatter = new DecimalFormat("00");
                String s = dateLimite.getText().toString() + " " + formatter.format(selectedHour) + ":" + formatter.format(selectedMinute);
                dateLimite.setText(s);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    public void getInviteList (View view){
        Intent intent = new Intent(this, ContactPickerMulti.class);
        intent.putExtra("list", view.getId());
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int fromList = data.getIntExtra("list", 0);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bundle b = data.getExtras();
                String[] array = b.getStringArray("PICK_CONTACT");
                if (fromList == R.id.button_select_liste_contact_1){
                    listPrioritairePhone1 = new ArrayList<>();
                    for(String s: array)
                        listPrioritairePhone1.add(s);
                } else if (fromList == R.id.button_select_liste_contact_2){
                    listPrioritairePhone2 = new ArrayList<>();
                    for (String s : array)
                        listPrioritairePhone2.add(s);
                } else {
                    Toast.makeText(this, "Erreur impossible, bizarre...", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                //Write your code if there's no result
            }
        }
    }

}
