package com.example.fevrec.uok;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
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
        Toast.makeText(this, getIntent().getExtras().getString("AuthToken"), Toast.LENGTH_SHORT).show(); // OUIIIIIIIIIIIIIi
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //START
                RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

                final String URL = MainActivity.SERVER_URL + "v1/me/event";
                // Post params to be sent to the server
                final HashMap<String, String> params = new HashMap<>();
                //if(address!=null && address.)
                params.put("location", address.getText().toString());
                params.put("name", name.getText().toString());

                //Generation de la date

                String s = dateEvent.getText().toString().replaceAll(":", "/").replaceAll(" ", "/");
                String[] dividedDate = s.split("[/]");
                Date d = new Date();
                if(dividedDate != null && dividedDate.length>0) {
                    d.setMonth(Integer.parseInt(dividedDate[0]));
                    d.setDate(Integer.parseInt(dividedDate[1]));
                    d.setYear(Integer.parseInt(dividedDate[2]) + 100);
                    d.setHours(Integer.parseInt(dividedDate[3]));
                    d.setMinutes(Integer.parseInt(dividedDate[4]));
                    d.setSeconds(0);
                    //Enfin
                    params.put("date", d.toString());
                }
                /*
                s = dateLimite.getText().toString().replaceAll(":", "/").replaceAll(" ", "/");
                dividedDate = s.split("[/]");
                if(dividedDate != null && dividedDate.length>0) {
                    d.setMonth(Integer.parseInt(dividedDate[0]));
                    d.setDate(Integer.parseInt(dividedDate[1]));
                    d.setYear(Integer.parseInt(dividedDate[2]) + 100);
                    d.setHours(Integer.parseInt(dividedDate[3]));
                    d.setMinutes(Integer.parseInt(dividedDate[4]));
                    d.setSeconds(0);
                    params.put("limiteTime", d.toString());
                }*/

                //params.put("date", new Date(2016,03,30,12,30,30).toString());
                params.put("isRush", isRush.isChecked() + "");
                params.put("cost", price.getText().toString());
                params.put("nmbPlaces", nbPlaces.getText().toString());
                params.put("desciption", description.getText().toString());

                JSONArray jsonArray = new JSONArray();

                //Les utilisateurs prioritaire
                if(listPrioritairePhone1 != null && !listPrioritairePhone1.isEmpty()) {
                    for (int i = 0; i < listPrioritairePhone1.size(); ++i) {
                        Invit invit = new Invit();
                        invit.setSecondaryList(false);
                        User user = new User();
                        user.setTelNumber(listPrioritairePhone1.get(i));
                        invit.setUserObject(user);
                        jsonArray.put(new Gson().toJson(invit));
                    }
                }
                //Les utilisateurs non prioritaire
                if(listPrioritairePhone2 != null && !listPrioritairePhone2.isEmpty()) {
                    for (int i = 0; i < listPrioritairePhone2.size(); ++i) {
                        Invit invit = new Invit();
                        invit.setSecondaryList(true);
                        User user = new User();
                        user.setTelNumber(listPrioritairePhone2.get(i));
                        invit.setUserObject(user);
                        jsonArray.put(new Gson().toJson(invit));
                    }
                }

                JSONObject object = new JSONObject(params);

                ArrayList<User> lit = new ArrayList();
                User user = new User();
                user.setTelNumber("123456789");
                User user2 = new User();
                user2.setTelNumber("1234567890");
                lit.add(user2);

                try {
                    object.put("invit",new Gson().toJson(new ArrayList<Invit>()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("erreur",e.toString());
                }
                /*try {
                    object.put("invit", jsonArray);
                } catch (JSONException e) {

                    e.printStackTrace();
                }*/

                Log.d("CreateEvent ::: ", object.toString());

                final HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getIntent().getExtras().getString("AuthToken"));
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(getApplicationContext(), EventView.class);
                                intent.putExtra("AuthToken", getIntent().getExtras().getString("AuthToken"));
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Erreur lors de la création d'événement", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), EventView.class);
                        intent.putExtra("AuthToken", getIntent().getExtras().getString("AuthToken"));
                        startActivity(intent);
                    }
                }) {
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                    public Map<String, String> getParams() {
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
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int fromList = data.getIntExtra("list", 0);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                String[] array = b.getStringArray("PICK_CONTACT");
                if (fromList == R.id.button_select_liste_contact_1) {
                    listPrioritairePhone1 = new ArrayList<>();
                    for (String s : array)
                        listPrioritairePhone1.add(s);
                } else if (fromList == R.id.button_select_liste_contact_2) {
                    listPrioritairePhone2 = new ArrayList<>();
                    for (String s : array)
                        listPrioritairePhone2.add(s);
                } else {
                    Toast.makeText(this, "Erreur impossible, bizarre...", Toast.LENGTH_SHORT).show();
                }
            } else {
                //Write your code if there's no result
            }
        }
    }

}
