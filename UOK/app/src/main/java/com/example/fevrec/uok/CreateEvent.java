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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fevrec.uok.tools.ContactPickerMulti;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity {

    private Calendar myCalendar;
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText address;
    private EditText dateEvent;
    private EditText dateLimite;

    private ArrayList<String> listPrioritairePhone1;
    private ArrayList<String> listPrioritairePhone2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Mettre requete ici", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                String s = dateEvent.getText().toString() + " " + selectedHour + ":" + selectedMinute;
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
                String s = dateLimite.getText().toString() + " " + selectedHour + ":" + selectedMinute;
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
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
